FROM openjdk:17-slim

RUN apt-get update && apt-get install -y tzdata \
    && ln -fs /usr/share/zoneinfo/Asia/Seoul /etc/localtime \
    && dpkg-reconfigure -f noninteractive tzdata \
    && mkdir -p /var/log/springboot \
    && chmod -R 777 /var/log/springboot \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENV TZ=Asia/Seoul
ENV LOGGING_PATH=/var/log/springboot

ENTRYPOINT ["java","-Dlogging.file.path=/var/log/springboot","-jar","/app.jar"]
