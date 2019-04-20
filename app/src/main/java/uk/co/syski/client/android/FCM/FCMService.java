package uk.co.syski.client.android.FCM;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Service for handling Firebase Cloud Messaging (Notifications) For the App
 * Only functions when a message is received from Firebase, which is parsed and handled
 */
public class FCMService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "Message Received");

        //Check Message Payload
        if(remoteMessage.getData().size() > 0){
            //handle the data message here
        }

        //get title and the body
        String title = remoteMessage.getNotification().getTitle();
        Log.i(TAG, "Title: "+title);
        String body = remoteMessage.getNotification().getBody();
        Log.i(TAG, "Body: "+body);

        // build notification
        NotifManager.getInstance(this).displayNotification(title,body);
    }

}
