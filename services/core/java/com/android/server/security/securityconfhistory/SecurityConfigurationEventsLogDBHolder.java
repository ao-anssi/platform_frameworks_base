package com.android.server.security.securityconfhistory;

import android.content.Context;

public class SecurityConfigurationEventsLogDBHolder {
    static private SecurityConfigurationEventsLogDBNoThread m_logdb = null;
    
    static public SecurityConfigurationEventsLogDBNoThread getLogDB(Context context) {
        if (m_logdb == null) {
            m_logdb = new SecurityConfigurationEventsLogDBNoThread(context);
        }
        return m_logdb;
    }
}

