# KKIRI-TRIP(끼리트립)
## 🚀 기술 스택

- **🛠️ Build Tool** : Gradle  
- **🌱 Spring Boot** : 3.4.4  
- **☕ Java** : 17

 
## 🗂️ 패키지 구조 설계 - 도메인형 패키지 구조
```
com  
└── app  
    ├── api                # 🌐 외부에 제공할 API 구현  
    ├── domain             # 📦 핵심 비즈니스 로직 관련 클래스  
    │   ├── common  
    │   └── domains...  
    ├── global             # 🌍 애플리케이션 전반에 사용되는 클래스  
    │   ├── config         # ⚙️ 각종 설정 클래스  
    │   ├── error          # ❗ 예외 처리 및 관련 클래스  
    │   ├── jwt            # 🔒 JWT 기반 인증 처리 클래스  
    │   ├── interceptor    # 🛑 인증 및 인가 인터셉터 클래스  
    │   ├── resolver       # 🔄 Resolver 클래스  
    │   └── util           # 🛠️ 유틸리티 클래스  
    ├── infra              # ☁️ 외부 서비스 관련 클래스 (파일 업로드, 이메일 전송 등)  
    └── web                # 🌐 Kakao 토큰 발급 및 CORS 설정 테스트  
```

### 📜 상세 설명

#### `api` (🌐 외부에 제공할 API 구현)
- 외부 클라이언트가 호출하는 엔드포인트를 정의하는 컨트롤러 클래스들

#### `domain` (📦 핵심 비즈니스 로직 관련 클래스)
- **`common`**: 공통 엔티티, DTO, 상수, Enum 등  
- **`domains...`**: 각 도메인 별 핵심 비즈니스 로직 (예: `User`, `Order`, `Product` 등)  

#### `global` (🌍 애플리케이션 전반에 사용되는 클래스)
- **`config`** (⚙️ 설정 클래스): Spring Security, WebSocket, Kafka, Database 설정 등  
- **`error`** (❗ 예외 처리 관련): `CustomException`, `@ControllerAdvice` 전역 예외 처리  
- **`jwt`** (🔒 JWT 인증 관련): JWT 생성, 검증, 파싱 처리  
- **`interceptor`** (🛑 인증 및 인가 관련): 인증 및 로깅 처리를 위한 인터셉터  
- **`resolver`** (🔄 리졸버 관련): 커스텀 애노테이션 기반의 파라미터 바인딩  
- **`util`** (🛠️ 유틸리티 클래스): 문자열 처리, 날짜 변환 등 공통 로직  

#### `infra` (☁️ 외부 서비스 관련 클래스)
- 파일 업로드, 이메일 전송, 외부 API 연동 등 외부 인프라 서비스 처리  

#### `web` (🌐 Kakao 토큰 발급 및 CORS 설정 테스트)
- Kakao 소셜 로그인, CORS 설정 등 테스트 및 API 통신 관련 클래스  


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

