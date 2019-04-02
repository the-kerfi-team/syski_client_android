package uk.co.syski.client.android.FCM;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //Check Message Payload
        if(remoteMessage.getData().size() > 0){
            //handle the data message here
        }

        //get title and the body
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        // build notification
        NotifManager.getInstance(this).displayNotification(title,body);
    }

}
