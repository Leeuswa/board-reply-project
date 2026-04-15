package com.example.b01.config;

// 객체 간 자동 매핑을 위한 ModelMapper
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

// 스프링 설정 관련 어노테이션
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 해당 클래스가 스프링 설정 클래스임을 의미
@Configuration
public class RootConfig {

    // ModelMapper를 스프링 Bean으로 등록
    @Bean
    public ModelMapper getMapper() {

        // ModelMapper 객체 생성
        ModelMapper modelMapper = new ModelMapper();

        // ModelMapper의 세부 동작 방식 설정
        modelMapper.getConfiguration()

                // 필드 기준 매핑 허용
                // → getter/setter 없이도 필드 이름만 같으면 매핑 가능
                .setFieldMatchingEnabled(true)

                // 접근 허용 범위 설정
                // → private 필드까지 접근해서 값 복사 가능
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)

                // 매칭 전략 설정 (LOOSE = 느슨한 매칭)
                // → 필드명이 완전히 같지 않아도 유사하면 자동 매핑
                // 예:
                // userName ↔ name
                // user_id ↔ userId
                // regDate ↔ createdDate
                // → 실무에서 DTO/Entity 이름 조금 달라도 매핑 가능하게 해줌
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        // 설정 완료된 ModelMapper를 반환 → 스프링이 Bean으로 관리
        return modelMapper;
    }
}