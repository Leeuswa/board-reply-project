package com.example.b01.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="Reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")  //연관 관계를 지정하는 경우에  제와하도록 해야함
//field의 값을 자동으로 String으로 변환해주는 에노테이션 board를 ToString을 적용하게되면 객체가 String으로 적용되기때문에 제외

public class Reply extends  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY) // fetch는 사용할떄 조회함.
//    @JoinColumn(name ="bno")
    private Board board;

    @Column
    private String replyText;

    @Column
    private String replyer;


    public void changeText(String text){
        this.replyText = text;
    }

}
