FROM maven:3.6.3-openjdk-11-slim as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve
COPY src src
RUN mvn package
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM adoptopenjdk/openjdk11
VOLUME /tmp
ARG DEPENDENCY=/app/target/dependency
WORKDIR /app
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib lib
COPY --from=build ${DEPENDENCY}/META-INF META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes .
ENTRYPOINT ["java","-cp","/app:/app/lib/*","com.zenika.kata.knister.KnisterApplicationKt"]
