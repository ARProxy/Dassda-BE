package com.dassda.controller;

import com.dassda.request.BoardRequest;
import com.dassda.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("일기장 추가 테스트")

    void addBoard() throws Exception {
        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setTitle("Test");
        boardRequest.setImageNumber(1);
        boardRequest.setAppearanceType(1);

        mockMvc.perform(post("/api/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBoard() {
    }

    @Test
    void getBoard() {
    }

    @Test
    void updateBoard() {
    }

    @Test
    void getHero() {
    }

    @Test
    void getMembers() {
    }

    @Test
    void deleteShare() {
    }
}