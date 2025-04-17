package org.team4.sol_server.domain.stamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.team4.sol_server.domain.stamp.entity.Stamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Integer> {

    // user_idx와 day에 해당하는 스탬프 기록 조회
    Optional<Stamp> findByUser_UserIdxAndDay(int userIdx, int day);

    // 사용자에 대한 모든 스탬프 기록 조회
    List<Stamp> findByUser_UserIdx(int userIdx);

    // 특정 유저가 오늘 찍은 스탬프가 있는지 확인
    boolean existsByUser_UserIdxAndCreatedBetween(int userIdx, LocalDateTime start, LocalDateTime end);

    // 마지막으로 찍은 스탬프 조회 (내림차순으로 첫 번째)
    Optional<Stamp> findTopByUser_UserIdxOrderByDayDesc(int userIdx);

    // ⭐ 사용자의 첫 번째(가장 오래된) 스탬프 기록 조회 (챌린지 시작 날짜용)
    @Query("SELECT s.created FROM Stamp s WHERE s.user.userIdx = :userIdx ORDER BY s.created ASC LIMIT 1")
    Optional<LocalDateTime> findFirstStampDateByUserIdx(@Param("userIdx") int userIdx);

    // ✅ 특정 날짜(day)에 찍지 않은 스탬프 목록 조회
    List<Stamp> findByIsStampedFalseAndDay(int day);

    // 유저의 스탬프 개수 카운트 (이미 찍힌 스탬프만)
    long countByUser_UserIdxAndIsStampedTrue(int userIdx);

}
