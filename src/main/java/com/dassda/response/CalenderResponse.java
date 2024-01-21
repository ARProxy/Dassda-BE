package com.dassda.response;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.entity.Sticker;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "달력 조회 응답 데이터")
public class CalenderResponse {

    private Long id;
    private List<Member> members;
    private Board board;
    private Sticker sticker;
    private String contents;
    private Long likesCount;
    private int commentCount;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private boolean backUp;

}
