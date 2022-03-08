package android.security.securityconfhistory;

/**
 * @hide
 */
interface ISecurityConfigurationHistoryService {
    void startRecording();
    void runtimePermissionGranted(String packageName, String permName, int userId);
    void runtimePermissionRevoked(String packageName, String permName, int userId, String reason);
}
