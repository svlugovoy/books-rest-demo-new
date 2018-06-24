package com.svlugovoy.books.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("Book model")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("Unique numeric book identifer")
    private Long id;

    @NotNull(message = "Field author must not be null")
    @Size(min = 2, max = 30, message = "Not supported field size")
    private String author;

    @NotNull
    @Size(min = 2, max = 40, message = "Not supported field size")
    private String name;

    @JsonProperty("published")
    @Min(value = 1900)
    @Max(value = 2020, message = "Not supported value: > then {value}")
    private int year;

    private boolean rented;

}
