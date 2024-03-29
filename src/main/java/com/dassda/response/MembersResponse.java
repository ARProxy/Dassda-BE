package com.dassda.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MembersResponse {
    private Long id;
    private String nickname;
    private String profileUrl;
    private LocalDateTime regDate;

    public MembersResponse(Long id, String nickname, String profileUrl, LocalDateTime regDate) {
        this.id = id;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.regDate = regDate;
    }
}
