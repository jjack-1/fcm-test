package com.example.fcmspring.friends;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Flutter 클라이언트로부터 들어오는 친구 요청 관련 HTTP API 요청을 처리하는 컨트롤러입니다.
 */
@RestController // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다. @ResponseBody가 포함되어 있어 메소드 반환값이 자동으로 JSON으로 변환됩니다.
@RequestMapping("/api/friend-requests") // 이 컨트롤러의 모든 API는 '/api/friend-requests' 라는 기본 경로를 가집니다.
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    // 생성자를 통해 FriendRequestService를 주입받습니다.
    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    /**
     * 클라이언트로부터 친구 요청에 필요한 데이터를 받기 위한 DTO(Data Transfer Object) 클래스입니다.
     * 엔티티를 직접 노출하지 않고 필요한 데이터만 주고받아 보안과 유연성을 높입니다.
     */
    @Data // Lombok: Getter, Setter 등을 자동 생성합니다.
    public static class FriendRequestDto {
        private Long fromId; // 요청을 받을 사용자의 ID
        private Long toId;
    }

    /**
     * 친구 요청을 보내는 API 엔드포인트입니다.
     * HTTP POST 메소드와 '/send' 경로에 매핑됩니다. (최종 URL: POST /api/friend-requests/send)
     *
     * @param requestDto 클라이언트가 보낸 JSON 요청 본문을 FriendRequestDto 객체로 변환하여 받습니다.
     * @return 처리 결과에 따른 HTTP 응답을 담은 ResponseEntity 객체
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequestDto requestDto) {
        System.out.println(requestDto.toString());
        try {
            // [중요] 실제 애플리케이션에서는 이 부분을 반드시 수정해야 합니다.
            // Spring Security 같은 인증 프레임워크를 사용하여 현재 로그인된 사용자의 ID를 가져와야 합니다.
            // 예: UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // Long requesterId = userDetails.getId();
            Long requesterId = 1L; // 여기서는 테스트를 위해 요청자 ID를 1번으로 하드코딩합니다.

            // 서비스 계층에 실제 비즈니스 로직 처리를 위임합니다.
            FriendRequest newRequest = friendRequestService.sendFriendRequest(requestDto.fromId, requestDto.toId);

            // 성공적으로 처리된 경우, HTTP 200 OK 상태와 함께 성공 메시지를 반환합니다.
            return ResponseEntity.ok("친구 요청이 성공적으로 전송되었습니다. 요청 ID: " + newRequest.getId());

        } catch (IllegalArgumentException e) {
            // 서비스에서 발생시킨 유효성 검증 예외를 처리합니다.
            // HTTP 400 Bad Request 상태와 함께 에러 메시지를 반환합니다.
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // 그 외 예상치 못한 서버 내부 오류를 처리합니다.
            System.err.println("친구 요청 처리 중 서버 오류: " + e.getMessage());
            // HTTP 500 Internal Server Error 상태와 함께 일반적인 에러 메시지를 반환합니다.
            return ResponseEntity.internalServerError().body("친구 요청 처리 중 오류가 발생했습니다.");
        }
    }
}
