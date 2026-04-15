package com.example.b01.service;

import com.example.b01.domain.Board;
import com.example.b01.dto.BoardDTO;
import com.example.b01.dto.BoardListReplyCountDTO;
import com.example.b01.dto.PageRequestDTO;
import com.example.b01.dto.PageResponseDTO;
import com.example.b01.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// 서비스 계층을 나타내는 클래스 (비즈니스 로직 담당)
@Service

// log.info() 등 로그 출력 가능
@Log4j2

// final 필드들을 자동으로 생성자 주입 (의존성 주입)
@RequiredArgsConstructor

@Transactional
public class BoardServiceImpl implements BoardService{

    // DTO ↔ Entity 변환을 위한 객체 (자동 매핑)
    private final ModelMapper modelMapper;

    // DB 접근을 위한 Repository (JPA가 구현체 자동 생성)
    private final BoardRepository boardRepository;

    // 게시글 등록 메서드
    @Override
    public Long register(BoardDTO boardDTO) {

        // DTO → Entity 변환
        // 이유: DB 저장은 Entity 객체로 해야 함
        Board board = modelMapper.map(boardDTO, Board.class);

        // DB에 저장 (insert 수행)
        // save() 실행 후 저장된 Entity 반환
        // → getBno()로 생성된 기본키(PK) 추출
        Long bno = boardRepository.save(board).getBno();

        // 생성된 게시글 번호 반환
        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        BoardDTO boardDTO = modelMapper.map(board,BoardDTO.class);
        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        Board board = result.orElseThrow();
        board.change(boardDTO.getTitle(), boardDTO.getContent());
        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno){
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());
        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();

    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types,keyword,pageable);
        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }
}