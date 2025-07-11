package com.example.fcmspring.users;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User 엔티티에 대한 데이터 접근(CRUD)을 담당하는 리포지토리 인터페이스입니다.
 * JpaRepository를 상속받아 기본적인 데이터베이스 작업 메소드를 자동으로 제공받습니다.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA는 메소드 이름을 분석하여 자동으로 쿼리를 생성해줍니다.
    // 예를 들어, 아래 메소드는 'username' 필드로 사용자를 찾는 JPQL 쿼리를 자동으로 만들어 실행합니다.
    // SELECT u FROM User u WHERE u.username = :username
    Optional<User> findByUsername(String username);

}