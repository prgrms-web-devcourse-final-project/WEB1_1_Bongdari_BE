package com.somemore.user.domain;

import com.somemore.global.common.entity.BaseEntity;
import com.somemore.global.imageupload.service.ImageUploadService;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_common_attribute")
public class UserCommonAttribute extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Column(name = "introduce", nullable = false)
    private String introduce;

    @Column(name = "is_customized", nullable = false)
    private boolean isCustomized;

    public static UserCommonAttribute createDefault(UUID userId) {
        return UserCommonAttribute.builder()
                .userId(userId)
                .nickname(String.valueOf(UUID.randomUUID()).substring(0, 8))
                .imgUrl(ImageUploadService.DEFAULT_IMAGE_URL)
                .introduce("")
                .isCustomized(false)
                .build();
    }

    @Builder
    private UserCommonAttribute(UUID userId, String nickname, String imgUrl, String introduce, boolean isCustomized) {
        this.userId = userId;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.introduce = introduce;
        this.isCustomized = isCustomized;
    }
}
