package android.security.securityconfhistory;

import android.content.pm.PackageManager;
import java.sql.Date;
import android.annotation.NonNull;
import android.annotation.Nullable;
import java.util.Objects;

public final class GrantRuntimePermissionEvent implements SecurityConfigurationEvent {
    private String permission_name;
    private String package_name;
    private int user_id;
    private long time;

    public GrantRuntimePermissionEvent(@NonNull String permission, @NonNull String packagename, int userId, long time) {
        permission_name = permission;
        package_name = packagename;
        user_id = userId;
        this.time = time;
    }

    public GrantRuntimePermissionEvent(@NonNull String serialized) {
        permission_name = "empty";
        package_name = "empty";
        user_id = -1;
        time = -1;
        initFromString(serialized);
    }
        
    @Override
    public @NonNull String serialize() {
        StringBuffer sb = new StringBuffer();
        sb.append("grantRuntimePermissionEvent");
        sb.append(SEPARATOR);
        sb.append(permission_name);
        sb.append(SEPARATOR);
        sb.append(package_name);
        sb.append(SEPARATOR);
        sb.append(String.valueOf(user_id));
        sb.append(SEPARATOR);
        sb.append(String.valueOf(time));
        return sb.toString();
    }

    private void initFromString(@Nullable String inputline) {
        if (inputline == null) return;
        String[] tokens = inputline.split(SEPARATOR);
        if ((tokens == null) || (tokens.length != 5)) return;
        permission_name = tokens[1];
        package_name = tokens[2];
        user_id = Integer.valueOf(tokens[3]);
        time = Long.valueOf(tokens[4]);
    }

    @Override
    public int getType() {
        return SecurityConfigurationEvent.GRANT_RUNTIME_CONFIGURATION_EVENT;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof GrantRuntimePermissionEvent))
            return false;

        GrantRuntimePermissionEvent event = (GrantRuntimePermissionEvent) obj;
        if (!event.permission_name.equals(this.permission_name)) return false;
        if (!event.package_name.equals(this.package_name)) return false;
        if (!(event.user_id == this.user_id)) return false;
        if (!(event.time == this.time)) return false;

        return true;
    }

    @Override public int hashCode() {
        return Objects.hash("grantRuntimePermissionEvent", package_name, user_id, time);
    }    
        
    public @NonNull String getDescription(@NonNull PackageManager pm) {
        String description = "permission : "+permission_name+"\n";
        description+= "application : "+ SecurityConfHistoryUtils.getApplicationNameFromPackageName(pm, package_name)+"\n";
        description+= "time : "+new Date(time).toString()+"\n";
        description+= "time : "+new java.util.Date(time).toString()+"\n";
        return description;
    }

    public @NonNull String getEventName() {
        return "Runtime Permission Granted";
    }
    public @NonNull String getPermissionName() { return permission_name; }
    public @NonNull String getPackageName() {
        return package_name;
    }
    public int getUserId() { return user_id; }
    public @NonNull String getReason() {return ""; }
}
