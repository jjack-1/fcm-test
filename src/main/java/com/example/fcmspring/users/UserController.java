package com.example.fcmspring.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/api/users/{username}/fcm-token")
    public ResponseEntity<Void> updateUserFcmToken(
            @PathVariable String username,
            @RequestBody Map<String, String> payload) {
        System.out.println(username);
        String fcmToken = payload.get("fcmToken");
        userService.updateFcmToken(username, fcmToken);
        return ResponseEntity.ok().build();
    }
}
