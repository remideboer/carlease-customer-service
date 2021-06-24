FROM adoptopenjdk/openjdk11:latest as BUILDER
# copy stuff from project into container
COPY . /build
RUN cd build/ && ./mvnw clean package

FROM adoptopenjdk/openjdk11:latest as carlease-customer-service
EXPOSE 9092
COPY --from=BUILDER build/target/customer-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

