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

    @Column(name = "stock_name")
    private String stockName; // 보유 주식 이름

    @Column(name = "ticker")
    private String ticker; // 주식 Ticker

    @Column(name = "stock_count")
    private Double stockCount; // 보유 주식 수량

    @Column(name = "average_price")
    private Double averagePrice; // 매수 평균가

    @Column(name = "closing_price") // 전일 종가 컬럼
    private Double closingPrice;

    @Column(name = "purchase_amount")
    private Double purchaseAmount; // 총 매수 금액

    @Column(name = "profit_loss") // 평가 손익 컬럼
    private Double profitLoss;

    @Column(name = "profit_loss_rate") // 평가 수익률 컬럼
    private Double profitLossRate;
}
