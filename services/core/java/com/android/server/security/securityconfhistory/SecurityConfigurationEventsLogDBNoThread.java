package com.android.server.security.securityconfhistory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.security.securityconfhistory.GrantRuntimePermissionEvent;
import android.security.securityconfhistory.RevokeRuntimePermissionEvent;
import android.security.securityconfhistory.SecurityConfigurationEvent;
import android.security.securityconfhistory.SecurityConfigurationEventDescription;

import android.database.DatabaseUtils;

public class SecurityConfigurationEventsLogDBNoThread implements ISecurityConfigurationEventsLogDB {
    SQLiteOpenHelper mHelper;
    List<SecurityConfigurationEventDescription> mCurrentEventsList;
    boolean dirty;
    Context m_context;

    public final static int MSG_POST_GRANT = 1;
    public final static int MSG_POST_REVOKE = 2;
    public final static int MSG_CLEAN = 3;

    final String TAG = "SecurityConfigurationEventsLogDB";
    final String DB_NAME = "EventLogDB";
    final int DB_VERSION = 1;
    final int IDLE_CONNECTION_TIMEOUT_MS = 30000;
    final String TAB_LOG = "permission_event_log";
    final String EVENT_TYPE = "type";
    final String PACKAGENAME = "package";
    final String PERMISSIONNAME = "permission";
    final String USERID = "userid";
    final String REASON = "reason";
    final String TIME = "time";

    public SecurityConfigurationEventsLogDBNoThread(Context in_context){
        dirty = true;
        m_context = in_context;
        init(m_context);
    }

    public void init(Context context) {
        mHelper = new SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE " + TAB_LOG + " (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                EVENT_TYPE + " INT," +
                                PACKAGENAME + " STRING," +
                                PERMISSIONNAME + " STRING," +
                                USERID + " INT," +
                                REASON + " STRING,"+
                                TIME + " INT"+
                        ")");
            }

            @Override
            public void onConfigure(SQLiteDatabase db) {
                // Memory optimization - close idle connections after 30s of inactivity
                setIdleConnectionTimeout(IDLE_CONNECTION_TIMEOUT_MS);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                if (oldVersion != newVersion) {
                    db.execSQL("DROP TABLE IF EXISTS " + TAB_LOG);
                    onCreate(db);
                }
            }
        };
    }

    synchronized private void  writeEvent(SecurityConfigurationEvent event) {
        Log.d(TAG, "***** write event");
        ContentValues cv = new ContentValues();
        cv.put(EVENT_TYPE, event.getType());
        cv.put(PACKAGENAME, DatabaseUtils.sqlEscapeString(event.getPackageName()));
        cv.put(PERMISSIONNAME, DatabaseUtils.sqlEscapeString(event.getPermissionName()));
        cv.put(USERID, event.getUserId());
        cv.put(REASON, DatabaseUtils.sqlEscapeString(event.getReason()));
        cv.put(TIME, event.getTime());
        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            if (db.insert(TAB_LOG, null, cv) < 0) {
                Log.wtf(TAG, "Error while trying to insert values: " + cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        dirty = true;
    }

    synchronized private void emptyTable() {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM " + TAB_LOG + ";");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        dirty = true;
    }

    public void addEvent(GrantRuntimePermissionEvent event) {
        writeEvent(event);
    }

    public void addEvent(RevokeRuntimePermissionEvent event) {
        writeEvent(event);
    }

    public void cleanDB() {
        emptyTable();
    }

    synchronized private void updateEventsList() {
        Log.d(TAG, "****** list event update");
        SQLiteDatabase db = mHelper.getReadableDatabase();
        List<SecurityConfigurationEventDescription> result = new ArrayList<SecurityConfigurationEventDescription>();

        String request = "SELECT * FROM "+TAB_LOG;
        Cursor cursor = db.rawQuery(request, null);
        try {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Log.d(TAG, "**** evt : " + cursor.getString(1));
                int event_type = cursor.getInt(1);
                String pkg = removeQuotes(cursor.getString(2));
                String permission = removeQuotes(cursor.getString(3));
                int userid = cursor.getInt(4);
                String reason = removeQuotes(cursor.getString(5));
                long time = cursor.getLong(6);

                /*
                if (event_type == SecurityConfigurationEvent.GRANT_RUNTIME_CONFIGURATION_EVENT) {
                    SecurityConfigurationEvent ev = new GrantRuntimePermissionEvent(permission, pkg, userid, time);
                    result.add(ev);
                }
                if (event_type == SecurityConfigurationEvent.REVOKE_RUNTIME_CONFIGURATION_EVENT) {
                    SecurityConfigurationEvent ev = new RevokeRuntimePermissionEvent(permission, pkg, userid, reason, time);
                    result.add(ev);
                }*/
                if (event_type == SecurityConfigurationEvent.GRANT_RUNTIME_CONFIGURATION_EVENT) {
                    SecurityConfigurationEventDescription ev = new SecurityConfigurationEventDescription(permission,pkg,userid,
                                                        time,reason,SecurityConfigurationEventDescription.GRANT_PERMISSION_EVENT);
                    result.add(ev);                                                        
                    continue;
                }
                if (event_type == SecurityConfigurationEvent.REVOKE_RUNTIME_CONFIGURATION_EVENT) {
                    SecurityConfigurationEventDescription ev = new SecurityConfigurationEventDescription(permission,pkg,userid,
                                                        time,reason,SecurityConfigurationEventDescription.REVOKE_PERMISSION_EVENT);
                    result.add(ev);
                    continue;
                }
                SecurityConfigurationEventDescription ev =  new SecurityConfigurationEventDescription("unknown", "unknown", 0, 0,   "unknown", SecurityConfigurationEventDescription.UNKNOWN_EVENT);
                result.add(ev);
            }
        } finally {
            cursor.close();
        }

        Collections.reverse(result);
        mCurrentEventsList = result;
        dirty = false;
    }

    private String removeQuotes(String quoted) {
        String result = "";
    
        if (quoted.startsWith("'") && quoted.endsWith("'")) {
                if (quoted.length() > 2)
                    result = quoted.substring(1, quoted.length()-1);
        }
        
        return result;
    }    
    
    public List<SecurityConfigurationEventDescription> listEvents() {
        if (dirty) {
            updateEventsList();
        }
        return mCurrentEventsList;
    }
}
