# How to Run IronPDF for Java in Google Cloud
Note: We have not fully tested support with Google Cloud but if you want to try, we have some `Required Settings`.


# Important: Required Settings
* Zip Deployment is not supported, since IronPDF requires execution of binaries at runtime.
* Default Cloud Function Docker images is not working because it lacks of required packages required for Chrome to run properly. https://cloud.google.com/functions/docs/reference/system-packages
* Please use a custom Dockerfile and install all required packages. Learn more about this here: https://ironpdf.com/docs/questions/linux/
* Include the `ironpdf-engine-linux-x64` dependency to your project: (change the version number to latest)
```xml
 <dependency>
    <groupId>com.ironsoftware</groupId>
    <artifactId>ironpdf-engine-linux-x64</artifactId>
    <version>2022.xx.x</version>
</dependency>
```
* This plugin **may** be required:
```xml
 <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.2.4</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>

                        <!--  required for Docker-->
                        <configuration>
                            <transformers>
                                <transformer
                                        implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                            </transformers>
                        </configuration>

                    </execution>
                </executions>
            </plugin>
```
* These dependencies **may** be required:
```xml
<dependency>
    <groupId>io.perfmark</groupId>
    <artifactId>perfmark-api</artifactId>
    <version>0.26.0</version>
</dependency>

<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-okhttp</artifactId>
    <version>1.50.2</version>
</dependency>

<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-netty-shaded</artifactId>
    <version>1.50.2</version>
</dependency>
``` 

* Set `timeout` to `330 seconds` due to slow start.
* Set `memory size` to at least `2048 MB`.
* Set `EphemeralStorage Size` to at least `1024 MB`.

* In some environments, you may need to point at an IronPdfEngineWorkingDirectory and set execution permissions for it:
```java
Setting.setIronPdfEngineWorkingDirectory(Paths.get("/tmp/"));
```
```Dockerfile
RUN chmod 777 /tmp/
```

