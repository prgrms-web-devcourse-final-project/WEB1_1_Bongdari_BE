package com.somemore.domains.search.domain;

import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.domain.VolunteerCategory;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Document(indexName = "recruit_board")
public class RecruitBoardDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer")
    private String title;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer")
    private String content;

    @Field(type = FieldType.Text, analyzer = "nori_analyzer")
    private String centerName;

    @Field(type = FieldType.Long)
    private Long locationId;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime updatedAt;

    @Field(type = FieldType.Text)
    private String region;

    @Field(type = FieldType.Keyword)
    private RecruitStatus recruitStatus;

    @Field(type = FieldType.Integer)
    private Integer recruitmentCount;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime volunteerStartDateTime;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime volunteerEndDateTime;

    @Field(type = FieldType.Keyword)
    private VolunteerCategory volunteerCategory;

    @Field(type = FieldType.Integer)
    private Integer volunteerHours;

    @Field(type = FieldType.Boolean)
    private Boolean admitted;

    @Field(type = FieldType.Keyword)
    private UUID centerId;

    @Field(type = FieldType.Text)
    private String address;

    @GeoPointField
    private double[] location;

    @Builder
    public RecruitBoardDocument(Long id, String title, String content, String centerName,
                                Long locationId, LocalDateTime createdAt, LocalDateTime updatedAt,
                                String region, RecruitStatus recruitStatus, Integer recruitmentCount,
                                LocalDateTime volunteerStartDateTime, LocalDateTime volunteerEndDateTime,
                                VolunteerCategory volunteerCategory, Integer volunteerHours,
                                Boolean admitted, UUID centerId, String address,
                                double[] location) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.centerName = centerName;
        this.locationId = locationId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.region = region;
        this.recruitStatus = recruitStatus;
        this.recruitmentCount = recruitmentCount;
        this.volunteerStartDateTime = volunteerStartDateTime;
        this.volunteerEndDateTime = volunteerEndDateTime;
        this.volunteerCategory = volunteerCategory;
        this.volunteerHours = volunteerHours;
        this.admitted = admitted;
        this.centerId = centerId;
        this.address = address;
        this.location = location;
    }
}
