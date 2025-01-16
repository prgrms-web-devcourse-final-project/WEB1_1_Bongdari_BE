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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Column(name = "introduce", nullable = false)
    private String introduce;

    @Column(name = "is_customized", nullable = false)
    private boolean isCustomized;

    public void customize() {
        // TODO param의 정보를 필드에 업데이트
        this.isCustomized = true;
    }

    public static UserCommonAttribute createDefault(UUID userId, UserRole role) {
        return UserCommonAttribute.builder()
                .userId(userId)
                .name(role.getDescription() + UUID.randomUUID().toString().substring(0, 8))
                .contactNumber("")
                .imgUrl(ImageUploadService.DEFAULT_IMAGE_URL)
                .introduce("")
                .isCustomized(false)
                .build();
    }

    @Builder
    private UserCommonAttribute(UUID userId, String name, String contactNumber, String imgUrl, String introduce, boolean isCustomized) {
        this.userId = userId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.imgUrl = imgUrl;
        this.introduce = introduce;
        this.isCustomized = isCustomized;
    }
}
