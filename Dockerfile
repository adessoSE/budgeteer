FROM docker.io/openjdk:11.0.15-jdk AS build
COPY . /build
WORKDIR /build
RUN ./gradlew budgeteer-web-interface:bootWar -DmainBranch=HEAD

FROM docker.io/openjdk:11.0.15-jre
EXPOSE 8080

COPY --from=build /build/budgeteer-web-interface/build/libs/*.war /budgeteer.war

ENTRYPOINT ["java","-jar","/budgeteer.war"]
