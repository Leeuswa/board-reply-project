package com.example.b01.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity // 해당 클래스를 DB 테이블과 매핑
@Getter // getter 자동 생성
@Builder // 빌더 패턴 생성 (객체 생성 편하게)
@AllArgsConstructor // 모든 필드 생성자
@NoArgsConstructor // 기본 생성자 (JPA 필수)
@ToString // toString 메서드 생성
public class Board extends BaseEntity { // BaseEntity 상속 → 공통 시간 필드 사용

    @Id // 기본키(PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 방식 (MySQL, H2)
    // Oracle 사용 시 SEQUENCE 전략 사용
    private Long bno;

    @Column(length = 500, nullable = false) // 길이 제한 + NOT NULL
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    // 수정 메서드 (setter 대신 사용 → 객체 안전성 유지)
    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }
}