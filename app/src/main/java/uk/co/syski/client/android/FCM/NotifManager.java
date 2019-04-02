package uk.co.syski.client.android.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.view.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotifManager {

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

    public void displayNotification(String title, String body) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext, "syskinotif")
                        .setSmallIcon(R.drawable.syski_sys_icon)
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
