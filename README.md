![somemore_logo2](https://github.com/user-attachments/assets/dc84b70c-66d6-48dd-a8f7-506fb14e83b5)

## Bongdari_BE

Programmers DevCourse BE 1기 8팀 최종 프로젝트 - 백엔드

***

## 📺 시연 영상

<table width="100%">
  <tr>
    <td align="center" width="50%">
      <h3>봉사자</h3>
      <a href="https://www.youtube.com/watch?v=Y_pWZVyEhyI">
        <img src="https://img.youtube.com/vi/Y_pWZVyEhyI/0.jpg" width="100%">
      </a>
    </td>
    <td align="center" width="50%">
      <h3>기관</h3>
      <a href="https://www.youtube.com/watch?v=jq6GtXT8OKU">
        <img src="https://img.youtube.com/vi/jq6GtXT8OKU/0.jpg" width="100%">
      </a>
    </td>
  </tr>
</table>

***

## 🧑‍🧑‍🧒‍🧒 Team

|                          조재중 (팀장)                          |                         윤서진 (PM)                          |                            양아영                            |                            이범수                            |
|:----------------------------------------------------------:|:---------------------------------------------------------:|:---------------------------------------------------------:|:---------------------------------------------------------:|
|                   인증/인가, SSE 기반 알림, 봉사자                    |                  기관 프로필/관심기관/쪽지, 프로젝트 배포                  |             커뮤니티 게시판/댓글, elasticsearch 기반 검색              |                 봉사 활동 모집/지원, 봉사 활동 관리/리뷰                  |
|          [m-a-king](https://github.com/m-a-king)           |              [7zrv](https://github.com/7zrv)              |        [ayoung-dev](https://github.com/ayoung-dev)        |         [leebs0521](https://github.com/leebs0521)         
| ![](https://avatars.githubusercontent.com/u/126754298?v=4) | ![](https://avatars.githubusercontent.com/u/90759319?v=4) | ![](https://avatars.githubusercontent.com/u/52439725?v=4) | ![](https://avatars.githubusercontent.com/u/86824224?v=4) |

***

# 1. 개요

## 프로젝트 소개

![somemore_logo](https://github.com/user-attachments/assets/a5ec03a0-bdac-46ea-841d-b86dcdc769dc)

### 자원 봉사자 구호활동 연결 플랫폼 : 손모아(SOMEMORE)

> 손모아(SomeMore)는 재해나 자원이 필요한 현장에서 도움을 원하는 단체와 자원봉사자를 연결하는 중계 플랫폼입니다.
> 자원이 필요한 기관의 정보를 한눈에 확인하고, 자신의 능력과 자원에 맞는 구호 활동에 참여할 수 있도록 돕습니다.

###

### 기획 배경

> 재해 현장이나 공익을 위한 활동에서 자원봉사자와 단체 간의 연결은 여전히 불편하고 번거로운 절차를 요구합니다.
> 기존 봉사 플랫폼의 관료적 절차를 개선하고 즉각적인 매칭 시스템을 구축하고자, 손모아는 봉사자와 기관이 **상호 주체적인 역할**을 통해 직접적으로 소통하고, 효율적으로 활동을 이어나갈 수 있도록 돕는 플랫폼을
> 기획했습니다.
> 이로 지원봉사 문화의 접근성이 향상되고, 지속 가능한 사회 공헌 생태계가 구축될 것을 기대합니다.

###

### 핵심 가치

- 연결: 도움이 필요한 곳과 봉사자를 직접 연결
- 자율성: 기관과 봉사자 모두에게 주도적인 참여 기회 제공

### 개발 기간

> 2024/11/12 ~ 2024/02/10

---

# 2. 주요 기능

- 로그인/회원가입
    - OAuth, ID/PW
    - JWT
- 도움 요청글
- 커뮤니티
    - 커뮤니티 글 작성, 조희, 수정, 삭제
    - 커뮤니티 댓글(대댓글) 작성, 조회, 수정, 삭제
- 랭킹
- 알림
    - SSE
- 검색
    - 도움 요청글 위치 기반 검색
    - 도움 요청글 제목, 내용 키워드 검색
    - 커뮤니티 제목, 내용 키워드 검색
- 쪽지

---

# 3. 시스템 아키텍처

![somemore_service](https://github.com/user-attachments/assets/0b3ab011-fc3e-442b-8aac-37de286a589a)

---

# 4. ERD

![somemore ERD](https://github.com/user-attachments/assets/2a2091da-b6bf-4db3-8855-652467f656e9)

---

# 5. WBS / 요구사항 명세

<img width="1350" alt="스크린샷 2024-12-10 오전 10 30 37" src="https://github.com/user-attachments/assets/49333eb7-c874-4152-bf26-79a39e5d70bc">

📺 [WBS](https://docs.google.com/spreadsheets/d/1BJ6unqhGYQMf1ZQvwPXxzdWcs8Djv224Yi5jLE-sD3M/edit?gid=1991800281#gid=1991800281)

🙏🏻 [요구사항 명세](https://docs.google.com/spreadsheets/d/1mYRyznCwWFqOqJjJhN7t6W8J8CqahkIvknfgWDqXuYs/edit?gid=0#gid=0)


# 6. 개발 환경

- OS : Mac
- IDE : JetBrain IntelliJ

| Software                      | 세부 Spec 사양 (Version)                                  |
|-------------------------------|-------------------------------------------------------|
| Java                          | jdk 21                                                |
| Spring Boot                   | 3.3.5                                                 |
| Spring Boot Libraries         | Oauth2, Security, Lombok, MySQL Connector, Swagger v3 |
| SonarQube                     | 5.1.0.4882                                            |
| AWS                           | awssdk:2.29.20                                        |
| QueryDSL                      | JPA: 5.0.0                                            |
| Redisson                      | 3.37.0                                                |
| JJWT                          | 0.12.6                                                |
| Elasticsearch/Kibana/Logstash | 8.16.1                                                |
| Redis                         | spring-boot-starter-data-redis                        |

***

## 기술스택

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">   <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"><img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"/>  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">  <img src="https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white">  <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white">  <img src="https://img.shields.io/badge/Elasticsearch-005571?style=for-the-badge&logo=Elasticsearch&logoColor=white">  <img src="https://img.shields.io/badge/Logstash-005571?style=for-the-badge&logo=Logstash&logoColor=white">  <img src="https://img.shields.io/badge/Kibana-005571?style=for-the-badge&logo=Kibana&logoColor=white">  <img src="https://img.shields.io/badge/SonarQube-4E9BCD?style=for-the-badge&logo=SonarQube&logoColor=white">  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">  <img src="https://img.shields.io/badge/Yarn-2C8EBB?style=for-the-badge&logo=Yarn&logoColor=white">
