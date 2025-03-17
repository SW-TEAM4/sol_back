package org.team4.sol_server.domain.stamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.team4.sol_server.config.BaseException;
import org.team4.sol_server.config.BaseResponseStatus;
import org.team4.sol_server.domain.login.entity.User;
import org.team4.sol_server.domain.login.repository.UserRepository;
import org.team4.sol_server.domain.stamp.entity.Stamp;
import org.team4.sol_server.domain.stamp.repository.StampRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public boolean hasStampedToday(int userIdx) throws BaseException {
        User user = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
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
        // 현재 날짜 확인
        LocalDate today = LocalDate.now();

        // 사용자의 이미 찍힌 스탬프 개수 확인
        long stampedCount = stampRepository.countByUser_UserIdxAndIsStampedTrue(userIdx);

        // 첫 번째 스탬프 날짜가 없다면, 현재 날짜부터 시작해서 1일차부터 찍도록 설정
        int nextDayToStamp = (int) stampedCount + 1;

        if (nextDayToStamp > 30) {
            System.out.println("스탬프 챌린지가 종료되었습니다. userIdx: " + userIdx); // 30일 초과 시 종료
            return false;
        }

        // 이미 해당 날짜에 스탬프가 찍혔는지 확인
        Optional<Stamp> existingStamp = stampRepository.findByUser_UserIdxAndDay(userIdx, nextDayToStamp);

        if (existingStamp.isPresent()) {
            Stamp stamp = existingStamp.get();
            if (!stamp.isStamped()) {
                // 스탬프는 있지만 아직 찍히지 않았다면 찍기
                stamp.setStamped(true);
                stamp.setUpdated(LocalDateTime.now());
                try {
                    stampRepository.save(stamp);
                    System.out.println("스탬프 저장 성공 userIdx: " + userIdx + " day: " + nextDayToStamp); // 성공 로그 추가
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("스탬프 저장 실패 userIdx: " + userIdx + " day: " + nextDayToStamp); // 실패 로그 추가
                    return false;
                }
            }
        } else {
            // 해당 날짜에 스탬프 데이터가 없으면 새로 생성
            Stamp newStamp = new Stamp();
            newStamp.setUser(userRepository.findByUserIdx(userIdx).orElse(null));
            newStamp.setDay(nextDayToStamp); // 찍혀야 할 날
            newStamp.setStamped(true);
            newStamp.setUpdated(LocalDateTime.now());

            try {
                stampRepository.save(newStamp);
                System.out.println("새로운 스탬프 저장 성공 userIdx: " + userIdx + " day: " + nextDayToStamp); // 성공 로그 추가
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("새로운 스탬프 저장 실패 userIdx: " + userIdx + " day: " + nextDayToStamp); // 실패 로그 추가
                return false;
            }
        }

        // 모든 스탬프가 이미 찍혀있다면
        System.out.println("모든 스탬프가 이미 찍혀 있습니다. userIdx: " + userIdx);
        return false;
    }


//    public boolean saveStamp(int userIdx) {
//        // 오늘 날짜 확인
//        LocalDate today = LocalDate.now();
//
//        // 사용자의 첫 번째 스탬프 날짜 조회
//        Optional<LocalDateTime> challengeStartDate = getChallengeStartDate(userIdx);
//
//        // 첫 번째 스탬프 날짜가 없다면, 현재 날짜를 첫 번째 스탬프 날짜로 설정
//        if (!challengeStartDate.isPresent()) {
//            // 처음 스탬프 찍는 경우, 새로운 스탬프 기록을 저장
//            Stamp newStamp = new Stamp();
//            newStamp.setUser(userRepository.findByUserIdx(userIdx).orElse(null)); // 유저 정보 설정
//            newStamp.setDay(1); // ✅ 첫 스탬프는 항상 1일차로 시작
//            newStamp.setStamped(true);
//            newStamp.setUpdated(LocalDateTime.now());
//
//            try {
//                stampRepository.save(newStamp);
//                return true; // 첫 스탬프 저장 성공
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        // 첫 번째 스탬프 날짜가 있으면 그 날짜부터 놓친 날 계산
//        LocalDate startDate = challengeStartDate.get().toLocalDate();
//        int calculatedDay = (int) ChronoUnit.DAYS.between(startDate, today) + 1; // 1일차부터 시작하도록 조정
//
//        if (calculatedDay > 30) {
//            System.out.println("스탬프 챌린지가 종료되었습니다.");
//            return false; // 30일 초과 시 챌린지가 종료됨
//        }
//
//        // ✅ 중간에 건너뛴 날이 있는지 확인하고 흐린 이미지 처리
//        for (int missingDay = 1; missingDay < calculatedDay; missingDay++) {
//            Optional<Stamp> missedStamp = stampRepository.findByUser_UserIdxAndDay(userIdx, missingDay);
//
//            if (!missedStamp.isPresent()) {
//                // ✅ 스탬프를 놓친 날은 `stamped=false`로 기록 (흐린 이미지 처리)
//                Stamp newMissedStamp = new Stamp();
//                newMissedStamp.setUser(userRepository.findByUserIdx(userIdx).orElse(null));
//                newMissedStamp.setDay(missingDay);
//                newMissedStamp.setStamped(false); // ❌ 놓친 날은 스탬프 안 찍힘
//                newMissedStamp.setUpdated(LocalDateTime.now());
//
//                stampRepository.save(newMissedStamp);
//            }
//        }
//
//        // 이미 찍힌 날 확인
//        Optional<Stamp> existingStamp = stampRepository.findByUser_UserIdxAndDay(userIdx, calculatedDay);
//
//        if (existingStamp.isPresent()) {
//            Stamp stamp = existingStamp.get();
//            if (!stamp.isStamped()) {
//                // 이미 해당 날짜에 스탬프가 있지만, 아직 찍히지 않았다면 스탬프 찍기
//                stamp.setStamped(true);
//                stamp.setUpdated(LocalDateTime.now());
//
//                try {
//                    stampRepository.save(stamp);
//                    return true; // 스탬프 저장 성공
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return false;
//                }
//            }
//        } else {
//            // 해당 날짜의 스탬프 데이터가 없으면 새로 생성
//            Stamp newStamp = new Stamp();
//            newStamp.setUser(userRepository.findByUserIdx(userIdx).orElse(null));
//            newStamp.setDay(calculatedDay); // 해당 날짜에 맞는 순차적인 스탬프
//            newStamp.setStamped(true);
//            newStamp.setUpdated(LocalDateTime.now());
//
//            try {
//                stampRepository.save(newStamp);
//                return true; // 스탬프 저장 성공
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        // 모든 스탬프가 이미 찍혀있는 경우
//        System.out.println("모든 스탬프가 이미 찍혀 있습니다. userIdx: " + userIdx);
//        return false;
//    }

    // 자정에 모든 사용자의 스탬프 초기화
    @Scheduled(cron = "0 0 0 * * ?")  // 매일 00시 실행
    public void resetDailyStamps() {
        // 어제 날짜 가져오기
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // 어제 찍지 않은 스탬프만 찾아서 false로 설정
        List<Stamp> missedStamps = stampRepository.findByIsStampedFalseAndDay(yesterday.getDayOfMonth());
        for (Stamp stamp : missedStamps) {
            stamp.setStamped(false); // 놓친 날을 흐린 이미지로 유지
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
