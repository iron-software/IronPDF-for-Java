#
# Base Linux image - currently using amazoncorretto 17
#
FROM amazoncorretto:17

#
# Option 1 : Build the Jar inside docker
# Build stage - you can skip this by manually copy the precompiled Jar instead.
#
RUN yum update -y
RUN yum install -y openjdk-11-jdk maven
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -X -f /home/app/pom.xml clean package

#
# Option 2 : Build the Jar outside and copy to docker
#COPY ironpdf-docker-example-1.0-SNAPSHOT.jar /home/app/target/ironpdf-docker-example-1.0-SNAPSHOT.jar
#

#
# Install required packages
# Which packages to install depends on which Linux distro is used
# For this example, we are using amazoncorretto:17 so these packages are specific to this distro.
# For information other distro's required packages please visit https://ironpdf.com/docs/questions/linux/
#
#RUN apt-get update -y
RUN yum install -y pango.x86_64 libXcomposite.x86_64 libXcursor.x86_64 libXdamage.x86_64 libXext.x86_64 libXi.x86_64 libXtst.x86_64 cups-libs.x86_64 libXScrnSaver.x86_64 libXrandr.x86_64 GConf2.x86_64 alsa-lib.x86_64 atk.x86_64 gtk3.x86_64 ipa-gothic-fonts xorg-x11-fonts-100dpi xorg-x11-fonts-75dpi xorg-x11-utils xorg-x11-fonts-cyrillic xorg-x11-fonts-Type1 xorg-x11-fonts-misc glibc-devel.x86_64 at-spi2-atk.x86_64 mesa-libgbm.x86_64 libxkbcommon amazon-linux-extras libgdiplus

#
# Start the app
#
ENTRYPOINT ["java","-jar","/home/app/target/ironpdf-docker-example-1.0-SNAPSHOT.jar"]