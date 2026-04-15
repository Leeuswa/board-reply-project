package com.example.b01.repository.search;

import com.example.b01.domain.Board;
import com.example.b01.domain.QBoard;
import com.example.b01.domain.QReply;
import com.example.b01.dto.BoardListReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{
    //구현 클래스에는  QuerydslRepositorySupport를 extends 해줘야함
    public BoardSearchImpl() {
        super(Board.class);
    }
    @Override
    public Page<Board> search1(Pageable pageable){
        //QueryDSL에서 자동 생성된 Board엔터티의 Q클래스
        QBoard board = QBoard.board;  //QueryDSL을 사용하여 Board 테이블을 조회할 준비
        JPQLQuery<Board> query = from(board);
        query.where(board.title.contains("1")); //title에 "1"을 포함하는 데이터만 필터링
        //SELECT * FROM board WHERE title LIKE '%1%';

        //페이징
        this.getQuerydsl().applyPagination(pageable,query);
        // SELECT * FROM board WHERE title LIKE '%1%' LIMIT 0, 10;

        List<Board> list = query.fetch(); //쿼리를 실행하고 결과를 리스트타입으로 리턴
        long count = query.fetchCount(); //검색된 데이터의 총 개수

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);
        if( (types != null && types.length > 0) && keyword != null ){ //검색 조건과 키워드가 있다면
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
            for(String type: types){
                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end for
            query.where(booleanBuilder);
        }//end if
        //bno > 0
        query.where(board.bno.gt(0L));
        //paging
        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list = query.fetch();
        long count = query.fetchCount();
        return new PageImpl<>(list, pageable, count);
    }


    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        JPQLQuery<Board> query = from(board);
        query.leftJoin(reply).on(reply.board.eq(board));
        query.groupBy(board);

        if( (types != null && types.length > 0) && keyword != null ){ //검색 조건과 키워드가 있다면
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
            for(String type: types){
                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end for
            query.where(booleanBuilder);
        }//end if

        //bno >0
        query.where(board.bno.gt(0L));
        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(
                Projections.bean(BoardListReplyCountDTO.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.regDate,
                        reply.count().as("replyCount")
                ));

        //paging
        this.getQuerydsl().applyPagination(pageable,query);
        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
        long count = query.fetchCount();


        //return null;
        return new PageImpl<>(dtoList, pageable, count);
    }
}
