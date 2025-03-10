package project.app.flutter_spring_todoapp.fcm.cofig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {

        FirebaseApp firebaseApp = null;
        boolean hasBeenInitialized = false;
        for(FirebaseApp app : FirebaseApp.getApps()){
            if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                hasBeenInitialized = true;
                firebaseApp = app;
            }
        }
        if(!hasBeenInitialized){
            firebaseApp = getFirebaseApp();
        }
        return firebaseApp;
    }

    private static FirebaseApp getFirebaseApp() throws IOException {
        FirebaseApp firebaseApp;
        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/todoapp-3faa2-firebase-adminsdk-fbsvc-cc78a801d0.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        firebaseApp = FirebaseApp.initializeApp(options);
        return firebaseApp;
    }

    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        return FirebaseAuth.getInstance(firebaseApp());
    }
}
