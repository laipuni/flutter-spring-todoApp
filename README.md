# 프로젝트 개요

flutter, spring으로 만든 간단한 할일 알림앱 입니다.

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
- 할 일 무한 스크롤
- 할 일 조건 검색 및 정렬

## 남은 기능
- 캘린더로 할 일 조회

## ERD
<img src="https://github.com/user-attachments/assets/ebdfa1c6-6268-4426-b1e9-6a329cfb23e7" width="600">

## 메인
<img src="https://github.com/user-attachments/assets/d3141f11-8c82-429a-a5c2-4f8117a0150b" width="300">

## 검색
### 검색
[할일 검색.webm](https://github.com/user-attachments/assets/62a4a5d0-8b45-402d-bae7-b095beb2baa4)

### <중요도_정렬>
[중요도_정렬.webm](https://github.com/user-attachments/assets/67cc01bf-1cb8-49e7-8e63-6884f812698a)

### <최신순 정렬>
[최신순_정렬.webm](https://github.com/user-attachments/assets/88e72e04-4949-477e-a7f0-307edad1e212)

### <무한 스크롤>
[무한 스크롤.webm](https://github.com/user-attachments/assets/12812a38-e8ec-432e-939b-5f40e10d8aed)

## 할일 상세 페이지
<img src="https://github.com/user-attachments/assets/a7ad5a33-bd90-4bb5-b937-a42c5d0a19e1" width="300">

## 할일 수정 페이지
<img src="https://github.com/user-attachments/assets/8b01dddb-86bb-437d-926e-2206df2dc8eb" width="300">
<img src="https://github.com/user-attachments/assets/5282cb9e-8d33-4068-bf18-f5284a704490" width="300">

## 할일 추가 페이지
<img src="https://github.com/user-attachments/assets/22c096d2-7021-4faf-922c-b4a7a50ccb9a" width="300">
<img src="https://github.com/user-attachments/assets/8d57cc2f-d74f-4b13-8a0c-4bc9d439312e" width="300">


## 로그인(Spring security, firebase auth)
<img src="https://github.com/user-attachments/assets/5f802797-f2da-4dcf-b353-243bef430279" width="300">

## 알림(firebase cloud messaging, redis ttl)
### 앱이 포그라운드 상태일 때 알림 발생
[포그라운드 알림.webm](https://github.com/user-attachments/assets/f939cfaf-6945-4d5c-bb5b-38094f5a8735)

### 앱이 백그라운드 상태일 때 알림 발생
[백그라운드 알림.webm](https://github.com/user-attachments/assets/282a2e35-c054-4c1e-8e03-4581a37307ef)

## 서버,클라이언트 에러 페이지
<img src="https://github.com/user-attachments/assets/dd4d6a1c-37a4-4c20-839e-ddd7fdfa19e6" width="300">
<img src="https://github.com/user-attachments/assets/2f319230-17c4-48f8-b97d-9e2ef29830b5" width="300">
