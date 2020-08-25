FROM openjdk:8

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /app/oauth2.jar

CMD ["java","-jar","/app/oauth2.jar"]
