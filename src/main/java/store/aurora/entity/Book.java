package store.aurora.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer regularPrice;

    private Integer salePrice;

    private Boolean packaging;

    private Integer stock;

    private String title;

    private String contents;

    private String explanation;

    private String ISBN;

    private Date publishDate;

    private Long publisherId;

    private Long seriesId;

    private Boolean isSale;

    // getters and setters
}
