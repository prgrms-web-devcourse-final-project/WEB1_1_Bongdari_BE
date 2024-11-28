package com.somemore.common.fixture;

import static com.somemore.common.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.recruitboard.domain.VolunteerType.OTHER;

import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.RecruitmentInfo;
import com.somemore.recruitboard.domain.VolunteerType;
import java.time.LocalDateTime;
import java.util.UUID;

public class RecruitBoardFixture {

    private RecruitBoardFixture() {
    }

    public static RecruitBoard createRecruitBoard() {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(UUID.randomUUID())
            .locationId(1L)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .recruitmentInfo(recruitmentInfo)
            .build();
    }

    public static RecruitBoard createRecruitBoard(String title) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(UUID.randomUUID())
            .locationId(1L)
            .title(title)
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .recruitmentInfo(recruitmentInfo)
            .build();
    }

    public static RecruitBoard createRecruitBoard(String title, UUID centerId, Long locationId) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(centerId)
            .locationId(locationId)
            .title(title)
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .recruitmentInfo(recruitmentInfo)
            .build();
    }

    public static RecruitBoard createRecruitBoard(String title, UUID centerId) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(centerId)
            .locationId(1L)
            .title(title)
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .recruitmentInfo(recruitmentInfo)
            .build();
    }

    public static RecruitBoard createRecruitBoard(VolunteerType type, UUID centerId) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(type)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(centerId)
            .locationId(1L)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .recruitmentInfo(recruitmentInfo)
            .build();
    }

    public static RecruitBoard createRecruitBoard(Boolean admitted, UUID centerId) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(OTHER)
            .admitted(admitted)
            .build();

        return RecruitBoard.builder()
            .centerId(centerId)
            .locationId(1L)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .recruitmentInfo(recruitmentInfo)
            .build();
    }


    public static RecruitBoard createRecruitBoard(Long locationId) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(UUID.randomUUID())
            .locationId(locationId)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .recruitmentInfo(recruitmentInfo)
            .build();
    }

    public static RecruitBoard createRecruitBoard(UUID centerId) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(centerId)
            .locationId(1L)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .recruitmentInfo(recruitmentInfo)
            .build();
    }

    public static RecruitBoard createRecruitBoard(UUID centerId, Long locationId) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(centerId)
            .locationId(locationId)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .recruitmentInfo(recruitmentInfo)
            .build();
    }

    public static RecruitBoard createRecruitBoard(String region, VolunteerType volunteerType) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
            .region(region)
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(volunteerType)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(UUID.randomUUID())
            .locationId(1L)
            .title("봉사모집제목")
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .recruitmentInfo(recruitmentInfo)
            .build();
    }

    public static RecruitBoard createRecruitBoard(Long locationId, String title) {
        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
            .region("경기")
            .recruitmentCount(1)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(OTHER)
            .admitted(true)
            .build();

        return RecruitBoard.builder()
            .centerId(UUID.randomUUID())
            .locationId(locationId)
            .title(title)
            .content("봉사모집내용")
            .imgUrl("https://image.domain.com/links")
            .recruitmentInfo(recruitmentInfo)
            .build();
    }
}
