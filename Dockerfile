FROM maven:3-jdk-12-alpine
COPY . /opt/app
ENV MAVEN_CONFIG=/tmp/
RUN mvn install -Duser.home=$MAVEN_CONFIG -f /opt/app/pom.xml
EXPOSE 8080
CMD ["java","-jar","/opt/app/target/app.jar"]