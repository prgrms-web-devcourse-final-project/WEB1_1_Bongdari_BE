package com.somemore.center.repository;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
class CenterRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private CenterRepository centerRepository;

    @DisplayName("기관 id로 기관 프로필을 조회할 수 있다.")
    @Test
    void findById() {
        //given
        Center center = Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://example.com/image.jpg",
                "기관 소개 내용",
                "http://example.com",
                "account123",
                "password123"
        );
        centerRepository.save(center);

        //when
        Center foundCenter = centerRepository.findById(center.getId()).orElse(null);

        //then
        assertThat(foundCenter).isNotNull();
        assertThat(foundCenter.getName()).isEqualTo("기본 기관 이름");
        assertThat(foundCenter.getContactNumber()).isEqualTo("010-1234-5678");
        assertThat(foundCenter.getImgUrl()).isEqualTo("http://example.com/image.jpg");
        assertThat(foundCenter.getIntroduce()).isEqualTo("기관 소개 내용");
        assertThat(foundCenter.getHomepageLink()).isEqualTo("http://example.com");
        assertThat(foundCenter.getAccountId()).isEqualTo("account123");
        assertThat(foundCenter.getAccountPw()).isEqualTo("password123");
    }

    @DisplayName("기관 id로 기관이 존재하는지 확인할 수 있다.")
    @Test
    void existsById() {
        //given
        Center center = Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://example.com/image.jpg",
                "기관 소개 내용",
                "http://example.com",
                "account123",
                "password123"
        );
        centerRepository.save(center);

        //when
        Boolean isExist = centerRepository.existsById(center.getId());

        //then
        assertThat(isExist).isTrue();
    }

    @DisplayName("존재하지 않는 기관 ID로 조회 시 false를 반환한다")
    @Test
    void notExistsById() {
        //given
        UUID nonExistentId = UUID.randomUUID();

        //when
        Boolean isExist = centerRepository.existsById(nonExistentId);

        //then
        assertThat(isExist).isFalse();
    }

}
