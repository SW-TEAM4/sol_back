package org.team4.sol_server.domain.news;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Data
public class StockNewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "summary")
    private String summary;

    @Column(name = "link")
    private String link;

    @Column(name = "time_info")
    private String timeInfo;

    @Column(name = "publication_date")
    private LocalDateTime publicationDate;
}

