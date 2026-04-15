package com.example.b01.controller;

import com.example.b01.dto.PageRequestDTO;
import com.example.b01.dto.PageResponseDTO;
import com.example.b01.dto.ReplyDTO;
import com.example.b01.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    //    public ResponseEntity<Map<String,Long>> register(@RequestBody ReplyDTO replyDTO){
//        // ReplyDTO를 JSON으로 받아서 @RepuestBody를 이용해서 자바 객체로 변환
//        log.info(replyDTO);
//        Map<String, Long> resultMap = Map.of("rno", 111L);
//        return ResponseEntity.ok(resultMap);
//    }

    private final ReplyService replyService;


    @Operation(description = "GET 방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno,
                                             PageRequestDTO pageRequestDTO){
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno,
                pageRequestDTO);
        return responseDTO;
    }


    @Operation(summary = "POST 방식으로 댓글 등록")
    // /에 있는 JSON 데이터만 받겟다는의미
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO
                                      , BindingResult bindingResult) throws BindException {
        log.info(replyDTO);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        Map<String,Long> resultMap = new HashMap<>();
        Long rno = replyService.register(replyDTO);
        resultMap.put("rno",rno);
        return resultMap;
    }

    @Operation(description = "Get 방식으로 특정 댓글 조회")
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO (@PathVariable("rno") Long rno){ //url 경로에 있는 값을 변수로 바인딩
        ReplyDTO replyDTO = replyService.read(rno);
        return replyDTO;
    }

    @Operation(description = "DELETE 방식으로 댓글 처리")
    @DeleteMapping("/{rno}")
    public Map<String,Long> remove(@PathVariable("rno") Long rno){
        replyService.remove(rno);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno",rno);
        return resultMap;
    }

    @Operation(description = "Put 방식으로 댓글 처리")
    @PutMapping(value = "/{rno}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> modify(@PathVariable("rno") Long rno
                                    , @RequestBody ReplyDTO replyDTO){
        replyDTO.setRno(rno); // 번호를 일치 시킴
        replyService.modify(replyDTO);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno",rno);
        return resultMap;
    }
}
