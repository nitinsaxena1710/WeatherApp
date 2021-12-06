FROM openjdk:8
ADD target/weather-App-Springboot.jar weather-App-Springboot.jar 
EXPOSE 8080 
ENTRYPOINT ["java","-jar","weather-App-Springboot.jar"]