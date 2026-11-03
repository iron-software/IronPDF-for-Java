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

### Managing the IronPdfEngine lifecycle (restart / health check)

For most applications the engine lifecycle is fully automatic and you never need
to manage it. Long-running services, however, may need to recover when the engine
is interrupted by something outside IronPDF's control, e.g. a remote IronPdfEngine
host restart, an OS `kill`, or a native crash. After such an event the cached
connection becomes stale and further calls can hang or fail.

`com.ironsoftware.ironpdf.IronPdfEngineManager` provides a small, thread-safe API
for this:

| Method | Description |
| --- | --- |
| `IronPdfEngineManager.isEngineActive()` | Health check. Returns `true` only if the engine is connected and answers a handshake. Never starts the engine. Non-blocking against a running engine (a single 5s-deadline handshake); if a connect/restart is in progress on another thread, it waits for that to finish. |
| `IronPdfEngineManager.startEngine()` | Starts/connects the engine (same as the implicit first-call behaviour). No-op if already healthy. |
| `IronPdfEngineManager.stopEngine()` | Stops the local subprocess (or closes the channel in remote modes). |
| `IronPdfEngineManager.restartEngine()` | Stops then re-establishes a fresh connection. The recommended recovery action. It resets the stale state that a plain call would not. |

A typical watchdog pattern:

```java
import com.ironsoftware.ironpdf.IronPdfEngineManager;
import com.ironsoftware.ironpdf.PdfDocument;

// Recover automatically if the engine was interrupted (host restart, crash, kill).
if (!IronPdfEngineManager.isEngineActive()) {
    IronPdfEngineManager.restartEngine();
}

PdfDocument pdf = PdfDocument.renderHtmlAsPdf("<h1>Hello World</h1>");
pdf.saveAs("output.pdf");
```

> Note: `isEngineActive()` reports the connection IronPDF holds, regardless of
> connection mode (subprocess or remote). It does not depend on inspecting OS
> processes, so it works the same way for local and remote engines.

> Note: `stopEngine()` and `restartEngine()` are **not** supported in `CUSTOM`
> connection mode (`withCustomGrpcConnection`) and throw
> `UnsupportedOperationException`. In that mode the gRPC channel is supplied by
> you and IronPDF cannot rebuild it once shut down, so you are responsible for
> its lifecycle. `isEngineActive()` and `startEngine()` work in all modes.

### IronPdfEngine security settings (IronPdfEngine 2026.11 and later)

IronPdfEngine 2026.11 adds security settings. Most of them are opt-in, but IronPDF for Java 2026.11.1 changes three defaults: `TARGET` and `OFFICIAL_CLOUD` now check the TLS certificate (see "Certificate errors after upgrading"), the engine download is checked against a SHA-256 (see "Engine download is verified"), and remote engines (not a subprocess, and not `withHostPort` to `127.0.0.1`, `localhost` or `::1`) receive the HTML content of `renderHtmlFileAsPdf` instead of a file path. All other settings keep the current behaviour until you configure them.

**Local subprocess.** IronPDF for Java starts the subprocess with `host=127.0.0.1` and connects to `127.0.0.1`. IronPdfEngine 2026.11 and later apply this setting and listen only on the loopback address. Other machines cannot connect to the subprocess. Earlier engines ignore the setting and listen on all interfaces.

**License key of the subprocess.** IronPDF for Java does not put the license key on the command line of the subprocess. The command line is visible in the process list and in the engine start-up log. IronPDF gives the key to the subprocess in the environment variable `IRONPDF_ENGINE_LICENSE_KEY`. IronPdfEngine 2026.11 and later read this variable at start-up. For all engines, IronPDF also sends the key to the engine after it connects. When you set no license key, IronPDF does not change an `IRONPDF_ENGINE_LICENSE_KEY` value that the subprocess inherits from your JVM environment.

**Authentication token (opt-in).** An IronPdfEngine that has the environment variable `IRONPDF_ENGINE_AUTH_TOKEN` accepts only calls that send `authorization: Bearer <token>`. To send the token, use `withAuthToken`:

```java
// IronPdfEngine Docker started with: -e IRONPDF_ENGINE_AUTH_TOKEN=<token>
Settings.setConnectionMode(IronPdfEngineConnection.configure()
        .withHostPort("my-engine-host", 33350)
        .withAuthToken(System.getenv("IRONPDF_ENGINE_AUTH_TOKEN")));
```

- In `SUBPROCESS` mode, IronPDF also gives the token to the subprocess in its environment (not on the command line). If two applications share one engine on the same port, give them the same token.
- In `SUBPROCESS` mode without `withAuthToken`, IronPDF removes an `IRONPDF_ENGINE_AUTH_TOKEN` value that the subprocess inherits from your JVM environment. IronPDF does not send a token in this case, so the engine must not require one.
- In `CUSTOM` mode, you can add the header with your own `ClientInterceptor` instead. Do not do both.
- IronPDF does not write the token to the log. Engines before 2026.11 ignore the header.

**Encryption (TLS, opt-in).** An IronPdfEngine that has the environment variable `IRONPDF_ENGINE_TLS_CERT_PATH` serves gRPC over TLS on the same port. Without TLS, the token and the documents go over the network as plain text. To connect with TLS, use one of these methods:

- `withTls()`: IronPDF checks the engine certificate against the default trust store of the JVM. Use it when a public CA or your company CA (in the JVM trust store) signed the certificate.
- `withTrustedCertificate(Path pemFile)`: IronPDF trusts only the certificates in the PEM file. Use it for a self-signed engine certificate (give the engine certificate) or a private CA (give the CA certificate). Do not give the private key.

```java
// IronPdfEngine Docker started with a certificate for "my-engine-host" and a token:
//   -e IRONPDF_ENGINE_TLS_CERT_PATH=/certs/engine.crt -e IRONPDF_ENGINE_TLS_KEY_PATH=/certs/engine.key
//   (or a .pfx file with IRONPDF_ENGINE_TLS_CERT_PASSWORD) -e IRONPDF_ENGINE_AUTH_TOKEN=<token>
// See SECURITY-GUIDE.md in iron-software/IronPdf (src/IronPdfEngineConsole) for all engine settings.
Settings.setConnectionMode(IronPdfEngineConnection.configure()
        .withHostPort("my-engine-host", 33350)
        .withTrustedCertificate(Paths.get("/etc/ironpdf/engine.crt"))
        .withAuthToken(System.getenv("IRONPDF_ENGINE_AUTH_TOKEN")));
```

- The certificate must contain the host name or the IP address that you give to `withHostPort` or `withTarget` (subject alternative name). Else the connection fails.
- TLS applies to the `HOST_PORT` and `TARGET` modes. Without these methods, `HOST_PORT` uses plaintext (as before). `TARGET` and `OFFICIAL_CLOUD` always use TLS and always check the certificate against the JVM trust store. Before 2026.11.1, they accepted any certificate, so anybody on the network path could read the HTML, the PDF data and the license key.
- In `SUBPROCESS` mode, IronPDF starts the engine on `127.0.0.1` without a certificate. IronPDF logs a warning and uses plaintext. `OFFICIAL_CLOUD` always uses TLS and does not change. In `CUSTOM` mode, set TLS on your own channel. `Settings.useIronPdfEngineDocker` has no TLS setting: use `withHostPort` instead.
- A plaintext client cannot connect to an engine with TLS, and a TLS client cannot connect to an engine without TLS.
- Java 8: IronPDF uses the BoringSSL library in `grpc-netty-shaded` on Windows x64, Linux x64 and arm64 (glibc), and macOS. On other platforms, IronPDF uses the TLS of the JVM, which needs ALPN. Use Java 8u252 or later.

**Certificate errors after upgrading (`TARGET` and `OFFICIAL_CLOUD`).** If your engine uses a self-signed certificate or a certificate from a private CA, the connection now fails with `unable to find valid certification path to requested target`. Fix it in one of these ways:

- Best: give the engine certificate (or the CA certificate) to IronPDF: `IronPdfEngineConnection.configure().withTarget("engine.example.com:443").withTrustedCertificate(Paths.get("/etc/ironpdf/engine.crt"))`.
- Or add the certificate to the JVM trust store (`keytool -importcert`). This also fixes a TLS-inspecting corporate proxy.
- Short-term only: `withInsecureTls()`, the system property `-Dironpdf.tls.insecure=true` or the environment variable `IRONPDF_TLS_INSECURE=true` turns the certificate check off (as before). The property and the variable do not apply to a connection that calls `withTls()` or `withTrustedCertificate(...)`. IronPDF logs a warning each time it connects.

**Engine download is verified.** When the engine is not added as a Maven dependency, IronPDF downloads the engine zip. IronPDF compares the SHA-256 of the zip with the value that this release contains for the engine version and platform. IronPDF does not unzip or run a zip that differs, it refuses a version or platform without a known value, and it refuses a zip entry that would leave the engine folder. To avoid the download, add the `ironpdf-engine-<platform>` Maven dependency. To run an engine version or platform that this release does not know, call `Settings.setAllowUnverifiedEngineDownload(true)`, or set `-Dironpdf.engine.allowUnverifiedDownload=true` or the environment variable `IRONPDF_ENGINE_ALLOW_UNVERIFIED_DOWNLOAD=true`. IronPDF then logs a warning and skips the check. This covers only a version or platform without a known SHA-256: a zip that differs from a known SHA-256 (for example a patched or re-published zip) is always refused, and a mirror must serve byte-identical zips. For a different engine build, use the `ironpdf-engine-<platform>` Maven dependency.

**Render an HTML file with IronPdfEngine in Docker or on a remote server.** `renderHtmlFileAsPdf` sends only the content of the HTML file. The engine does not receive the CSS, image, font or script files that the page references with relative paths. If the page uses local files, put the HTML file and its files in a .zip and use `renderZipAsPdf`. Keep the same relative paths in the zip. When the client starts IronPdfEngine on the same machine (the default), `renderHtmlFileAsPdf` reads the page and its local files as before.

This applies to `Settings.useIronPdfEngineDocker` and to the `HOST_PORT`, `TARGET`, `OFFICIAL_CLOUD` and `CUSTOM` connection modes. From version 2026.11, the IronPdfEngine Docker image sets `IRONPDF_ENGINE_ALLOW_LOCAL_FILE_ACCESS=false` by default. To let the image read files by path again, start it with `-e IRONPDF_ENGINE_ALLOW_LOCAL_FILE_ACCESS=true`.

**HTML files by path (subprocess).** In `SUBPROCESS` mode, IronPDF does not change `IRONPDF_ENGINE_ALLOW_LOCAL_FILE_ACCESS`. The subprocess reads files by path, as before. If you set `IRONPDF_ENGINE_ALLOW_LOCAL_FILE_ACCESS=false` yourself, the engine refuses to read an HTML file by path. `renderHtmlFileAsPdf` then reads the file in your application and sends the HTML content, as the .NET client does. Relative links (CSS, images) in that file do not resolve against the file folder in this case. If the file needs local assets, use `renderZipAsPdf`.

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