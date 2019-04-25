package uk.co.syski.client.android.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.view.activity.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Used to manage Notifications for the app, allowing notifications to be displayed
 */
public class NotifManager {

    private static final String TAG = "NotifManager";
    private Context mContext;
    private static NotifManager mInstance;

    private NotifManager(Context context) {
        mContext = context;
    }

    public static synchronized NotifManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NotifManager(context);
        }
        return mInstance;
    }

    /**
     * Used to display a notification while the application is running
     * @param title the title of the notification, appearing on the notification bar
     * @param body the body (main text) of the notification
     */
    public void displayNotification(String title, String body) {
        Log.i(TAG, "Displaying Notification: " +title);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext, "syskinotif")
                        .setSmallIcon(R.drawable.logo_sys_144)
                        .setContentTitle(title)
                        .setContentText(body);

        Intent resultIntent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        if (mNotifyMgr != null) {
            mNotifyMgr.notify(1, mBuilder.build());
        }
    }

}
