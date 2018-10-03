FROM openjdk:8
ADD target/fileupload-microservice.jar fileupload-microservice.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "fileupload-microservice.jar"]