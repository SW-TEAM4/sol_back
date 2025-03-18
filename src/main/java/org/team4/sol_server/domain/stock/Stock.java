package org.team4.sol_server.domain.stock;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Entity
@Table(name = "stock")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "date")
    private LocalDate date;

    private int startPrice;
    private int highPrice;
    private int lowPrice;
    private int endPrice;
    private Long volume;
}
