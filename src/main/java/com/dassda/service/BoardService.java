package com.dassda.service;

import com.dassda.entity.Board;
import com.dassda.entity.Member;
import com.dassda.repository.BoardRepository;
import com.dassda.repository.DiaryRepository;
import com.dassda.repository.MemberRepository;
import com.dassda.repository.ShareRepository;
import com.dassda.request.BoardDto;
import com.dassda.response.BoardResponse;
import com.dassda.response.HeroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final DiaryRepository diaryRepository;
    private final ShareRepository shareRepository;

    private Member email() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("다시 로그인 해주세요."));
    }
    public void addBoard(BoardDto boardDto) {
        Optional<Member> member = memberRepository.findByEmail(email().getEmail());
        //Dto에 있는 데이터 말고 다른 값을 클라이언트에서 추가적으로 요청했을 때
        //스프링에서 객체로 바인딩할 때 자동으로 무시함
        if(boardDto.getBoardTitle().length() > 10) {
            throw new IllegalArgumentException("제목은 10자를 넘을 수 없다.");
        }

        if(member.isPresent()) {
            Board board = new Board();
            board.setMember(member.get());
            board.setTitle(boardDto.getBoardTitle());
            board.setImageNumber(boardDto.getImageNumber());
            board.setAppearanceType(boardDto.getAppearanceType());
            board.setRegDate(LocalDateTime.now());
            board.setShared(false);
            board.setBackUp(false);
            boardRepository.save(board);
        } else {
            throw new UsernameNotFoundException("다시 로그인 해주세요.");
        }
    }
    public void deleteBoard(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);

        if(board.isPresent()) {
            boardRepository.delete(board.get());
        } else {
            throw new IllegalStateException("삭제된 일기장입니다.");
        }
    }
    public List<BoardResponse> getBoard() {
        Optional<Member> member = memberRepository.findByEmail(email().getEmail());

        if(member.isPresent()) {
            Long memberId = member.get().getId();
            List<Board> boards = boardRepository.findByMemberId(memberId);
            return boards.stream()
                    .map(this::convertToBoard)
                    .collect(Collectors.toList());
        } else {
            throw new UsernameNotFoundException("다시 로그인 해주세요.");
        }
    }
    private BoardResponse convertToBoard(Board board) {
        Long diaryCount = diaryRepository.countByBoardId(board.getId());
        Long memberCount = shareRepository.countByBoardId(board.getId());
        BoardResponse boardResponse = new BoardResponse();
        boardResponse.setId(board.getId());
        boardResponse.setImageNumber(board.getImageNumber());
        boardResponse.setAppearanceType(board.getAppearanceType());
        boardResponse.setTitle(board.getTitle());
        boardResponse.setRegDate(board.getRegDate());
        boardResponse.setDiaryCount(diaryCount);
        boardResponse.setMemberCount(memberCount);
        boardResponse.setNewBadge(newBadge());
        return boardResponse;
    }
    private boolean newBadge() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(3);
        Boolean badge = diaryRepository.existsDiariesInLastThreeDays(startDate, endDate);
        return badge;
    }
    public void updateBoard(BoardDto boardDto) {
        Optional<Board> boardInfo = boardRepository.findById(boardDto.getId());

        if(boardDto.getBoardTitle().length() > 10) {
            throw new IllegalArgumentException("제목은 10자를 넘을 수 없다.");
        }

        if(boardInfo.isPresent()) {
            Board board = boardInfo.get();
            boardRepository.save(board);
        } else {
            throw new IllegalStateException("삭제된 일기장입니다.");
        }
    }
    public HeroResponse getHero() {
        HeroResponse heroResponse = new HeroResponse();
        Optional<Member> member = memberRepository.findByEmail(email().getEmail());
        Long memberId = member.get().getId();
        heroResponse.setNickname(member.get().getNickname());
        Long shareCount = shareRepository.countByMemberId(memberId);
        heroResponse.setMemberCount(shareCount);
        Long diaryCount = diaryRepository.countIsSharedDiaries(memberId);
        heroResponse.setMemberCount(diaryCount);
        Boolean isShared = boardRepository.existsSharedBoardByMemberId(memberId);
        heroResponse.setShared(isShared);

        return heroResponse;
    }
}

