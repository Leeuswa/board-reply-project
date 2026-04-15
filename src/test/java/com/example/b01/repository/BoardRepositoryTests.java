package com.example.b01.repository;

import com.example.b01.domain.Board;
import com.example.b01.dto.BoardListReplyCountDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert(){

        IntStream.rangeClosed(1,100).forEach(i ->{
            Board board = Board.builder()
                    .title("title...." + i)
                    .content("content..." + i)
                    .writer("user..." + (i % 10))
                    .build();

            Board result  =boardRepository.save(board);
            log.info("BNO: " + result.getBno());
        });
    }

    @Test
    public void testSelect(){

        Long bno = 100L; // 조회할 게시글의 PK 값

        Optional<Board> result = boardRepository.findById(bno);
        // findById():
        // PK로 엔티티를 조회
        // 반환 타입은 Optional<Board>
        // -> 데이터가 있을 수도 있고 없을 수도 있기 때문

        Board board = result.orElseThrow();
        // Optional 처리
        // 값이 있으면 Board 객체 반환
        // 값이 없으면 NoSuchElementException 발생

        log.info(board);
        // 조회된 Board 객체 전체 출력
    }

    @Test
    public void testUpdate(){

        Long bno = 100L; // 수정할 게시글의 PK 값

        Optional<Board> result = boardRepository.findById(bno);
        // findById():
        // PK로 데이터 조회 (Optional로 반환)

        Board board = result.orElseThrow();
        // 값이 있으면 Board 객체 반환
        // 없으면 예외 발생

        board.change("update ,,, title 100","update content 100");
        // 엔티티 내부 메서드를 통해 값 변경
        // setter 대신 change() 같은 메서드를 사용하는 방식 (캡슐화)

        boardRepository.save(board);
        // save():
        // PK가 존재하므로 insert가 아닌 update 실행
    }


    @Test
    public void testDelete(){

        Long bno = 1L; // 삭제할 게시글의 PK 값

        boardRepository.deleteById(bno);
        // deleteById():
        // 해당 PK 값을 가진 데이터를 DB에서 삭제
    }


    @Test
    public void testPaging(){

        // Pageable: 페이징(페이지 번호, 개수, 정렬)을 설정하는 객체
        Pageable pageable =
                PageRequest.of(
                        0, // 페이지 번호 (0부터 시작 → 0 = 1페이지)
                        10, // 한 페이지당 데이터 개수 (size)
                        Sort.by("bno").descending() // bno 기준 내림차순 정렬
                );

        // findAll(Pageable)
        // → 페이징 + 정렬이 적용된 데이터를 조회
        // → 반환 타입은 List가 아니라 Page<T> (페이징 정보 포함)
        Page<Board> result = boardRepository.findAll(pageable);

        // 전체 데이터 개수 (DB에 존재하는 총 데이터 수)
        log.info("total count: " + result.getTotalElements());

        // 전체 페이지 수 (전체 데이터 / size 계산 결과)
        log.info("total pages: " + result.getTotalPages());

        // 현재 페이지 번호 (0부터 시작)
        log.info(" page number: " + result.getNumber());

        // 한 페이지당 데이터 개수 (size 값)
        log.info("page size: " + result.getSize());

        // 현재 페이지에 조회된 데이터 리스트 추출
        List<Board> todoList = result.getContent();

        // 조회된 데이터 출력
        todoList.forEach(board -> log.info(board));
    }

    @Test
    public void testSearch1(){

        Pageable pageable = PageRequest.of(1,10,Sort.by("bno").descending());
        boardRepository.search1(pageable);
    }

//    @Test
//    public void testSearchAll(){
//        String[] types = {"t","c","w"};
//        String keyword = "1";
//        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
//        Page<Board> result = boardRepository.searchAll(types,keyword,pageable);
//
//    }

    @Test
    public void testSearchAll(){

        // 검색 대상 필드 지정
        // t: title, c: content, w: writer
        // → 세 필드 모두에서 검색 수행 (OR 조건)
        String[] types = {"t","c","w"};

        // 검색 키워드
        String keyword = "1";

        // 페이징 설정
        // 0페이지(=1페이지), 10개씩, bno 기준 내림차순 정렬
        Pageable pageable =
                PageRequest.of(
                        0,
                        10,
                        Sort.by("bno").descending()
                );

        // Querydsl 기반 검색 + 페이징 실행
        Page<Board> result =
                boardRepository.searchAll(types, keyword, pageable);


        // 전체 페이지 수 출력
        // (전체 데이터 개수 / 페이지당 개수)
        log.info(result.getTotalPages());

        // 한 페이지당 데이터 개수 (size)
        log.info(result.getSize());

        // 현재 페이지 번호 (0부터 시작)
        log.info(result.getNumber());

        // 이전/다음 페이지 존재 여부
        // hasPrevious(): 이전 페이지 있는지
        // hasNext(): 다음 페이지 있는지
        log.info(result.hasPrevious() + " : " + result.hasNext());

        // 현재 페이지에 조회된 데이터 출력
        result.getContent().forEach(board -> log.info(board));
    }


    @Test
    public void testSearchReplyCount(){
        String[] types = {"t","c","w"};
                String keyword = "1";
        Pageable pageable =PageRequest.of(0,10,Sort.by("bno").descending());
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types,keyword,pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious() + ": " + result.hasNext());
        result.getContent().forEach(board -> log.info(board));
    }


}
