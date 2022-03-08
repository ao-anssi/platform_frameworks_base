package android.security.securityconfhistory;

import android.os.Parcel;
import android.os.Parcelable;
import android.annotation.NonNull;


public final class SecurityConfigurationEventDescription implements Parcelable {

    private @NonNull String permission_name;
    private @NonNull String package_name;
    private int user_id;
    private long time;
    private @NonNull String reason;
    private int event_type;
    
    public static final int GRANT_PERMISSION_EVENT = 1;
    public static final int REVOKE_PERMISSION_EVENT = 2;
    public static final int UNKNOWN_EVENT = -1;
    
    public static final @NonNull Parcelable.Creator<SecurityConfigurationEventDescription> CREATOR = new Parcelable.Creator<SecurityConfigurationEventDescription>() {
        public SecurityConfigurationEventDescription createFromParcel(Parcel in) {
            return new SecurityConfigurationEventDescription(in);
        }

        public SecurityConfigurationEventDescription[] newArray(int size) {
            return new SecurityConfigurationEventDescription[size];
        }
    };
/*
    public SecurityConfigurationEventDescription() {
    }
*/
    private SecurityConfigurationEventDescription(Parcel in) {
        readFromParcel(in);
    }
    
    public SecurityConfigurationEventDescription(@NonNull String in_permission_name, 
                                @NonNull String in_package_name,
                                int in_user_id,
                                long in_time,
                                @NonNull String in_reason,
                                int in_event_type) {
        permission_name = in_permission_name;
        package_name = in_package_name;
        user_id = in_user_id;
        time = in_time;
        reason = in_reason;
        event_type = in_event_type;            
    }

    public void writeToParcel(@NonNull Parcel out, int flags) {
        out.writeString(permission_name);
        out.writeString(package_name);
        out.writeInt(user_id);
        out.writeLong(time);
        out.writeString(reason);
        out.writeInt(event_type);
    }

    public void readFromParcel(@NonNull Parcel in) {
        permission_name = in.readString();
        package_name = in.readString();
        user_id = in.readInt();
        time = in.readLong();
        reason = in.readString();
        event_type = in.readInt();    
    }

    public int describeContents() {
        return 0;
    }

    public @NonNull String  getPermissionName() {
        return permission_name;
    }
    
    public @NonNull String getPackageName() {
        return package_name;
    }
    
    public int getUserId() {
        return user_id;
    }
    
    public long getTime() {
        return time;
    }
    
    public @NonNull String getReason() {
        return reason;
    }
    
    public int getEventType() {
        return event_type;
    }
}
