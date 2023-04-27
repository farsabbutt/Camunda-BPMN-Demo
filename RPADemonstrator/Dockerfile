#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
ENV HOME=/build
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN --mount=type=cache,target=/root/.m2 mvn -f $HOME/pom.xml install

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /build/target/rpa-demonstrator-api.jar /app/rpa-demonstrator-api.jar
ENTRYPOINT ["java","-jar","/app/rpa-demonstrator-api.jar"]