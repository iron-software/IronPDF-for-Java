# How to Run IronPDF for Java in an Azure Function
* IronPDF for Java only supports Docker deployment.
* Zip Deployment is not supported, since **IronPDF requires execution of binaries at runtime.**

1. Follow this Microsoft Official guide 
    * https://learn.microsoft.com/en-us/azure/azure-functions/functions-create-function-linux-custom-image
    * For `Choose a programming language` -> select `Java`
    * Follow the guide until your app is up and running.
2. Add the IronPDF dependency
   * Add this to your pom with the latest `<version>`
```xml
   <dependencies>
      <dependency>
            <groupId>com.ironsoftware</groupId>
            <artifactId>ironpdf</artifactId>
            <version>2022.xx.x</version>
        </dependency>
        <dependency>
            <groupId>com.ironsoftware</groupId>
            <artifactId>ironpdf-engine-linux-x64</artifactId>
            <version>2022.xx.x</version>
        </dependency>
    </dependencies>
```
   * Note: `ironpdf-engine-linux` is **required** to run IronPDF in Docker.
3. Add a RenderPdf function
   * Add a new function in `Function.java`
   * This function will receive an URL and return a rendered PDF.
```java
  public class Function {
  
  //...
   @FunctionName("RenderPdf")
   public HttpResponseMessage renderPdf(
           @HttpTrigger(
                   name = "req",
                   methods = {HttpMethod.GET, HttpMethod.POST},
                   authLevel = AuthorizationLevel.ANONYMOUS)
           HttpRequestMessage<Optional<String>> request,
           final ExecutionContext context) {
       context.getLogger().info("Java HTTP trigger processed a request. (RenderPdf)");
       // Parse query parameter
       final String url = request.getQueryParameters().get("url");
       if (url == null) {
           return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a url on the query string").build();
       } else {
           context.getLogger().info("IronPDF try to render url: " + url);
           PdfDocument pdfDocument = com.ironsoftware.ironpdf.PdfDocument.renderUrlAsPdf(url);
           byte[] content = pdfDocument.getBinaryData();
           return request.createResponseBuilder(HttpStatus.OK)
                   .body(content)
                   .header("Content-Disposition", "attachment; filename=ironpdf_result.pdf")
                   .build();
       }
   }
  }
```
4. Update Dockerfile
   * Add the IronPdf Linux required packages. 
   From the example the base Docker image is `mcr.microsoft.com/azure-functions/java:4-java$JAVA_VERSION-build`, which is `Debian 11`
   So we need to add these package to the docker file.
   ```dockerfile
   RUN apt update \
   && apt install -y libgdiplus libxkbcommon-x11-0 libc6 libc6-dev libgtk2.0-0 libnss3 libatk-bridge2.0-0 libx11-xcb1 libxcb-dri3-0 libdrm-common libgbm1 libasound2 libxrender1 libfontconfig1 libxshmfence1
   RUN apt-get install -y xvfb libva-dev libgdiplus
   ```
     * for other Linux distros please see https://ironpdf.com/docs/questions/linux/

5. Redeploy your function to Azure
   1. Build & package `mvn clean package`
   2. Build Docker image e.g. `docker build --tag <DOCKER_ID>/azurefunctionsimage:v1.0.0 .`
   3. Push Docker image e.g. `docker push <DOCKER_ID>/azurefunctionsimage:v1.0.0`
   4. Update Azure function e.g. ` az functionapp create --name <APP_NAME> --storage-account <STORAGE_NAME> --resource-group AzureFunctionsContainers-rg --plan myPremiumPlan --deployment-container-image-name <DOCKER_ID>/azurefunctionsimage:v1.0.0`
6. Enjoy IronPdf
   * Trigger function at:`https://<APP_NAME>.azurewebsites.net/api/RenderPdf?url=https://www.google.com`
   * Note: The first time function was triggered, It may slow or fail due to initializing, but after that it will be fine.
