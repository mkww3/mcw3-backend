FROM gradle:7-jdk17 AS build

WORKDIR /porto
COPY . .

RUN gradle bootJar

FROM eclipse-temurin:17-jdk AS runtime

WORKDIR /porto
COPY --from=build /porto/build/libs/hackathon-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "hackathon-0.0.1-SNAPSHOT.jar"]
