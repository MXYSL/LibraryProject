package com.Team3.LibraryProject.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String edition;
    private Integer totalQuantity;
    private Integer availableQuantity;
    private String isbn;
    private String synopsis;
    private String genreName;
    private String coverImageUrl;
    private String materialType;
    private Double averageRating;
}
