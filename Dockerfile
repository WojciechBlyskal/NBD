FROM openjdk:11
COPY src/main/java/ /tmp
WORKDIR /tmp
CMD java org.example.Main