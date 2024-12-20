# <img width=25px src=https://github.com/user-attachments/assets/9b133f45-b09a-430b-bcc6-ae20c8934281> Smart IoT Toothbrush Application for Dogs, 양치코치
> Capstone Design, Fall 2024, Inha University <br>
> 인하대학교 종합설계프로젝트 - 대상 수상작  <br>
><br>
> Project Duration : 2024.09. ~ 2024.12. <br>
> 
<br>

# Server
Spring Boot, Docker 기반 서버 코드.

<br>

# ⚒️ Development Environment
- **Java 11** (OpenJDK 11)
- **Spring Boot**
- **MySQL 8** (AWS RDS)
- **Nginx**
- **Docker & Docker Compose**
- **Gradle**

<br>

# ⚙️ Dependencies
## Spring Boot Dependencies
- **Spring Web**
- **JPA**
- **MySQL Driver**
- **Spring Security** : JWT 인증
- **Swagger** : API 문서화
- **Hibernate**
- **Lombok**
- **KAKAO API**

## Infrastructure
- **Nginx** : 로드밸런싱용 
- **Docker** : 컨테이너화
- **MySQL** : AWS RDS

## Configuration
- **Database**: MySQL 8 

<br>

# 🚩 Implement Method

### 서버 실행
```bash
docker-compose up -d
```

### 도커 컨테이너 확인
```bash
docker ps
```

### 도커 Java Log 확인
```bash
docker logs -f [server_hash_number]
```

### 서버 종료
```bash
docker-compose down
```

<br>

# 📁 Client Folder Structure

```
📁 PetToothPairy
.
├── Dockerfile
├── build.gradle
├── docker-compose.yml
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── nginx.conf
├── run.sh
├── settings.gradle
├── sql
│   └── init.sql
└── src
    └── main
        ├── java
        │   ├── SwaggerConfiguration.java
        │   └── com
        │       └── example
        │           └── server
        │               ├── DemoApplication.java
        │               ├── Response
        │               │   ├── BaseResponse.java
        │               │   ├── CException.java
        │               │   ├── CExceptionHandler.java
        │               │   ├── ErrorBase.java
        │               │   └── SuccessBase.java
        │               ├── controller
        │               │   ├── HomeController.java
        │               │   ├── LoginController.java
        │               │   ├── RefreshTokenController.java
        │               │   ├── RegisterController.java
        │               │   ├── ToothController.java
        │               │   └── UserController.java
        │               ├── jwt
        │               │   └── JwtTokenService.java
        │               ├── model
        │               │   ├── Token.java
        │               │   ├── ToothDataAnalyzer.java
        │               │   ├── User.java
        │               │   ├── UserId.java
        │               │   └── UserPet.java
        │               ├── repository
        │               │   └── UserRepository.java
        │               ├── resttemplate
        │               │   └── RestTemplateConfig.java
        │               └── service
        │                   ├── HomeService.java
        │                   ├── InvalidTokenService.java
        │                   ├── LoginService.java
        │                   ├── RefreshTokenService.java
        │                   ├── RegisterService.java
        │                   ├── ToothService.java
        │                   └── UserService.java
        └── resources
            ├── application-dev.yml
            ├── application-prod.yml
            └── application.yml
```
