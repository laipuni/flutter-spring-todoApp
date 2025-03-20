# Flutter SDK가 포함된 베이스 이미지
FROM cirrusci/flutter:latest

# 작업 디렉토리 생성
WORKDIR /app

# 프로젝트 복사
COPY . .

# 의존성 설치 (필요시)
RUN flutter pub get

# 포트 노출 (Flutter web-server 기본 포트)
EXPOSE 8000

# 컨테이너 시작 시 Flutter web-server 실행
CMD ["flutter", "run", "-d", "web-server", "--web-port=8000", "--web-hostname=0.0.0.0"]

