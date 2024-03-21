#
# Base Linux image - currently using Debian 11.5 (bullseye)
#
FROM openjdk:18-slim-bullseye

#
# Option 1 : Build the Jar inside docker
# Build stage - you can skip this by manually copy the precompiled Jar instead.
#
RUN apt update -y
RUN apt install -y openjdk-11-jdk maven
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Option 2 : Build the Jar outside and copy to docker
#COPY ironpdf-docker-example-1.0-SNAPSHOT.jar /home/app/target/ironpdf-docker-example-1.0-SNAPSHOT.jar
#

#
# Install required packages
# Which packages to install depends on which Linux distro is used
# For this example, we are using Debian 11.5 (bullseye) so these packages are specific to this distro.
# For information other distro's required packages please visit https://ironpdf.com/docs/questions/linux/
#
#RUN apt-get update -y
RUN apt-get install -y libc6-dev libgtk2.0-0 libnss3 libatk-bridge2.0-0 libx11-xcb1 libxcb-dri3-0 libdrm-common libgbm1 libasound2 libxkbcommon-x11-0 libxrender1 libfontconfig1 libxshmfence1
RUN apt-get install -y software-properties-common
RUN add-apt-repository ppa:ubuntu-toolchain-r/test
RUN apt-get install -y gcc-9 g++-9
RUN update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-9 90 --slave /usr/bin/g++ g++ /usr/bin/g++-9 --slave /usr/bin/gcov gcov /usr/bin/gcov-9
RUN apt-get install -y xvfb libva-dev libgdiplus

#
# Start the app
#
ENTRYPOINT ["java","-jar","/home/app/target/ironpdf-docker-example-1.0-SNAPSHOT.jar"]