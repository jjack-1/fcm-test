package com.example.fcmspring.users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 사용자(User) 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service // 이 클래스를 Spring의 서비스 계층 빈으로 등록합니다.
public class UserService {

    private final UserRepository userRepository;

    // 생성자를 통해 UserRepository를 주입받습니다.
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자 ID를 이용해 사용자를 조회합니다.
     *
     * @param id 조회할 사용자의 ID
     * @return 조회된 User 객체를 담은 Optional. 없으면 빈 Optional을 반환합니다.
     */
    @Transactional(readOnly = true) // 데이터 변경이 없는 읽기 전용 트랜잭션임을 명시하여 성능을 최적화합니다.
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // 여기에 나중에 사용자 생성, 수정, 삭제 등의 메소드를 추가할 수 있습니다.
}
