FROM openjdk:17-jdk-alpine
RUN mkdir -p /usr/src/atipera/backend
WORKDIR /usr/src/atipera/backend
COPY . /usr/src/atipera/backend

ENV TOKEN=${TOKEN}

RUN ./mvnw package
RUN rm -rf /usr/src/atipera/backend/src

ENTRYPOINT ["java","-jar","target/Atipera-0.0.1-SNAPSHOT.jar"]