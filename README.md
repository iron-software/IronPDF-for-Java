![Apache Maven](https://img.shields.io/maven-central/v/com.ironsoftware/ironpdf)  ![Passed](https://img.shields.io/badge/build-%20%E2%9C%93%203158%20tests%20passed%20(0%20failed)%20-107C10?logo=visualstudio)  [![windows](https://img.shields.io/badge/%E2%80%8E%20-%20%E2%9C%93-107C10?logo=windows)](https://ironpdf.com/python/docs/questions/installation/?utm_source=nuget&utm_medium=organic&utm_campaign=readme&utm_content=topshield) [![macOS](https://img.shields.io/badge/%E2%80%8E%20-%20%E2%9C%93-107C10?logo=apple)](https://ironpdf.com/python/docs/questions/macos/?utm_source=nuget&utm_medium=organic&utm_campaign=readme&utm_content=topshield) [![linux](https://img.shields.io/badge/%E2%80%8E%20-%20%E2%9C%93-107C10?logo=linux&logoColor=white)](https://ironpdf.com/python/docs/questions/linux/?utm_source=nuget&utm_medium=organic&utm_campaign=readme&utm_content=topshield) [![docker](https://img.shields.io/badge/%E2%80%8E%20-%20%E2%9C%93-107C10?logo=docker&logoColor=white)](https://ironpdf.com/python/docs/questions/docker-linux/?utm_source=nuget&utm_medium=organic&utm_campaign=readme&utm_content=topshield) [![aws](https://img.shields.io/badge/%E2%80%8E%20-%20%E2%9C%93-107C10?logo=amazonaws)](https://ironpdf.com/python/docs/questions/creating-pdfs-csharp-amazon-aws-lambda/?utm_source=nuget&utm_medium=organic&utm_campaign=readme&utm_content=topshield) [![microsoftazure](https://img.shields.io/badge/%E2%80%8E%20-%20%E2%9C%93-107C10?logo=microsoftazure)](https://ironpdf.com/python/docs/questions/azure/?utm_source=nuget&utm_medium=organic&utm_campaign=readme&utm_content=topshield) [![livechat](https://img.shields.io/badge/Live%20Chat:-24/5-purple?logo=googlechat&logoColor=white)](https://ironpdf.com/python/?utm_source=nuget&utm_medium=organic&utm_campaign=readme&utm_content=topshield#helpscout-support)

# IronPDF for Java - Create, Edit, and Read PDFs in Java Applications
___

IronPdf is a library developed and maintained by Iron Software that helps
Software Engineers to create, edit and extract PDF content in projects in Java.

## IronPDF excels at:

- Generating PDFs from: HTML, URL, JavaScript, CSS and many image formats
- Adding headers/footers, signatures, attachments, and passwords and security
- Performance optimization: Full Multithreading and Async support
- And many more! Visit our website to see all our code examples and
  a [full list of our 50+ features](https://ironpdf.com/java/features/?utm_source=nuget&utm_medium=organic&utm_campaign=readme&utm_content=featureslist)*

## Using IronPDF

### Define IronPDF as a Java Dependency

To define IronPDF as a dependency, please add the following excerpt to your pom.xml:

```xml  
<dependencies>
  <dependency>
    <groupId>com.ironsoftware</groupId>
    <artifactId>ironpdf</artifactId>
    <version>20xx.xx.xxxx</version>
  </dependency>
</dependencies>
```
### Start writing Java code

Once the dependence is defined, you can get started by adding the `import com.ironsoftware.ironpdf.*` statement to the top of your Java code. Here is
a sample HTML to PDF example to get started:

```java
PdfDocument pdfDocument = PdfDocument.renderHtmlAsPdf("<h1> ~Hello World~ </h1> Made with IronPDF!");
        pdfDocument.saveAs(Paths.get("html_saved.pdf"));
```

This is another example which demonstrates URL to PDF:

```java 
PdfDocument pdfDocument = PdfDocument.renderUrlAsPdf("https://ironpdf.com/java");
        pdfDocument.saveAs(Paths.get("url_saved.pdf"));
```

### Settings

Note: Please note that all settings, logging, and licensing operations must be executed before any IronPDF methods are called.

#### Applying License Key

```java 
com.ironsoftware.ironpdf.License.setLicenseKey("YOUR-LICENSE-KEY");
```

#### Logging

IronPdf Java uses the [slf4j](https://www.slf4j.org/) logger. To enable logging use:
```java 
com.ironsoftware.ironpdf.Settings.setDebug(true);
```
To specify the IronPdfEngine log path add:
```java 
com.ironsoftware.ironpdf.Settings.setLogPath(Paths.get("C:/tmp/myIronPdfEngineLog.log"));
```

Not familiar with slf4j? Just add this to your `pom.xml`:
```xml
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-simple</artifactId>
  <version>2.0.17</version>
</dependency>
```

## About IronPDF for Java

`IronPDF for Java` is based on `IronPDF for .NET`, it will generally be about 1 version behind.

IronPDF for Java uses gRPC to communicate with the `IronPdfEngine`, which consumes `IronPDF for .NET`

## About IronPDF Engine

`IronPdfEngine` is the core of IronPdf. To use `IronPdf for Java` `IronPdfEngine` binaries is required.

The `IronPdfEngine` process will start when you call any IronPdf function for the first time, and stop when your application is closed, or when it enters an idle stage.

By default `IronPdf for Java` will download `IronPdfEngine` binaries on the first run (or when it cannot find the binaries).

> Note: For macOS, `IronPdfEngine` binaries that download on the first run might not work in your system (Gatekeeper on macOS).
Please use `IronPdfEngine` as a maven dependency instead (next section)

### Install IronPDF Engine as a Maven Dependency

By adding IronPdfEngine as a Maven dependency, the binaries will be downloaded during the loading of dependencies:
- This approach will avoid a lengthy startup process, as the IronPdfEngine binaries will already be downloaded.
- Moreover, it will be beneficial for deployment setups that do not allow downloading from external sources.

Simply add one or multiple of the following code snippets to your **pom.xml** file if you are working on a multiplatform app:

#### For windows x64
```xml
<dependency>
  <groupId>com.ironsoftware</groupId>
  <artifactId>ironpdf-engine-windows-x64</artifactId>
  <version>20xx.xx.xxxx</version>
</dependency>
```

#### For windows x86
```xml
<dependency>
  <groupId>com.ironsoftware</groupId>
  <artifactId>ironpdf-engine-windows-x86</artifactId>
  <version>20xx.xx.xxxx</version>
</dependency>
```

#### For Linux x64
```xml
<dependency>
  <groupId>com.ironsoftware</groupId>
  <artifactId>ironpdf-engine-linux-x64</artifactId>
  <version>20xx.xx.xxxx</version>
</dependency>
```

#### For macOS x64 (Intel)
```xml
<dependency>
  <groupId>com.ironsoftware</groupId>
  <artifactId>ironpdf-engine-macos-x64</artifactId>
  <version>20xx.xx.xxxx</version>
</dependency>
```

#### For macOS Arm (Apple Silicon)
```xml
<dependency>
  <groupId>com.ironsoftware</groupId>
  <artifactId>ironpdf-engine-macos-arm64</artifactId>
  <version>20xx.xx.xxxx</version>
</dependency>
```

## Licensing & Support Available

`IronPdf for Java` is free to use and test with an IronPDF watermark applied. To remove the watermark [apply for a license or trial license.](https://ironpdf.com/licensing/).

For our full list of code examples, tutorials, licensing information, and documentation
visit: [https://ironpdf.com/java](https://ironpdf.com/java)

For more support and inquiries, please email us at: support@ironsoftware.com