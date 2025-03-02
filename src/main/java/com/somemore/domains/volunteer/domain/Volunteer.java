package com.somemore.domains.volunteer.domain;

import com.somemore.domains.volunteer.dto.request.VolunteerProfileUpdateRequestDto;
import com.somemore.global.auth.oauth.domain.OAuthProvider;
import com.somemore.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "volunteer") // TODO 삭제될 엔티티(테이블)
public class Volunteer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider", nullable = false)
    private OAuthProvider oauthProvider;

    @Column(name = "oauth_id", nullable = false)
    private String oauthId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Lob
    @Column(name = "introduce", length = 500, nullable = false)
    private String introduce;

    @Enumerated(EnumType.STRING)
    @Column(name = "tier", nullable = false, length = 20)
    private Tier tier;

    @Column(name = "total_volunteer_hours", nullable = false)
    private Integer totalVolunteerHours;

    @Column(name = "total_volunteer_count", nullable = false)
    private Integer totalVolunteerCount;


    public static Volunteer createDefault(OAuthProvider oauthProvider, String oauthId) {
        return Volunteer.builder()
                .oauthProvider(oauthProvider)
                .oauthId(oauthId)
                .nickname(UUID.randomUUID().toString().substring(0, 8))
                .imgUrl("")
                .introduce("")
                .tier(Tier.RED)
                .totalVolunteerHours(0)
                .totalVolunteerCount(0)
                .build();
    }

    public void updateWith(VolunteerProfileUpdateRequestDto dto, String imgUrl) {
        this.nickname = dto.nickname();
        this.introduce = dto.introduce();
        this.imgUrl = imgUrl;
    }

    public void updateVolunteerStats(int hours, int count) {
        this.totalVolunteerHours += hours;
        this.totalVolunteerCount += count;
    }

    @Builder
    private Volunteer(
            OAuthProvider oauthProvider,
            String oauthId,
            String nickname,
            String imgUrl,
            String introduce,
            Tier tier,
            Integer totalVolunteerHours,
            Integer totalVolunteerCount
    ) {
        this.oauthProvider = oauthProvider;
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.introduce = introduce;
        this.tier = tier;
        this.totalVolunteerHours = totalVolunteerHours;
        this.totalVolunteerCount = totalVolunteerCount;
    }
}
