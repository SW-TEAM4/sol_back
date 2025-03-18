package org.team4.sol_server.domain.stock.category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category") // 데이터베이스 테이블 이름과 매핑
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "category")
    private String category;

    @Column(name = "current_price")
    private Double currentPrice;

    @Column(name = "yesterday_change")
    private Double yesterdayChange;

    @Column(name = "one_month_change")
    private Double oneMonthChange;

    @Column(name = "three_month_change")
    private Double threeMonthChange;

    @Column(name = "one_year_change")
    private Double oneYearChange;
}
