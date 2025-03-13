package org.team4.sol_server.domain.portfolio;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "portfolio")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "krw_balance")
    private Double krwBalance; // 보유 KRW (증권 계좌)

    @Column(name = "stock_count")
    private Integer stockCount; // 보유 주식 종류 수

    @Column(name = "total_purchase_amount")
    private Double totalPurchaseAmount; // 총 매수 금액

    @Column(name = "stock_name")
    private String stockName; // 보유 주식 이름

    @Column(name = "ticker")
    private String ticker; // 주식 Ticker

    @Column(name = "stock_quantity")
    private Double stockQuantity; // 보유 주식 수량

    @Column(name = "average_purchase_price")
    private Double averagePurchasePrice; // 매수 평균가

    @Column(name = "purchase_amount")
    private Double purchaseAmount; // 총 매수 금액

    @Column(name = "closing_price") // 전일 종가 컬럼
    private Double closingPrice;

    @Column(name = "evaluation_amount") // 평가 금액 컬럼
    private Double evaluationAmount;

    @Column(name = "profit_loss") // 평가 손익 컬럼
    private Double profitLoss;

    @Column(name = "profit_loss_rate") // 평가 수익률 컬럼
    private Double profitLossRate;
}
