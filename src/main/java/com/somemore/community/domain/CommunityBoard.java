package com.somemore.community.domain;

import com.somemore.community.dto.request.CommunityBoardUpdateRequestDto;
import com.somemore.global.common.entity.BaseEntity;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "community_board")
public class CommunityBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "writer_id", nullable = false, length = 16)
    private UUID writerId;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Builder
    public CommunityBoard(UUID writerId, String title, String content, String imgUrl) {
        this.writerId = writerId;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
    }

    public boolean isWriter(UUID writerId) {
        return this.writerId.equals(writerId);
    }

    public void updateWith(CommunityBoardUpdateRequestDto dto, String imgUrl) {
        this.title = dto.title();
        this.content = dto.content();
        this.imgUrl = imgUrl;
    }
}