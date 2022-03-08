package com.android.server.security;

import android.os.IBinder;
import android.content.Intent;
import com.android.server.SystemService;
import android.content.Context;
import android.security.securityconfhistory.ISecurityConfigurationHistoryService;
import android.util.Slog;

import java.util.Date;


import android.security.securityconfhistory.GrantRuntimePermissionEvent;
import android.security.securityconfhistory.RevokeRuntimePermissionEvent;
import com.android.server.security.securityconfhistory.SecurityConfigurationEventsLogDBNoThread;
import com.android.server.security.securityconfhistory.SecurityConfigurationEventsLogDBHolder;


/**
 * A Service that provides an history of security configuration changes.
 * @hide
 */
public class SecurityConfigurationHistoryService extends ISecurityConfigurationHistoryService.Stub
 {
    private Context service_context;
    private final String TAG = this.getClass().getName();
    private SecurityConfigurationEventsLogDBNoThread logdb;
    private boolean isRecording;
    
    public SecurityConfigurationHistoryService(Context context) {
        service_context = context;
        logdb = SecurityConfigurationEventsLogDBHolder.getLogDB(context);
        isRecording = false;
    }
    
    
        @Override    
        public void startRecording() {
            isRecording = true;
        }
    
        @Override
        public void runtimePermissionGranted(String packageName, String permName, int userId){   
            if (!isRecording) return;        
            Slog.w(TAG, "***** runtimePermissionGranted : "+packageName + " : " + permName + " :" + String.valueOf(userId));
            long time = new Date().getTime();
            GrantRuntimePermissionEvent grant_event = new GrantRuntimePermissionEvent(permName, packageName, userId, time);
            logdb.addEvent(grant_event);
        }
        
        @Override
        public void runtimePermissionRevoked(String packageName, String permName, int userId, String reason) {
            if (!isRecording) return;        
            Slog.w(TAG, "***** runtimePermissionRevoked : "+packageName + " : " + permName + " :" + String.valueOf(userId) + " :" + reason);
            long time = new Date().getTime();
            RevokeRuntimePermissionEvent revoke_event = new RevokeRuntimePermissionEvent(packageName, permName, userId, reason, time);
            logdb.addEvent(revoke_event);
            
        }    

}
