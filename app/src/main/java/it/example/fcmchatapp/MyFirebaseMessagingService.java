// MyFirebaseMessagingService.java
package it.example.fcmchatapp;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("FCM", "Token aggiornato: " + token);

        // TODO: inviare il token al server Spring Boot tramite una chiamata HTTP
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("FCM", "Messaggio ricevuto: " + remoteMessage.getData());

        if (remoteMessage.getNotification() != null) {
            Log.d("FCM", "Notifica: " + remoteMessage.getNotification().getBody());
            // TODO: mostrare notifica con NotificationManager
        }
    }
}
