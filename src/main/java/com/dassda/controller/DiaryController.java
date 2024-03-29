package com.dassda.controller;

import com.dassda.request.DiaryRequest;
import com.dassda.request.DiaryUpdateRequest;
import com.dassda.response.DiaryDetailResponse;
import com.dassda.response.LikesResponse;
import com.dassda.service.DiaryService;
import com.dassda.service.LikesService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
@CrossOrigin
public class DiaryController {

    private final DiaryService diaryService;
    private final LikesService likesService;

    @Operation(summary = "일기 작성 API", description = "일기 내용, 기분, 제목, 사진들")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> addDiary(
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @ModelAttribute DiaryRequest diaryRequest
    ) throws Exception {
        diaryService.addDiary(diaryRequest);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "일기 일별 조회 API", description = "일기장, 일기 아이디")
    @GetMapping("/{diaryId}/related-list")
    public ResponseEntity<List<DiaryDetailResponse>> getDetail(
            @PathVariable("diaryId") Long diaryId) {
        List<DiaryDetailResponse> diaryDetailResponse = diaryService.getDiary(diaryId);
        return ResponseEntity.ok(diaryDetailResponse);
    }

    @Operation(summary = "일기 상제 조회 API", description = "일기 제목, 좋아요 수, 댓글 수, 사진, 멤버 정보들")
    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryDetailResponse> getDiary(
            @PathVariable("diaryId") Long diaryId
    ) {
        DiaryDetailResponse diaryDetailRespons = diaryService.getDiaries(diaryId);
        return ResponseEntity.ok(diaryDetailRespons);
    }
    @Operation(summary = "일기 수정 API", description = "일기 수정할 내용을 보내주셈")
    @PutMapping(value = "{diaryId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updateDiary(
            @PathVariable(value = "diaryId") Long diaryId,
            @ModelAttribute DiaryUpdateRequest diaryUpdateRequest,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
            ) throws Exception {
        diaryService.updateDiary(diaryId, diaryUpdateRequest);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "일기 삭제 API", description = "일기 번호 보내주면 삭제")
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable(value = "diaryId") Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "좋아요 토글 API", description = "일기 번호 보내주면 토글 됌")
    @PostMapping("/{diaryId}/likes")
    public ResponseEntity<?> toggleLikes(@PathVariable(value = "diaryId") Long diaryId) {
        likesService.toggleLike(diaryId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "좋아요 조회 API", description = "좋아요 닉네임 사람 좋아요 수 보내기")
    @GetMapping("/{diaryId}/likes")
    public ResponseEntity<?> getLikes(@PathVariable(value = "diaryId") Long diaryId) {
        List<LikesResponse> likesResponse = likesService.getLikesForDiary(diaryId);
        return ResponseEntity.ok(likesResponse);
    }
}
