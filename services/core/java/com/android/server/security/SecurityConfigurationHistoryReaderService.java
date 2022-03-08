package com.android.server.security;

import android.security.securityconfhistory.SecurityConfigurationEventDescription;
import android.security.securityconfhistory.ISecurityConfigurationHistoryReaderService;
import com.android.server.security.securityconfhistory.SecurityConfigurationEventsLogDBNoThread;
import com.android.server.security.securityconfhistory.SecurityConfigurationEventsLogDBHolder;
import java.util.List;
import java.util.ArrayList;
import android.content.Context;

import android.util.Slog;

public class SecurityConfigurationHistoryReaderService  extends ISecurityConfigurationHistoryReaderService.Stub {

    private Context service_context;
    private final String TAG = this.getClass().getName();
    private SecurityConfigurationEventsLogDBNoThread logdb;

    public SecurityConfigurationHistoryReaderService(Context context) {
        service_context = context;
        logdb = SecurityConfigurationEventsLogDBHolder.getLogDB(context);
    }    
    
    @Override
    public List<SecurityConfigurationEventDescription> listEvents() {
        Slog.w(TAG, "***** listEvents");
        return logdb.listEvents();
    }
}
