
# Example project: Running IronPDF for Java in Docker

## Quick start

1. run `docker build . -t my-ironpdf`
2. run `docker run my-ironpdf`
3. Enjoy IronPDF!

## How it works

### Base Docker image.

In this Example we use `openjdk:18-slim-bullseye` which is `jdk:18` + `Debian 11`.
You can use other base images that suit following requirements:

* JRE 8+ need to match your app minimum requirement.
* JDK 8+, (optional) if your want to build JAR file in docker.
* Maven (optional) or other compiler if your want to compile your code in docker.
* Required packages: IronPdf requires some packages to run and which packages depends on distro being used.
    * In this example we use Debian 11 which requires:
      `libc6-dev` `libgtk2.0-0` `libnss3` `libatk-bridge2.0-0` `libx11-xcb1` `libxcb-dri3-0`  `libdrm-common`
      `libgbm1` `libasound2` `libxkbcommon-x11-0` `libxrender1` `libfontconfig1`
      `libxshmfence1` `xvfb` `libva-dev` and `libgdiplus`
      (some of this may have been included already in the base image)
    * For Centos 8, you will require:
      `glibc-devel` `nss` `at-spi2-atk` `libXcomposite` `libXrandr` `mesa-libgbm` `alsa-lib`
      `pango` `cups-libs` `libXdamage` `libxshmfence`
    * For other distros please visit https://ironpdf.com/docs/questions/linux/ to learn more information.
* Permission to read write and execute the binaries. (`chmod 777` may be needed)

### JAR file

We need an executable fat JAR (JAR file that includes all dependencies and is ready to execute) to run in docker.

To build a fat JAR with Maven please add the following plugins:
```xml
<plugins>
    <plugin>
        <!-- Build an executable JAR -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>3.1.0</version>
        <configuration>
            <archive>
                <manifest>
                    <addClasspath>true</addClasspath>
                    <classpathPrefix>lib/</classpathPrefix>
                    <mainClass>org.example.Main</mainClass>
                </manifest>
            </archive>
        </configuration>
    </plugin>
    <plugin>
        <!-- Build fat Jar -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.2.4</version>
        <executions>
            <execution>
                <phase>package</phase>
                <goals>
                    <goal>shade</goal>
                </goals>

                <!-- Required! for running IronPdf in Docker -->
                <configuration>
                    <transformers>
                        <transformer
                                implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                    </transformers>
                </configuration>

            </execution>
        </executions>
    </plugin>
</plugins>
```

### Add JAR file to Docker
You can build a JAR file from POM inside docker by adding this to you docker file:
```dockerfile
RUN apt update -y
RUN apt install -y openjdk-11-jdk maven
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
```
*Or* if you **already have a JAR file** you can just put it into the docker with:
```dockerfile
COPY ironpdf-docker-example-1.0-SNAPSHOT.jar /home/app/target/ironpdf-docker-example-1.0-SNAPSHOT.jar
```

#### Set entry point
```dockerfile
ENTRYPOINT ["java","-jar","/home/app/target/ironpdf-docker-example-1.0-SNAPSHOT.jar"]
```