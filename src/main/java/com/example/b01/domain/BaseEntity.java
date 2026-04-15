package com.example.b01.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
// 테이블로 생성되지 않음
// 다른 엔티티가 상속받아서 필드만 사용

@EntityListeners(value = {AuditingEntityListener.class})
// 엔티티 변화 감지 → 자동으로 시간 저장

@Getter
abstract class BaseEntity {

    @CreatedDate // 엔티티 생성 시 자동 저장
    @Column(name ="regdate", updatable = false)
    // updatable = false → 한번 저장되면 수정 불가
    private LocalDateTime regDate;

    @LastModifiedDate
    // 수정될 때마다 자동으로 시간 갱신
    @Column(name = "moddate")
    private LocalDateTime modDate;
}