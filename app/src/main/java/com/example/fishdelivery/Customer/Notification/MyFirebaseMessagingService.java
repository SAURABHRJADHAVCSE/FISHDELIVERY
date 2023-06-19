package com.example.fishdelivery.Customer.Notification;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Handle the incoming FCM message here
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            // Show the notification to the user or perform any other actions
            // You can customize the notification appearance by creating a NotificationCompat.Builder
            // and displaying it using the NotificationManager system service
        }
    }
}
