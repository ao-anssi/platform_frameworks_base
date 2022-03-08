package android.security.securityconfhistory;

import android.content.pm.PackageManager;
import java.util.Date;
import android.annotation.NonNull;
import android.annotation.Nullable;
import java.util.Objects;

public final class RevokeRuntimePermissionEvent implements SecurityConfigurationEvent {
    private String permission_name;
    private String package_name;
    private String reason;
    private int user_id;
    private long time;

    @Override
    public long getTime() { return time; }

    public int getType() {
        return SecurityConfigurationEvent.REVOKE_RUNTIME_CONFIGURATION_EVENT;
    }

    public RevokeRuntimePermissionEvent(@NonNull String permission, @NonNull String packagename, int userId, @NonNull String reason, long time) {
        permission_name = permission;
        package_name = packagename;
        user_id = userId;
        this.time = time;
        this.reason = reason;
    }

    public RevokeRuntimePermissionEvent(@NonNull String serialized) {
        permission_name = "empty";
        package_name = "empty";
        reason = "empty";
        user_id = -1;
        time = -1;
        initFromString(serialized);
    }

    @Override
    public @NonNull String serialize() {
        StringBuffer sb = new StringBuffer();
        sb.append("revokeRuntimePermissionEvent");
        sb.append(SEPARATOR);
        sb.append(permission_name);
        sb.append(SEPARATOR);
        sb.append(package_name);
        sb.append(SEPARATOR);
        sb.append(String.valueOf(user_id));
        sb.append(SEPARATOR);
        sb.append(String.valueOf(reason));
        sb.append(SEPARATOR);
        sb.append(String.valueOf(time));
        return sb.toString();
    }

    private void initFromString(@Nullable String inputline) {
        if (inputline == null) return;
        String[] tokens = inputline.split(SEPARATOR);
        if ((tokens == null) || (tokens.length != 6)) return;
        permission_name = tokens[1];
        package_name = tokens[2];
        user_id = Integer.valueOf(tokens[3]);
        reason = tokens[4];
        time = Long.valueOf(tokens[5]);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof RevokeRuntimePermissionEvent))
            return false;

        RevokeRuntimePermissionEvent event = (RevokeRuntimePermissionEvent) obj;
        if (!event.permission_name.equals(this.permission_name)) return false;
        if (!event.package_name.equals(this.package_name)) return false;
        if (!event.reason.equals(this.reason)) return false;
        if (!(event.user_id == this.user_id)) return false;
        if (!(event.time == this.time)) return false;

        return true;
    }

    @Override public int hashCode() {
        return Objects.hash("revokeRuntimePermissionEvent", package_name, user_id, reason, time);
    }    
    
    public @NonNull String getDescription(@NonNull PackageManager pm) {
        String description = "permission : "+permission_name+"\n";
        description+= "application : "+SecurityConfHistoryUtils.getApplicationNameFromPackageName(pm, package_name)+"\n";
        description+= "time : "+new Date(time).toString()+"\n";
        return description;
    }

    public @NonNull String getEventName() {
        return "Runtime Permission Revoked";
    }
    public @NonNull String getPermissionName() { return permission_name; }
    public @NonNull String getPackageName() {
        return package_name;
    }
    public int getUserId() { return user_id; }
    public @NonNull String getReason() {
        if (reason == null) {
            return "";
        }
        return reason; 
    }
}
