FROM gradle:4.7.0-jdk8-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootRepackage --no-daemon

FROM openjdk:8-jre-slim
EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/budgeteer-web-interface/build/libs/*.war /app/budgeteer.war

ENTRYPOINT ["java","-jar","/app/budgeteer.war"]