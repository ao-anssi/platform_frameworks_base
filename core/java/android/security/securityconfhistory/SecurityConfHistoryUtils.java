package android.security.securityconfhistory;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.annotation.NonNull;

/**
* @hide
*/
public final class SecurityConfHistoryUtils {

    public static @NonNull String getApplicationNameFromPackageName(@NonNull PackageManager pm, @NonNull String packagename) {
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo( packagename, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : packagename);

        return applicationName;
    }
    
}
