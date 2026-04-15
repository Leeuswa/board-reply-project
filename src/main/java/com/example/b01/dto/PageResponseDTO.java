package com.example.b01.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {

    // 현재 페이지 번호
    private int page;

    // 한 페이지에 보여줄 데이터 개수
    private int size;

    // 전체 데이터 개수 (총 게시글 수 등)
    private int total;

    // 화면에 표시될 시작 페이지 번호 (ex: 1, 11, 21 ...)
    private int start;

    // 화면에 표시될 끝 페이지 번호 (ex: 10, 20, 30 ...)
    private int end;

    // 이전 페이지 그룹 존재 여부
    private boolean prev;

    // 다음 페이지 그룹 존재 여부
    private boolean next;

    // 실제 화면에 보여줄 데이터 리스트 (제네릭 타입)
    private List<E> dtoList;

    // 빌더 패턴 사용 (메서드 이름을 withAll로 지정)
    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total){

        // 전체 데이터가 없으면 계산할 필요 없으므로 종료
        if(total <= 0){
            return;
        }

        // 요청에서 받은 현재 페이지 번호
        this.page = pageRequestDTO.getPage();

        // 요청에서 받은 페이지당 데이터 개수
        this.size = pageRequestDTO.getSize();

        // 전체 데이터 개수 저장
        this.total = total;

        // 화면에 뿌릴 데이터 리스트
        this.dtoList = dtoList;

        // 끝 페이지 계산 (페이지를 10개 단위로 끊음)
        // 예:
        // page=1 → end=10
        // page=13 → end=20
        this.end = (int)(Math.ceil(this.page / 10.0)) * 10;

        // 시작 페이지 계산
        // 예: end=10 → start=1
        //     end=20 → start=11
        this.start = this.end - 9;

        // 실제 마지막 페이지 번호 계산
        // 예: total=95, size=10 → last=10
        int last = (int)(Math.ceil((total / (double) size)));

        // end가 실제 마지막 페이지보다 크면 last로 보정
        this.end = end > last ? last : end;

        // 이전 페이지 그룹 존재 여부
        // start가 1보다 크면 이전 페이지 있음
        this.prev = this.start > 1;

        // 다음 페이지 그룹 존재 여부
        // 전체 데이터가 현재 end * size보다 크면 다음 페이지 존재
        this.next = total > this.end * this.size;
    }
}