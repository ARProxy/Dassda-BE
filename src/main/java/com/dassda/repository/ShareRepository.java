package com.dassda.repository;

import com.dassda.entity.Board;
import com.dassda.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShareRepository extends JpaRepository<Share, Long> {
    int countByBoardId(Long boardId);
    @Query("SELECT COUNT(s) FROM Share s WHERE s.board.member.id = :memberId AND s.board.isShared = true")
    int countByMemberIdAboutShare(@Param("memberId") Long memberId);
    @Query("SELECT s FROM Share s WHERE s.board.id = :boardId")
    List<Share> findByBoardIdAboutMembers(@Param(value = "boardId") Long boardId);

    Share findByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    boolean existsById(Long memberId);

    Optional<Share> findByMemberIdAndBoardId(Long memberId, Long boardId);

    boolean existsByBoardIdAndMemberId(Long boardId, Long memberId);
    List<Share> findByMemberId(Long currentMemberId);

    @Query("SELECT s.board FROM Share s WHERE s.member.id = :memberId")
    List<Board> findBoardsShared(@Param("memberId") Long memberId);

    boolean existsByBoardId(Long boardId);
    boolean existsByMemberId(Long memberId);
    List<Share> findByBoardId(Long boardId);


}

