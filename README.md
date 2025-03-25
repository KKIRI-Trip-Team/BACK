# KKIRI-TRIP(끼리트립)
## 🚀 기술 스택

- **🛠️ Build Tool** : Gradle  
- **🌱 Spring Boot** : 3.4.4  
- **☕ Java** : 17  

## 패키지 구조 설계 - 도메인형 패키지 구조
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
- api 패키지 : 외부에 제공할 api 구현
- domain 패키지 : 애플리케이션 핵심 비즈니스 로직와 관련된 클래스
- global : 애플리케이션 전반에 사용되는 클래스
    - config 패키지 : 각종 설정 클래스
    - error 패키지 : 예외 처리 및 예외 관련 클래스
    - jwt 패키지 : JWT 기반의 인증 처리 관련 클래스
    - interceptor 패키지 : interceptor 클래스. 인증과 인가를 위한 인터셉터 구현
    - resolver 패키지 : resolver 클래스
    - util 패키지 : util 클래스
- infra : 아마존 파일 업로드, 이메일 전송, SMS 전송과 같은 외부 서비스에 대한 클래스
- web : kakao 토큰 발급 및 cors 설정 테스트 진행.


## 커밋 컨벤션
| **태그 이름**       | **설명**                               |
|--------------------|----------------------------------------|
| **Feat**             | 새로운 기능을 추가할 경우                 |
| **Fix**              | 버그를 고친 경우                         |
| **Design**           | CSS 등 사용자 UI 디자인 변경             |
| **!BREAKING CHANGE** | 커다란 API 변경이 있을 경우               |
| **!HOTFIX**          | 급하게 치명적인 버그를 고쳐야하는 경우       |
| **Style**            | 코드 포맷 변경, 세미 콜론 누락, 코드 수정이 없는 경우 |
| **Refactor**         | 프로덕션 코드 리팩토링                    |
| **Comment**          | 필요한 주석 추가 및 변경                  |
| **Docs**             | 문서를 수정할 경우                        |
| **Test**             | 테스트 추가, 테스트 리팩토링(프로덕션 코드 변경 X) |
| **Chore**            | 빌드 테스트 업데이트, 패키지 매니저를 설정하는 경우(프로덕션 코드 변경 X) |
| **Rename**           | 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우   |
| **Remove**           | 파일을 삭제하는 작업만 수행한 경우            |

