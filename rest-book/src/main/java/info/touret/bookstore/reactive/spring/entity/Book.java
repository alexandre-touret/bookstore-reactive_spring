package info.touret.bookstore.reactive.spring.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;

public class Book implements Serializable {


    private String title;
    @Column("isbn_13")
    private String isbn13;
    @Column("isbn_10")
    private String isbn10;
    private String author;
    @Column( "year_of_publication")
    private Integer yearOfPublication;
    @Column("nb_of_pages")
    private Integer nbOfPages;

    @Min(1)
    @Max(10)
    public Integer rank;
    public BigDecimal price;
    @Column("small_image_url")
    public URL smallImageUrl;
    @Column("medium_image_url")
    public URL mediumImageUrl;
    @Column()
    public String description;
    @Id()
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(Integer yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public Integer getNbOfPages() {
        return nbOfPages;
    }

    public void setNbOfPages(Integer nbOfPages) {
        this.nbOfPages = nbOfPages;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public URL getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(URL smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public URL getMediumImageUrl() {
        return mediumImageUrl;
    }

    public void setMediumImageUrl(URL mediumImageUrl) {
        this.mediumImageUrl = mediumImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
