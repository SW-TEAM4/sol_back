package org.team4.sol_server.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team4.sol_server.domain.account.entity.AccountEntity;
import org.team4.sol_server.domain.account.repository.AccountRepository;
import java.util.List;
/**
 * 파일명 : InterestScheduler.java
 * 생성자 : JDeok
 * 날 짜  : 2025.03.09
 * 시 간  : 오후 02:14
 * 기 능  : 이자 스케줄링
 * 변경사항
     - 2025.03.09 : JDeok(최초작성)
**/
@Service
public class InterestScheduler {

    private final AccountRepository accountRepository;

    public InterestScheduler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    //   매일 자정(00:00)에 실행
    @Scheduled(cron = "0 06 10 * * *")
    @Transactional
    public void applyInterest() {
        //  기존 계좌 정보 출력
        List<AccountEntity> accounts = accountRepository.findAll();
        System.out.println("----- [이자 적용 전] -----");
        for (AccountEntity account : accounts) {
            System.out.println("계좌번호: "    + account.getAccountNumber()
                             + ", 잔고: "     + account.getBalance()
                             + ", 현재 이자: " + account.getInterest());
        }

        // 계좌 찾기
        List<AccountEntity> updatedAccounts = accountRepository.findAll();
        double  interestRate  = 0.02;  // 2% 이자율


        for(AccountEntity account: updatedAccounts){
            long currentBalance = account.getBalance();                                  // 현재 잔고
            int  currentInterest = account.getInterest();                                // 현재 이자
            int dailyInterest = (int) Math.round((currentBalance * interestRate) / 365); // 하루 이자

            int updatedInterest = currentInterest + dailyInterest;                       // 기존이자 + 계산된 이자
            account.setInterest(updatedInterest);                                        // 계좌 업데이트

            System.out.println("계좌번호: "      + account.getAccountNumber() +
                            " | 기존 이자: "     + currentInterest +
                            " | 추가된 이자: "   + dailyInterest +
                            " | 최종 적용 이자: " + updatedInterest);
        }
        accountRepository.saveAll(accounts); // 변경된 모든 계좌 저장
        System.out.println("  이자 적용 완료: " + accounts.size() + "개 계좌 업데이트");
    }
}

