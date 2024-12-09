package com.somemore.recruitboard.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Document(indexName = "recruit_board")
public class RecruitBoardDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer")
    private String title;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer")
    private String content;


    @Builder
    public RecruitBoardDocument(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
