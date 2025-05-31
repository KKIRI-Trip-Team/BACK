# BACK
사이드프로젝트 백입니다!
build tool : Gradle
SpringBoot : 3.4.4
JAVA 17


# 패키지 구조 설계 - 도메인형 패키지 구조
```
com/
└── kkiri_trip/
    └── back/
        ├── api/
        │   ├── controller/
        │   └── dto/
        │       ├── chatMessage/   
        │       ├── dashboard/
        │       ├── feed/
        │       │   └── attribute/
        │       ├── feedUser/
        │       ├── image/
        │       │   ├── request/
        │       │   └── response/
        │       ├── place/
        │       ├── schedule/
        │       │   └── scheduleItem/
        │       └── user/
        │           ├── request/
        │           └── response/
        ├── domain/
        │   ├── feed/
        │   │   └── repository/
        │   ├── jpa/
        │   │   ├── common/
        │   │   │   └── entity/
        │   │   ├── dashboard/
        │   │   │   ├── entity/
        │   │   │   ├── repository/
        │   │   │   └── service/
        │   │   ├── feed/
        │   │   │   ├── entity/
        │   │   │   ├── repository/
        │   │   │   └── service/
        │   │   ├── feedUser/
        │   │   │   ├── entity/
        │   │   │   ├── repository/
        │   │   │   └── service/
        │   │   ├── image/
        │   │   │   └── service/
        │   │   ├── schedule/
        │   │   │   ├── entity/
        │   │   │   ├── repository/
        │   │   │   └── service/
        │   │   ├── scheduleItem/
        │   │   │   ├── entity/
        │   │   │   ├── repository/
        │   │   │   └── service/
        │   │   └── user/
        │   │       ├── entity/
        │   │       ├── repository/
        │   │       ├── service/        
        │   │       └── util/
        │   └── mongo/
        │       ├── chat/
        │       │   ├── entity/
        │       │   ├── repository/
        │       │   └── service/
        │       └── place/      
        │           ├── entity/
        │           ├── repository/
        │           └── service/
        ├── global/
        │   ├── chat/
        │   ├── common/
        │   │   └── dto/
        │   ├── config/
        │   │       ├── chat/
        │   │       ├── querydsl/
        │   │       └── web/
        │   ├── enums/
        │   ├── error/
        │   │       ├── errorcode/
        │   │       └── exception/
        │   └── jwt/
        └── infra/
```
• api 패키지 : 클라이언트에 노출되는 계층으로, Controller 및 요청/응답 DTO 포함
• domain 패키지 : 비즈니스 로직이 집중되는 계층. 저장소 타입별로 jpa/, mongo/ 구분
• global : 전역적으로 사용되는 설정 및 클래스
 - chat 패키지: 채팅 기능을 위한 전역 설정 클래스
 - common 패키지 : 전역적으로 공통 사용되는 DTO, 유틸리티 클래스
 - config 패키지 : 로젝트에서 사용하는 다양한 기술 설정 클래스
 - error 패키지 : 예외 처리 및 예외 관련 클래스
 - jwt 패키지 : JWT 기반의 인증 처리 관련 클래스
• infra : 아마존 파일 업로드, 이메일 전송, SMS 전송과 같은 외부 서비스에 대한 클래스
