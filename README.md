# flutter-spring-todoApp

# 프로젝트 개요

이 프로젝트는 Spring Boot 기반의 백엔드와 Flutter 기반의 프론트엔드로 구성된 애플리케이션입니다. 사용자 인증, 데이터 저장, 푸시 알림 등의 기능을 제공합니다.

# 프로젝트 개요

이 프로젝트는 Spring Boot 기반의 백엔드와 Flutter 기반의 프론트엔드로 구성된 애플리케이션입니다. 사용자 인증, 데이터 저장, 푸시 알림 등의 기능을 제공합니다.

## 기술 스펙 (Tech Stack)

### 백엔드 (Backend)
- **언어**: Java 17
- **프레임워크**: Spring Boot 3.3.2
- **빌드 도구**: Gradle
- **데이터베이스**: MySQL
- **ORM**: Spring Data JPA
- **인증 및 보안**:
  - Spring Security
  - Firebase Admin SDK
- **데이터 검증**: Spring Boot Starter Validation
- **캐싱 및 세션 관리**: Redis
- **알림 기능**:
  - Redis TTL 설정 후 만료 이벤트 (Keyspace Event) 이용
- **테스트**:
  - JUnit (Spring Boot Starter Test)
  - Spring Security Test

### 프론트엔드 (Frontend)
- **프레임워크**: Flutter
- **의존성 관리**: pubspec.yaml
- **주요 라이브러리**:
  - **HTTP 통신**: `http`
  - **인증**: `firebase_auth`, `google_sign_in`
  - **Firebase 연동**: `firebase_core`, `firebase_messaging`
  - **로컬 데이터 저장**:
    - `flutter_secure_storage` (보안 저장소)
    - `shared_preferences` (간단한 데이터 저장)
  - **UI 및 기능**:
    - `flutter_datetime_picker_plus` (날짜 선택기)
    - `flutter_local_notifications` (로컬 푸시 알림)

## 설치 및 실행 방법

### 백엔드 실행 방법
```sh
./gradlew clean build
```

### 프론트엔드 실행 방법
```sh
flutter run
```

## 주요 기능
- 사용자 로그인 및 인증 (OAuth2 및 Firebase)
- 할 일 생성 (알림 설정 포함)
- 할 일 조회
- 할 일 수정
- 할 일 삭제
- 포그라운드 알림 기능
- 백그라운드 알림 기능

## 남은 기능
- 할 일 무한 스크롤
- 할 일 초성 검색
- 캘린더로 할 일 조회

## ERD
<img src="https://github.com/user-attachments/assets/ebdfa1c6-6268-4426-b1e9-6a329cfb23e7" width="600">

## 메인
<img src="https://github.com/user-attachments/assets/af562665-08b6-41c2-97ba-612595285adb" width="400">

## 할일 상세 페이지
<img src="https://github.com/user-attachments/assets/3f7958cd-963b-48a1-9a89-29d5e39d70df" width="400">

## 할일 수정 페이지
<img src="https://github.com/user-attachments/assets/3c5d3f2e-2045-43b0-94de-d58cd123f3f1" width="400">
<img src="https://github.com/user-attachments/assets/4094bbac-3c13-4cb6-8700-6162cd7b2d6a" width="400">

## 할일 추가 페이지
<img src="https://github.com/user-attachments/assets/600e4fd5-4fe3-491b-9abd-20951ee2f744" width="400">
<img src="https://github.com/user-attachments/assets/096c6589-03af-4ef4-83e7-be99c7cd4d86" width="400">

## 로그인(Spring security, firebase auth)
<img src="https://github.com/user-attachments/assets/8985cc61-dc77-4f56-b244-3234ad1da8f3" width="400">


## 알림(firebase cloud messaging, redis ttl)
### 앱이 포그라운드 상태일 때 알림 발생
[포그라운드 알림.webm](https://github.com/user-attachments/assets/f939cfaf-6945-4d5c-bb5b-38094f5a8735)

### 앱이 백그라운드 상태일 때 알림 발생
[백그라운드 알림.webm](https://github.com/user-attachments/assets/282a2e35-c054-4c1e-8e03-4581a37307ef)

## 서버,클라이언트 에러 페이지
<img src="https://github.com/user-attachments/assets/dd4d6a1c-37a4-4c20-839e-ddd7fdfa19e6" width="400">
<img src="https://github.com/user-attachments/assets/2f319230-17c4-48f8-b97d-9e2ef29830b5" width="400">
