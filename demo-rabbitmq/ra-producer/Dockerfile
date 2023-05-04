FROM openjdk:8
COPY ./ra-producer/target/ra-producer-0.0.1-SNAPSHOT.jar /app.jar
CMD ["--server.port=18181"]
EXPOSE 18181

ENTRYPOINT ["java", "-jar", "/app.jar"]