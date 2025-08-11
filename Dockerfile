FROM library/maven:3.9.11-eclipse-temurin-21-alpine
WORKDIR /build
COPY . /build/
RUN mvn package -DskipTests
FROM library/openjdk:21
WORKDIR /work
COPY --from=0 /build/target/movie-booking-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java","-Dspring.profiles.active=dev","-jar", "movie-booking-0.0.1-SNAPSHOT.jar"]

