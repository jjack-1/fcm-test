package com.example.fcmspring._core.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * 이 클래스는 Spring Boot 애플리케이션에서 Firebase Admin SDK를 초기화하고
 * FirebaseMessaging 인스턴스를 Bean으로 등록하는 설정을 담당합니다.
 */
@Configuration // 이 클래스가 Spring의 설정(Configuration) 클래스임을 나타냅니다.
public class FirebaseConfig {

    /**
     * FirebaseApp을 초기화하고 Bean으로 등록하는 메소드.
     *
     * @return 초기화된 FirebaseApp 인스턴스
     * @throws IOException service-account.json 파일 로드 중 발생할 수 있는 예외
     */
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // 1. 클래스패스에서 service-account.json 파일에 대한 리소스를 가져옵니다.
        //    'src/main/resources'는 클래스패스의 루트이므로 파일 이름만 명시하면 됩니다.
        ClassPathResource resource = new ClassPathResource("env/service-account.json");

        // 2. 리소스로부터 InputStream을 얻어 GoogleCredentials 객체를 생성합니다.
        //    이 인증 정보를 통해 서버가 Firebase 프로젝트에 접근할 권한을 얻습니다.
        try (InputStream serviceAccountStream = resource.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            // 3. 이미 FirebaseApp이 초기화되었는지 확인하고, 초기화되지 않았을 때만 초기화를 수행합니다.
            //    이것은 중복 초기화를 방지하여 애플리케이션의 안정성을 높입니다.
            if (FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.initializeApp(options);
            } else {
                return FirebaseApp.getInstance();
            }
        }
    }

    /**
     * FirebaseMessaging 인스턴스를 Bean으로 등록하는 메소드.
     * 이 Bean은 다른 서비스(예: NotificationService)에서 주입받아 FCM 메시지 전송에 사용됩니다.
     *
     * @param firebaseApp 위에서 초기화되고 등록된 FirebaseApp Bean을 주입받습니다.
     * @return FirebaseMessaging 인스턴스
     */
    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}