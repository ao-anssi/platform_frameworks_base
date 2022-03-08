package android.security.securityconfhistory;

import android.security.securityconfhistory.SecurityConfigurationEventDescription;
/**
 * @hide
 */
interface ISecurityConfigurationHistoryReaderService {
    List<SecurityConfigurationEventDescription> listEvents();
} 
