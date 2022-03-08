package android.security.securityconfhistory;

import android.content.pm.PackageManager;
import android.annotation.NonNull;

public interface SecurityConfigurationEvent {
    public @NonNull String serialize();
    public int getType();
    public long getTime();
    public @NonNull String getDescription(@NonNull PackageManager pm);
    public @NonNull String getEventName();
    public @NonNull String getPackageName();
    public @NonNull String getPermissionName();
    public int getUserId();
    public @NonNull String getReason();

    final static public String SEPARATOR="##";
    final static public int GRANT_RUNTIME_CONFIGURATION_EVENT = 1;
    final static public int REVOKE_RUNTIME_CONFIGURATION_EVENT = 2;
}

