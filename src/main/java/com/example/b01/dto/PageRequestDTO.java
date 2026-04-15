package com.example.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    // 기본 페이지 번호 = 1
    // @Builder 사용 시 값이 안 들어오면 기본값 1로 설정
    @Builder.Default
    private int page = 1;

    // 한 페이지에 보여줄 데이터 개수 (기본 10개)
    @Builder.Default
    private int size = 10;

    // 검색 조건 타입
    // 예:
    // t = title
    // c = content
    // w = writer
    // tc = title + content
    // twc = title + writer + content
    private String type;

    // 검색어 (사용자가 입력한 키워드)
    private String keyword;

    // type을 쪼개서 배열로 반환하는 메서드
    public String[] getTypes(){

        // type이 없으면 null 반환 (검색 조건 없음)
        if(type == null || type.isEmpty()){
            return null;
        }

        // 문자열을 한 글자씩 분리해서 배열로 반환
        // 예: "tc" → ["t", "c"]
        return type.split("");
    }

    // Pageable 객체 생성 (페이징 + 정렬)
    public Pageable getPageable(String... props){

        // page-1 하는 이유:
        // Pageable은 0부터 시작 (0페이지 = 첫 페이지)
        // 사용자는 보통 1부터 입력하므로 -1 해줌

        // size: 페이지당 데이터 개수

        // props: 정렬 기준 필드명 (예: "bno", "regDate")
        // → 가변 인자라 여러 개도 가능

        // Sort.by(props).descending():
        // → 지정한 필드를 기준으로 내림차순 정렬

        return PageRequest.of(
                this.page - 1,
                this.size,
                Sort.by(props).descending()
        );
    }

    // 페이지 이동 시 사용할 쿼리 문자열을 저장하는 변수
// 예: page=1&size=10&type=t&keyword=java
    private String link;

    // 쿼리 문자열 생성 및 반환 메서드
    public String getLink() {

        // link가 이미 만들어져 있으면 재사용 (중복 생성 방지)
        if (link == null) {

            // 문자열을 효율적으로 만들기 위한 StringBuilder
            StringBuilder builder = new StringBuilder();

            // 현재 페이지 정보 추가
            builder.append("page=" + this.page);

            // 페이지당 데이터 개수 추가
            builder.append("&size=" + this.size);

            // 검색 타입이 존재할 경우 추가
            // 예: type=t 또는 tc 등
            if (type != null && type.length() > 0) {
                builder.append("&type=" + type);
            }

            // 검색어가 존재할 경우 추가
            if (keyword != null) {
                try {
                    // URL에 한글이나 특수문자가 들어갈 수 있으므로 인코딩 처리
                    // 예: "자바" → "%EC%9E%90%EB%B0%94"
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    // UTF-8은 거의 항상 지원되므로 보통 여기로 안 들어옴
                    // 예외 발생 시 특별한 처리 없이 무시
                }
            }

            // 완성된 문자열을 link 변수에 저장 (캐싱)
            link = builder.toString();
        }

        // 최종 쿼리 문자열 반환
        return link;
    }
}
