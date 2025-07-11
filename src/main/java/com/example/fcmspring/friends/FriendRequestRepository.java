package com.example.fcmspring.friends;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * JpaRepository를 사용하지 않고 EntityManager(em)를 직접 사용하여
 * FriendRequest 엔티티에 대한 데이터베이스 작업을 수동으로 구현하는 클래스입니다.
 * 이를 통해 JPA의 내부 동작 원리를 더 깊이 이해할 수 있습니다.
 */
@Repository // 이 클래스를 Spring의 데이터 접근 계층(Repository) 빈으로 등록합니다.
public class FriendRequestRepository {

    // JPA의 핵심 인터페이스인 EntityManager를 주입받습니다.
    // EntityManager는 엔티티의 생명주기(저장, 조회, 수정, 삭제)를 관리합니다.
    private final EntityManager em;

    // 생성자를 통해 EntityManager를 주입받습니다.
    public FriendRequestRepository(EntityManager em) {
        this.em = em;
    }

    /**
     * save: 엔티티를 데이터베이스에 저장(영속화)합니다.
     *
     * @param friendRequest 저장할 FriendRequest 객체
     */
    @Transactional // 데이터베이스의 상태를 변경하는 작업(INSERT, UPDATE, DELETE)은 반드시 트랜잭션 내에서 수행되어야 합니다.
    public FriendRequest save(FriendRequest friendRequest) {
        // em.persist() 메소드는 주어진 엔티티 객체를 영속성 컨텍스트에 저장합니다.
        // 트랜잭션이 커밋될 때 실제 INSERT 쿼리가 데이터베이스로 전송됩니다.
        em.persist(friendRequest);
        return friendRequest;
    }

    /**
     * findById: 기본 키(ID)를 사용하여 엔티티를 조회합니다.
     *
     * @param id 조회할 엔티티의 ID
     * @return 조회된 FriendRequest 객체를 담은 Optional. 없으면 빈 Optional을 반환합니다.
     */
    public Optional<FriendRequest> findById(Long id) {
        // em.find(엔티티클래스.class, 기본키) 메소드는 ID를 이용해 엔티티 하나를 조회합니다.
        // 데이터베이스에서 해당 ID를 가진 데이터를 찾으면 엔티티 객체로 만들어 반환하고, 없으면 null을 반환합니다.
        FriendRequest friendRequest = em.find(FriendRequest.class, id);
        // 조회 결과가 null일 수 있으므로, Optional.ofNullable()로 감싸서 반환하는 것이 안전합니다.
        return Optional.ofNullable(friendRequest);
    }

    /**
     * findAll: 테이블의 모든 엔티티를 조회합니다.
     *
     * @return 모든 FriendRequest 객체의 리스트
     */
    public List<FriendRequest> findAll() {
        // JPQL(Java Persistence Query Language)을 사용하여 쿼리를 작성합니다.
        // "SELECT f FROM FriendRequest f"는 FriendRequest 테이블의 모든 데이터를 조회하라는 의미입니다.
        // SQL과 비슷하지만 테이블 이름 대신 엔티티 클래스 이름을 사용합니다.
        return em.createQuery("SELECT f FROM FriendRequest f", FriendRequest.class)
                .getResultList(); // 쿼리 실행 결과를 리스트 형태로 반환받습니다.
    }
}
