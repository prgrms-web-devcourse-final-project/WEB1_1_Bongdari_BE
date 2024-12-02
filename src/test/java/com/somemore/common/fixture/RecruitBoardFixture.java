package com.somemore.common.fixture;

import static com.somemore.common.fixture.LocalDateTimeFixture.createStartDateTime;
import static com.somemore.recruitboard.domain.VolunteerType.OTHER;

import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.domain.RecruitmentInfo;
import com.somemore.recruitboard.domain.VolunteerType;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;

public class RecruitBoardFixture {

    public static final String REGION = "경기";
    public static final int RECRUITMENT_COUNT = 1;
    public static final LocalDateTime START_DATE_TIME = createStartDateTime();
    public static final LocalDateTime END_DATE_TIME = START_DATE_TIME.plusHours(1);
    public static final boolean ADMITTED = true;
    public static final long LOCATION_ID = 1L;
    public static final String TITLE = "봉사모집제목";
    public static final String CONTENT = "봉사모집내용";
    public static final String IMG_URL = "https://image.domain.com/links";
    public static final VolunteerType VOLUNTEER_TYPE = OTHER;

    private RecruitBoardFixture() {
    }

    public static RecruitBoard createRecruitBoard() {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(VOLUNTEER_TYPE)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(String title) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(VOLUNTEER_TYPE)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(LOCATION_ID)
                .title(title)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(String title, UUID centerId, Long locationId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(VOLUNTEER_TYPE)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(locationId)
                .title(title)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(String title, UUID centerId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(VOLUNTEER_TYPE)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(LOCATION_ID)
                .title(title)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(VolunteerType type, UUID centerId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(type)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(Boolean admitted, UUID centerId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(VOLUNTEER_TYPE)
                .admitted(admitted)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(Long locationId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(VOLUNTEER_TYPE)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(locationId)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(UUID centerId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(VOLUNTEER_TYPE)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(UUID centerId, Long locationId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(VOLUNTEER_TYPE)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(locationId)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(String region, VolunteerType volunteerType) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(region)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(volunteerType)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(Long locationId, String title) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(VOLUNTEER_TYPE)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(locationId)
                .title(title)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createCompletedRecruitBoard() {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(VOLUNTEER_TYPE)
                .admitted(ADMITTED)
                .build();

        RecruitBoard recruitBoard = RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();

        setRecruitStatusCompleted(recruitBoard);

        return recruitBoard;
    }

    public static RecruitBoard createCompletedRecruitBoard(UUID centerId, VolunteerType type) {
        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(type)
                .admitted(ADMITTED)
                .build();

        RecruitBoard recruitBoard = RecruitBoard.builder()
                .centerId(centerId)
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();

        setRecruitStatusCompleted(recruitBoard);

        return recruitBoard;
    }

    public static RecruitBoard createCompletedRecruitBoard(VolunteerType type) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerType(type)
                .admitted(ADMITTED)
                .build();

        RecruitBoard recruitBoard = RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();

        setRecruitStatusCompleted(recruitBoard);

        return recruitBoard;
    }

    private static void setRecruitStatusCompleted(RecruitBoard recruitBoard) {
        try {
            Field recruitStatusField = RecruitBoard.class.getDeclaredField("recruitStatus");
            recruitStatusField.setAccessible(true); // private 필드 접근 가능 설정
            recruitStatusField.set(recruitBoard, RecruitStatus.COMPLETED); // 필드 값 설정
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("리플렉션으로 recruitStatus를 설정하는 것에 실패했습니다", e);
        }
    }
}
