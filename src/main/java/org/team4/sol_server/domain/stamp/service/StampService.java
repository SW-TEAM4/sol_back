package org.team4.sol_server.domain.stamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.team4.sol_server.domain.stamp.entity.Stamp;
import org.team4.sol_server.domain.stamp.repository.StampRepository;
import org.team4.sol_server.domain.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
파일명 : StampService.java
생성자 : JM
날 짜  : 2025.03.10
시 간  : 오전 11:00
기 능  : 유저 정보
Params :
Return :
변경사항
     - 2025.03.10 : JM(최초작성)
*/

@Service
public class StampService {
    @Autowired
    private StampRepository stampRepository;

    @Autowired
    private UserRepository userRepository;

    // 사용자가 오늘 스탬프를 찍었는지 확인하는 메서드
    public boolean hasStampedToday(int userIdx) {
        LocalDate today = LocalDate.now();
        return stampRepository.existsByUser_UserIdxAndCreatedBetween(
                userIdx,
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        );
    }

    // 사용자의 첫 번째 스탬프(챌린지 시작 날짜) 조회
    public Optional<LocalDateTime> getChallengeStartDate(int userIdx) {
        return stampRepository.findFirstStampDateByUserIdx(userIdx);
    }


    // 스탬프 저장 및 상태 업데이트 (하루에 한 번만 가능)
    public boolean saveStamp(int userIdx) {
        // 오늘 날짜 확인
        LocalDate today = LocalDate.now();

        // 사용자의 첫 번째 스탬프 날짜 조회
        Optional<LocalDateTime> challengeStartDate = getChallengeStartDate(userIdx);

        // 첫 번째 스탬프 날짜가 없다면, 현재 날짜를 첫 번째 스탬프 날짜로 설정
        if (!challengeStartDate.isPresent()) {
            // 처음 스탬프 찍는 경우, 새로운 스탬프 기록을 저장
            Stamp newStamp = new Stamp();
            newStamp.setUser(userRepository.findById(userIdx).orElse(null)); // 유저 정보 설정
            newStamp.setDay(today.getDayOfMonth());
            newStamp.setStamped(true);
            newStamp.setUpdated(LocalDateTime.now());

            try {
                stampRepository.save(newStamp);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        // 첫 번째 스탬프 날짜가 있으면 그 날짜부터 놓친 날 계산
        LocalDate startDate = challengeStartDate.get().toLocalDate();
        int missedDay = today.getDayOfMonth() - startDate.getDayOfMonth();

        if (missedDay > 1) {
            // 1일 이상 스탬프를 놓쳤다면 다른 이미지 처리
            return false;  // 다른 이미지로 처리
        }

        // 1~30일까지 찍을 수 있는 스탬프 찾기
        for (int day = 1; day <= 30; day++) {
            Optional<Stamp> existingStamp = stampRepository.findByUser_UserIdxAndDay(userIdx, day);

            // 아직 찍지 않은 날이면 스탬프 찍기
            if (existingStamp.isPresent()) {
                Stamp stamp = existingStamp.get();

                if (!stamp.isStamped()) {
                    stamp.setStamped(true);
                    stamp.setUpdated(LocalDateTime.now());  // 최신 시간 갱신
                    try {
                        stampRepository.save(stamp);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            } else {
                // 해당 날짜의 스탬프 데이터가 없으면 새로 생성
                Stamp newStamp = new Stamp();
                newStamp.setUser(userRepository.findById(userIdx).orElse(null)); // 유저 정보 설정
                newStamp.setDay(day);
                newStamp.setStamped(true);
                newStamp.setUpdated(LocalDateTime.now());

                try {
                    stampRepository.save(newStamp);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        System.out.println("모든 스탬프가 이미 찍혀 있습니다. userIdx: " + userIdx);
        return false;
    }

    // 자정에 모든 사용자의 스탬프 초기화
    @Scheduled(cron = "0 0 0 * * ?")  // 매일 00시 실행
    public void resetDailyStamps() {
        List<Stamp> allStamps = stampRepository.findAll();
        for (Stamp stamp : allStamps) {
            stamp.setStamped(false);
            stampRepository.save(stamp);
        }
    }

    // 사용자 스탬프 기록을 1~30일까지 일차별로 가져오는 메서드
    public List<Stamp> getStampsByUser(int userIdx) {
        return stampRepository.findByUser_UserIdx(userIdx);
    }

    // 사용자가 놓친 날짜 계산 (일수가 몇일인지)
    public int calculateMissedDays(int userIdx) {
        Optional<Stamp> lastStamp = stampRepository.findTopByUser_UserIdxOrderByDayDesc(userIdx);
        if (lastStamp.isPresent()) {
            LocalDate today = LocalDate.now();
            return today.getDayOfMonth() - lastStamp.get().getDay();
        }
        return 0;  // 처음 시작할 때는 놓친 날이 없음
    }
}
