package com.somemore.domains.search.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@Document(indexName = "community_board")
public class CommunityBoardDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer")
    private String title;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer")
    private String content;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer")
    private String writerNickname;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime updatedAt;

    @Builder
    public CommunityBoardDocument(Long id, String title, String content, String writerNickname,
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writerNickname = writerNickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
