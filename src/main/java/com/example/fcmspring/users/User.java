package com.example.fcmspring.users;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users") // 데이터베이스 테이블 이름을 'users'로 지정합니다.
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 이름. 고유해야 하며, null일 수 없습니다.
    @Column(nullable = false, unique = true) // 데이터베이스 무결성을 위해 nullable=false와 unique=true 제약조건을 추가합니다[1].
    private String username;

    // FCM 기기 토큰. 이 필드가 바로 이 프로젝트의 핵심입니다.
    // 알림을 보낼 때 이 토큰을 사용합니다.
    // 사용자가 로그아웃하거나 앱을 재설치하면 변경될 수 있으므로, nullable로 설정합니다.
    @Column(name = "fcm_token")
    private String fcmToken;

    // 사용자 정보가 생성된 시간
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 사용자 정보가 마지막으로 수정된 시간
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 엔티티가 데이터베이스에 처음 저장되기 직전에 호출됩니다.
     * 생성 시간과 수정 시간을 현재 시간으로 초기화합니다.
     */
    @PrePersist
    protected void onCreate() {
        // Java의 LocalDateTime.now()를 사용하여 현재 시간을 설정합니다[5].
        // 이 시간은 UTC 기준으로 관리하는 것이 좋습니다[2][3].
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * 엔티티가 업데이트되기 직전에 호출됩니다.
     * 수정 시간을 현재 시간으로 갱신합니다.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
