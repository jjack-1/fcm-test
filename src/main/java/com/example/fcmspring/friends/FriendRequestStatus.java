package com.example.fcmspring.friends;

/**
 * 친구 요청의 상태를 정의하는 열거형(Enum) 클래스입니다.
 * PENDING: 요청이 보내졌으나 아직 수락/거절되지 않은 대기 상태
 * ACCEPTED: 요청이 수락된 상태
 * REJECTED: 요청이 거절된 상태
 */
public enum FriendRequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}