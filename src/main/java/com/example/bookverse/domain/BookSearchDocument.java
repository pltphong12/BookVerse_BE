package com.example.bookverse.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Document(indexName = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Double)
    private double price;

    @Field(type = FieldType.Integer)
    private int discount;

    @Field(type = FieldType.Long)
    private long sold;

    @Field(type = FieldType.Keyword)
    private String image;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Instant createdAt;
}
