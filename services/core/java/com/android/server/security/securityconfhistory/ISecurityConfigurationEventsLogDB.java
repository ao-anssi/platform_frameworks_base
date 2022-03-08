package com.android.server.security.securityconfhistory;

import java.util.List;
import android.security.securityconfhistory.GrantRuntimePermissionEvent;
import android.security.securityconfhistory.RevokeRuntimePermissionEvent;
import android.security.securityconfhistory.SecurityConfigurationEventDescription;

public interface ISecurityConfigurationEventsLogDB {

    public void addEvent(GrantRuntimePermissionEvent event);

    public void addEvent(RevokeRuntimePermissionEvent event);

    public void cleanDB();

    public List<SecurityConfigurationEventDescription> listEvents();
}
