FROM docker.io/eclipse-temurin:17-jdk AS build
COPY . /build
WORKDIR /build
RUN ./gradlew budgeteer-web-interface:bootWar -DmainBranch=HEAD

FROM docker.io/eclipse-temurin:17-jre
EXPOSE 8080

COPY --from=build /build/budgeteer-web-interface/build/libs/*.war /budgeteer.war

ENTRYPOINT ["java", "-Dwicket.ioc.useByteBuddy=true", "-jar","/budgeteer.war"]
