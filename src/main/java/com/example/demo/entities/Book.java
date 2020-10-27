package com.example.demo.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Book {
    @Id
    private String id;
    @NotEmpty(message = "isbn can not be empty")
    private String isbn;
    @NotEmpty(message = "name can not be empty")
    private String name;
    @NotEmpty(message = "plot can not be empty")
    private String plot;
    @NotEmpty(message = "author name can not be empty")
    private String author;
    @NotEmpty(message = "genre can not be empty")
    private String genre;
    @NotNull
    private boolean isAvailable;

}
