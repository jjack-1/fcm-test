package com.example.fcmspring.notification;

import com.example.fcmspring.users.User;
import com.example.fcmspring.users.UserService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * FCM 메시지 전송을 담당하는 서비스 클래스입니다.
 */
@Service
public class NotificationService {

    // FirebaseConfig에서 Bean으로 등록한 FirebaseMessaging 객체를 주입받습니다.
    private final FirebaseMessaging firebaseMessaging;
    private final UserService userService;

    public NotificationService(FirebaseMessaging firebaseMessaging, UserService userService) {
        this.firebaseMessaging = firebaseMessaging;
        this.userService = userService;
    }

    /**
     * 친구 요청 알림을 특정 사용자에게 보냅니다.
     *
     * @param recipientId 알림을 받을 사용자의 ID
     * @param requesterId 친구 요청을 보낸 사용자의 ID
     */
    public void sendFriendRequestNotification(Long recipientId, Long requesterId) {
        // 1. 알림을 받을 사용자(수신자)와 요청을 보낸 사용자(요청자)의 정보를 DB에서 조회합니다.
        Optional<User> recipientUserOpt = userService.findById(recipientId);
        Optional<User> requesterUserOpt = userService.findById(requesterId);

        // 2. 사용자 정보가 없는 경우, 로그를 남기고 함수를 종료합니다.
        if (recipientUserOpt.isEmpty() || requesterUserOpt.isEmpty()) {
            System.out.println("알림을 보낼 사용자 또는 요청자가 존재하지 않습니다.");
            return;
        }

        User recipientUser = recipientUserOpt.get();
        User requesterUser = requesterUserOpt.get();

        String recipientFcmToken = recipientUser.getFcmToken();
        // 3. 수신자의 FCM 토큰이 등록되어 있는지 확인합니다. 토큰이 없으면 알림을 보낼 수 없습니다.
        if (recipientFcmToken == null || recipientFcmToken.isEmpty()) {
            System.out.println("수신자의 FCM 토큰이 등록되지 않았습니다: " + recipientUser.getUsername());
            return;
        }

        // 4. 알림 메시지를 구성합니다. (Notification Payload)
        // 이 부분은 사용자의 디바이스에 직접 표시될 알림의 제목과 본문에 해당합니다.
        Notification notification = Notification.builder()
                .setTitle("새로운 친구 요청")
                .setBody(requesterUser.getUsername() + " 님이 친구 요청을 보냈습니다.")
                .build();

        // 5. 알림과 함께 보낼 추가 데이터를 구성합니다. (Data Payload)
        // 이 데이터는 Flutter 앱이 백그라운드나 포그라운드에서 받아서 특정 로직을 처리하는 데 사용됩니다.
        // (예: 알림 클릭 시 특정 페이지로 이동)
        Map<String, String> data = Map.of(
                "type", "FRIEND_REQUEST",
                "requesterId", String.valueOf(requesterId),
                "requesterUsername", requesterUser.getUsername(),
                "recipientId", String.valueOf(recipientId)
        );

        // 6. FCM 메시지 객체를 최종적으로 생성합니다.
        Message message = Message.builder()
                .setToken(recipientFcmToken)      // 이 토큰을 가진 기기로 메시지를 보냅니다.
                .setNotification(notification)   // 사용자에게 보여줄 알림 내용
                .putAllData(data)                // 앱에서 처리할 추가 데이터
                .build();

        // 7. 구성된 메시지를 FCM으로 전송합니다.
        try {
            String response = firebaseMessaging.send(message);
            System.out.println("Successfully sent message to FCM: " + response);
        } catch (FirebaseMessagingException e) {
            // FCM 전송 중 에러가 발생하면 로그를 남깁니다.
            System.err.println("Error sending message to FCM: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
