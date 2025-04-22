package it.example.fcmchatapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Richiesta permesso runtime per notifiche (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            } else {
                fetchAndSendFcmToken();
            }
        } else {
            fetchAndSendFcmToken();
        }
    }

    private void fetchAndSendFcmToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM", "Token fetch fallita", task.getException());
                        return;
                    }

                    String token = task.getResult();
                    Log.d("FCM", "Token ottenuto: " + token);

                    OkHttpClient client = new OkHttpClient();

                    RequestBody body = new FormBody.Builder()
                            .add("deviceToken", token)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://192.168.1.100:8080/api/fcm/save-token") // Sostituire con IP reale del server
                            .header("Authorization", "Bearer " + "<JWT_TOKEN>") // Sostituire con JWT reale
                            .post(body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("FCM", "Errore invio token", e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) {
                            Log.d("FCM", "Token inviato con successo");
                        }
                    });
                });
    }
}
