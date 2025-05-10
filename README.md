# BACK
사이드프로젝트 백입니다!
build tool : Gradle
SpringBoot : 3.4.4
JAVA 17


# 패키지 구조 설계 - 도메인형 패키지 구조
```
com
└── app
    ├── api
    │   └── apis...
    ├── domain
    │   ├── common
    │   └── domains...
    ├── global
    │   ├── config
    │   ├── error
    │   ├── jwt
    │   ├── interceptor
    │   ├── resolver
    │   └── util
    ├── infra
    └── web
```
• api 패키지 : 외부에 제공할 api 구현
• domain 패키지 : 애플리케이션 핵심 비즈니스 로직와 관련된 클래스
• global : 애플리케이션 전반에 사용되는 클래스
 - config 패키지 : 각종 설정 클래스
 - error 패키지 : 예외 처리 및 예외 관련 클래스
 - jwt 패키지 : JWT 기반의 인증 처리 관련 클래스
 - interceptor 패키지 : interceptor 클래스. 인증과 인가를 위한 인터셉터 구현
 - resolver 패키지 : resolver 클래스
 - util 패키지 : util 클래스
• infra : 아마존 파일 업로드, 이메일 전송, SMS 전송과 같은 외부 서비스에 대한 클래스
• web : kakao 토큰 발급 및 cors 설정 테스트 진행.
