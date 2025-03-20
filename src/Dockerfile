# Java 17 런타임 이미지
FROM openjdk:17-jdk-alpine

# 타임존 설정 (옵션)
ENV TZ=Asia/Seoul

# JAR 파일을 컨테이너에 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 포트 오픈
EXPOSE 8080

# 앱 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
