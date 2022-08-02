FROM openjdk:18-jdk-alpine as build

ENV APP_HOME=/root/

WORKDIR $APP_HOME
ADD gradle gradle
ADD build.gradle build.gradle
ADD gradlew gradlew
ADD src src
RUN ./gradlew clean build

FROM openjdk:18-alpine
WORKDIR /root/
COPY --from=build /root/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", \
     "-XX:+UnlockExperimentalVMOptions", \
     "--enable-preview", \
     "-Dspring.profiles.active=default,docker", \
     "-jar", \
     "app.jar"]