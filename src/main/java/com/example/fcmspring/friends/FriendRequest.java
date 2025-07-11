package com.example.fcmspring.friends;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity // 이 클래스가 JPA 엔티티임을 선언합니다. 데이터베이스의 테이블과 매핑됩니다.
@Table(name = "friend_requests") // 매핑될 테이블의 이름을 'friend_requests'로 지정합니다.
@Data // Lombok: @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor를 자동으로 생성합니다.
@NoArgsConstructor // Lombok: 파라미터가 없는 기본 생성자를 생성합니다. JPA는 엔티티 객체 생성 시 기본 생성자를 필요로 합니다.
public class FriendRequest {

    @Id // 이 필드가 테이블의 기본 키(Primary Key)임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스(예: MySQL의 AUTO_INCREMENT)에 위임합니다.
    private Long id;

    // 친구 요청을 보낸 사용자의 ID
    @Column(nullable = false) // 데이터베이스 컬럼 설정: null 값을 허용하지 않습니다[1].
    private Long requesterId;

    // 친구 요청을 받은 사용자의 ID
    @Column(nullable = false) // 데이터베이스 컬럼 설정: null 값을 허용하지 않습니다[1].
    private Long recipientId;

    // 친구 요청의 현재 상태 (PENDING, ACCEPTED, REJECTED)
    @Enumerated(EnumType.STRING) // Enum 타입을 데이터베이스에 저장할 때, Enum의 이름(문자열)으로 저장하도록 설정합니다.
    @Column(nullable = false)
    private FriendRequestStatus status;

    // 친구 요청이 생성된 시간
    // JPA가 자동으로 Java의 LocalDateTime 객체를 데이터베이스의 TIMESTAMP 타입과 매핑해줍니다[2][5].
    @Column(nullable = false)
    private LocalDateTime requestedAt;

    /**
     * 요청자와 수신자 ID를 받아 FriendRequest 객체를 생성하는 생성자입니다.
     * 객체가 생성될 때 상태와 요청 시간을 자동으로 초기화합니다.
     *
     * @param requesterId 요청자 ID
     * @param recipientId 수신자 ID
     */
    public FriendRequest(Long requesterId, Long recipientId) {
        this.requesterId = requesterId;
        this.recipientId = recipientId;
        this.status = FriendRequestStatus.PENDING; // 상태를 'PENDING'으로 기본 설정
        this.requestedAt = LocalDateTime.now();    // 현재 시간을 요청 시간으로 설정[5]
    }
}
