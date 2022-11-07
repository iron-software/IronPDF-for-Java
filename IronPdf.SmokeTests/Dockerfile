#
# Build stage
#
FROM maven:3-openjdk-18-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN apt-get update -y
RUN apt-get install -y libc6-dev libgtk2.0-0 libnss3 libatk-bridge2.0-0 libx11-xcb1 libxcb-dri3-0 libdrm-common libgbm1 libasound2 libxkbcommon-x11-0 libxrender1 libfontconfig1 libxshmfence1
RUN apt-get install -y software-properties-common fonts-noto fonts-ancient-scripts gsfonts
RUN add-apt-repository ppa:ubuntu-toolchain-r/test
RUN apt-get install -y gcc-9 g++-9
RUN update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-9 90 --slave /usr/bin/g++ g++ /usr/bin/g++-9 --slave /usr/bin/gcov gcov /usr/bin/gcov-9
RUN apt-get install -y xvfb libva-dev libgdiplus
# This will test before package
# Add -DskipTests if you want to skip test
RUN mvn --batch-mode -f /home/app/pom.xml package assembly:single -DskipTests

#
# Package stage
#
FROM openjdk:18-jdk-slim
COPY --from=build /home/app/target/IronPdf.SmokeTests-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/lib/IronPdf.SmokeTests.jar
RUN apt-get update -y
RUN apt-get install -y libc6-dev libgtk2.0-0 libnss3 libatk-bridge2.0-0 libx11-xcb1 libxcb-dri3-0 libdrm-common libgbm1 libasound2 libxkbcommon-x11-0 libxrender1 libfontconfig1 libxshmfence1
RUN apt-get install -y software-properties-common fonts-noto fonts-ancient-scripts gsfonts
RUN add-apt-repository ppa:ubuntu-toolchain-r/test
RUN apt-get install -y gcc-9 g++-9
RUN update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-9 90 --slave /usr/bin/g++ g++ /usr/bin/g++-9 --slave /usr/bin/gcov gcov /usr/bin/gcov-9
RUN apt-get install -y xvfb libva-dev libgdiplus
# Run jar when build
RUN java -jar /usr/local/lib/IronPdf.SmokeTests.jar
# Run jar when docker run
#ENTRYPOINT ["java","-jar","/usr/local/lib/IronPdf.SmokeTests.jar"]
