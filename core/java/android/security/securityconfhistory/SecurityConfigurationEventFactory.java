package android.security.securityconfhistory;

import android.annotation.NonNull;
import android.annotation.Nullable;

/**
* @hide
*/
public class SecurityConfigurationEventFactory {
        
        public static @Nullable SecurityConfigurationEvent makeSecurityConfigurationEvent(@NonNull SecurityConfigurationEventDescription eventDescription) {
            
            SecurityConfigurationEvent event = null;
            
            switch (eventDescription.getEventType())  {
                case SecurityConfigurationEventDescription.GRANT_PERMISSION_EVENT:
                    event = new GrantRuntimePermissionEvent(eventDescription.getPermissionName(), 
                                                            eventDescription.getPackageName(), 
                                                            eventDescription.getUserId(),
                                                            eventDescription.getTime());
                break;                
                case SecurityConfigurationEventDescription.REVOKE_PERMISSION_EVENT:
                    event = new RevokeRuntimePermissionEvent(eventDescription.getPermissionName(), 
                                                            eventDescription.getPackageName(), 
                                                            eventDescription.getUserId(),
                                                            eventDescription.getReason(),
                                                            eventDescription.getTime());
                break;                
                default :
                    event = null;
                break;
            }
            
            return event;
        }
        
}
