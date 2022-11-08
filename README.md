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

### Define IronPDF for Java Dependency

define IronPDF dependency in your pom.xml as follows:

```xml  
<dependencies>
    <dependency>
        <groupId>com.ironsoftware</groupId>
        <artifactId>ironpdf</artifactId>
        <version>20xx.xx.xxxx</version>
    </dependency>
</dependencies>
```
### Write the code

Once Defined, you can get started by adding `import com.ironsoftware.ironpdf.*` to the top of your Java code. Here is
a sample HTML to PDF example to get started:

```java 
  PdfDocument pdfDocument = PdfDocument.renderHtmlAsPdf("<h1> ~Hello World~ </h1> Made with IronPDF!");
  pdfDocument.saveAs(Paths.get("html_saved.pdf"));
```

And another option is to create from URL to PDF:

```java 
PdfDocument pdfDocument = PdfDocument.renderUrlAsPdf("https://ironpdf.com/java");
pdfDocument.saveAs(Paths.get("url_saved.pdf"));
```

### Logging
IronPdf Java using [slf4j](https://www.slf4j.org/) logger. 

To enable log 
```java 
com.ironsoftware.ironpdf.Settings.setDebug(true);
```
Set IronPdfEngine log path
```java 
com.ironsoftware.ironpdf.Settings.setLogPath(Paths.get("C:/tmp/myIronPdfEngineLog.log"));
```

Note: please set before cally any IronPDF function.

#### Not familiar with slf4j?
Just add this to your pom.xml.
```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>2.0.3</version>
</dependency>
```

## About IronPDF for Java

`IronPdf for Java` is based on `IronPdf for .Net.` (so it will be 1 version behind)

IronPdf for Java using gRPC to talked to `IronPdfEngine`, which
consumed `IronPdf for .Net`

`IronPdfEngine` binaries will be downloaded automatically when you run the project for the first time.
`IronPdfEngine` process will start when you call any IronPdf function for the first time, and stop when your application
is closed, or when it was in idle stage.

## Licensing & Support Available

`IronPdf for Java` is free to use with watermark. To remove watermark [learn more](https://ironpdf.com/licensing/).

### Apply License

```java 
com.ironsoftware.ironpdf.License.setLicenseKey("YOUR-LICENSE-KEY");
```
Note: please set before cally any IronPDF function.

For our full list of code examples, tutorials, licensing information, and documentation
visit: [https://ironpdf.com/java](https://ironpdf.com/java)

For more support and inquiries, please email us at: developers@ironsoftware.com
