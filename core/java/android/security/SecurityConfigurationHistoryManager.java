package android.security;

import android.annotation.NonNull;
import android.annotation.RequiresPermission;
import android.annotation.SystemService;
import android.content.Context;
import android.os.RemoteException;
import java.util.List;
import java.util.ArrayList;

import android.security.securityconfhistory.ISecurityConfigurationHistoryReaderService;
import android.security.securityconfhistory.SecurityConfigurationEventDescription;
import android.security.securityconfhistory.GrantRuntimePermissionEvent;
import android.security.securityconfhistory.SecurityConfigurationEventFactory;
import android.security.securityconfhistory.SecurityConfigurationEvent;

@SystemService(Context.SECURITY_CONF_HISTORY_READER_SERVICE)
public class SecurityConfigurationHistoryManager {
    @NonNull private final ISecurityConfigurationHistoryReaderService mService;
    
    /** @hide */
    public SecurityConfigurationHistoryManager(@NonNull ISecurityConfigurationHistoryReaderService service) {
        mService = service;
    }
    
    public @NonNull java.util.List<android.security.securityconfhistory.SecurityConfigurationEvent> listEvents() {
        try {
            List<SecurityConfigurationEvent> result = new ArrayList<SecurityConfigurationEvent>();
            for (SecurityConfigurationEventDescription description : mService.listEvents()) {
                SecurityConfigurationEvent ev = SecurityConfigurationEventFactory.makeSecurityConfigurationEvent(description);
                if (ev != null)
                    result.add(ev);
            }
            return result;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
    
    public @NonNull GrantRuntimePermissionEvent test() {
        return new GrantRuntimePermissionEvent("");
    }
}
