package com.example.fcmspring.friends;

import com.example.fcmspring.notification.NotificationService;
import com.example.fcmspring.users.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 친구 요청과 관련된 핵심 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 컨트롤러로부터 요청을 받아 DB 저장 및 알림 전송을 총괄합니다.
 */
@Service
public class FriendRequestService {

    // 필요한 의존성들을 주입받습니다.
    private final FriendRequestRepository friendRequestRepository;
    private final NotificationService notificationService;
    private final UserService userService;

    public FriendRequestService(FriendRequestRepository friendRequestRepository,
                                NotificationService notificationService,
                                UserService userService) {
        this.friendRequestRepository = friendRequestRepository;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    /**
     * 친구 요청을 보내는 전체 프로세스를 처리합니다.
     *
     * @param requesterId 요청을 보낸 사용자의 ID
     * @param recipientId 요청을 받을 사용자의 ID
     * @return DB에 저장된 새로운 FriendRequest 객체
     */
    // @Transactional: 이 메소드 내에서 수행되는 모든 DB 작업은 하나의 트랜잭션으로 묶입니다.
    // 만약 중간에 오류가 발생하면, 모든 작업이 롤백(취소)되어 데이터 일관성을 보장합니다.
    @Transactional
    public FriendRequest sendFriendRequest(Long requesterId, Long recipientId) {
        // 1. 유효성 검증 (비즈니스 로직)
        // 자기 자신에게 친구 요청을 보낼 수 없습니다.
        if (requesterId.equals(recipientId)) {
            throw new IllegalArgumentException("자기 자신에게 친구 요청을 보낼 수 없습니다.");
        }
        // 요청을 받을 사용자가 실제로 존재하는지 확인합니다.
        if (userService.findById(recipientId).isEmpty()) {
            throw new IllegalArgumentException("요청을 받을 사용자가 존재하지 않습니다.");
        }
        // TODO: 실제 서비스에서는 '이미 친구 관계인지', '이미 보낸 요청이 있는지' 등을 추가로 검증해야 합니다.

        // 2. DB에 친구 요청 저장
        // 새로운 FriendRequest 엔티티를 생성합니다.
        FriendRequest friendRequest = new FriendRequest(requesterId, recipientId);
        // JpaRepository의 save 메소드를 호출하여 DB에 저장합니다.
        FriendRequest savedRequest = friendRequestRepository.save(friendRequest);
        System.out.println("친구 요청이 데이터베이스에 저장되었습니다. ID: " + savedRequest.getId());

        // 3. FCM을 통해 알림 전송 (비동기 처리 권장)
        // NotificationService에 알림 전송을 위임합니다.
        // *실제 서비스에서는 이 부분을 @Async 어노테이션 등을 사용하여 비동기적으로 처리하는 것이 좋습니다.
        //  그래야 알림 전송이 지연되더라도 사용자에게 응답이 늦게 가는 것을 막을 수 있습니다.
        notificationService.sendFriendRequestNotification(recipientId, requesterId);

        // 4. 처리 결과를 반환합니다.
        return savedRequest;
    }
}
