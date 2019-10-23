# [SSLRetrofit][0]

Easy to SSL-Handshake using retrofit library.

Before moving ahead with library, lets know about <b>What is SSL-pining or SSL-handshake<b>?

SSL stands for Secure Socket Layer is the X509 certificate,
which ensures the network connection calls are secure with the chain of certificates from your leaf certificate through an intermediate certificate authority (CA) to a root certificate authority.

SSL pinning also knew as Public Key Pinning is an attempt to solve these issues, ensuring that the certificate chain used is the one your app expects by checking a particular public key or certificate appears in the chain

Using SSL in an Android app is easy, however ensuring that the connection is really secure is a different matter.

This library makes the SSL pinning with OkHttp since 2.1, ensure you use at least <b>OkHttp 3.2.0</b> or <b>OkHttp 2.7.5.</b>SSL-Pinning with OkHttp is pretty straightforward using the library.

# Usage

The library makes a handshake with two modes `PinMode` and `UnPinMode`.

* <b>UnPinMode:</b> Simply bypass the SSL pinning or handshaking, when the protocol is secure<i>(https)</i>.

```java

    HandShakeMode handShakeMode = UnPinBuilder.newBuilder()
                                  .enableTLSVersion("TLSv1.1") //optional, default SSL
                                  .build();
```

* <b>PinMode:</b> Makes SSL pinning or handshaking, when the protocol is secure<i>(https)</i> using the certificate or passing directy public key.

```java

    HandShakeMode handShakeMode = PinBuilder.newBuilder(baseURL)
                                  .pinKey("sha256/AAAAAAAAAPUBLIC-KEYAAAAAAAAAA")//hardcoded public key
                                          OR
                     PinExtract extractPin  = PinExtractor.newBuilder(context, "sha256")
                     .open("certificate.crt") //by assert or
                     .open(R.raw.certificate) //by raw or
                     .open(new File("path\certificate.crt"))  //by file
                     .build();
                                  .pinKey(extractPin)// PinExtract
                                  .build();
```

PinExtact is fetching the public key from certificate, loading by `assets`,`raw` and `file` path.

* <b>Retrofit-Handshake :</b>

```java

    //create OkHttpClient
    OkHttpClient client = OkHttpClient.Builder()
                          .connectTimeout(15, TimeUnit.SECONDS)
                          .readTimeout(15, TimeUnit.SECONDS)
                          .writeTimeout(15, TimeUnit.SECONDS)
                          .followRedirects(false)
                          .followSslRedirects(false)
                          .retryOnConnectionFailure(true)
                          .cache(null)
                          .build();

    //bind OkHttpClient with library class RetrofitHandShake
    RetrofitHandShake handshake_client = RetrofitHandShake
                                         .mode(handShakeMode)
                                         .handshake(client);

    Retrofit retrofit = Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(handshake_client)
                        .build().create(RetrofitService.class);

```
# Implementation
Download the [@ssl_retrofit-release.aar][1] file and copy to the libs folder, libs folder must be added to `project-level.gradle` file

```gradle
allprojects {
    repositories {
        google()
        jcenter()
        flatDir {
            dirs 'libs'
        }
    }
}

```
Add @aar file dependancy in `app-level.gradle` file

```gradle

dependencies {
implementation(name:'ssl_retrofit-release', ext:'aar')
}

```
[0]:https://github.com/Parmar-07/SSLRetrofit/blob/master/demo/ssl-retrofit-demo.apk
[1]:https://github.com/Parmar-07/SSLRetrofit/blob/master/demo/ssl_retrofit-release.aar