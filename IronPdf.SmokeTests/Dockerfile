#
# Build stage
#
FROM maven:3.8.6-openjdk-18 AS build
COPY src /home/app/src
COPY pom.xml /home/app
# This will test before package
# Add -DskipTests if you want to skip test
RUN mvn --batch-mode -f /home/app/pom.xml package assembly:single

#
# Package stage
#
FROM openjdk:18
COPY --from=build /home/app/target/IronPdf.SmokeTests-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/lib/IronPdf.SmokeTests.jar
# Run jar when build
RUN java -jar /usr/local/lib/IronPdf.SmokeTests.jar
# Run jar when docker run
#ENTRYPOINT ["java","-jar","/usr/local/lib/IronPdf.SmokeTests.jar"]
