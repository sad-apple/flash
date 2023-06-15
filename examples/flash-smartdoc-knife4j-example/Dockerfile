FROM openjdk:17
COPY ./target/flash-smartdoc-knife4j-example-1.0.0-SNAPSHOT.jar /app.jar
CMD ["--server.port=8080", "mkdir /models"]
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]