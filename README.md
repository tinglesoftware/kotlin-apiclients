# Tingle Api Client: Android

This is an abstract API client library build on top of
[Okhttp](http://square.github.io/okhttp/) library to ease the process of
making various server request.

<h2>Assumptions</h2>

  - Knowledge on development of android applications.
  - Knowledge on how to compile android libraries.

<h2>Features</h2>

  - Making POST requests.
  - Making GET requests.
  - Making PUT requests.
  - Making DELETE requests.
  - Making PATCH requests.

<h2>Download</h2>

To download and use the library you will need to bed authorized.

Start by generating maven or gradle credentials.

Once you have the credentials, you can now download the library.

   <h5>Maven</h5>

```
<repository>
    <id>tingle-visualstudio.com-tingle</id>
    <url>https://tingle.pkgs.visualstudio.com/_packaging/tingle/maven/v1</url>
    <releases>
        <enabled>true</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```

```
<dependency>
     <groupId>tingle.software.api</groupId>
     <artifactId>abstractions</artifactId>
     <version>0.1.3</version>
</dependency>
```

   <h5>Gradle</h5>

```
...
repositories {
        maven {
            url 'https://tingle.pkgs.visualstudio.com/_packaging/tingle/maven/v1'
            credentials {
                username "VSTS"
                password "${vstsGradleAccessToken}"
            }
        }
    }
...
```

```
implementation(group: 'tingle.software.api', name: 'abstractions', version: '[version]')
```

# Examples

#### Building the request header

```
public class SampleApiClient extends AbstractApiClient {
    private static final String HEADER_NAME_AUTHORIZATION = "Authorization";
    private static final String HEADER_NAME_APP_PACKAGE_ID = "AppPackageId";
    private static final String HEADER_NAME_APP_VERSION_NAME = "AppVersionName";
    private static final String HEADER_NAME_APP_VERSION_CODE = "AppVersionCode";


    private String accessToken;

    public SampleApiClient(@NonNull String baseUrl,String accessToken) {
        super(baseUrl);
        this.accessToken = accessToken;
    }

    @Override
    public long getTimeout() {
            return 10; //connection, read and write time out; TimeUnit.SECONDS
    }

    //Neccessary if one needs to show Okhttp logs during debug
    @Override
    public boolean getBuildConfig() {
            return BuildConfig.DEBUG;
    }


    @Override
    protected void authenticate(Request.Builder builder) {
       if (!TextUtils.isEmpty(accessToken))
             builder
                  .header(HEADER_NAME_APP_PACKAGE_ID, BuildConfig.APPLICATION_ID)
                  .header(HEADER_NAME_APP_VERSION_NAME, BuildConfig.VERSION_NAME)
                  .header(HEADER_NAME_APP_VERSION_CODE, String.valueOf(BuildConfig.VERSION_CODE))
                  .header(HEADER_NAME_AUTHORIZATION, "Bearer " + accessToken);

    }

}

```


##### GET from server

```
...
    public ResourceResponse<Pojo> getFromServer(){
        String url = "";
        return executeGet(url, Pojo.class);
    }
...
```


##### Post to server

```
...
    public ResourceResponse<Pojo> postToServer(Foo requestBody){
       String url = "";
       return executePost(url,requestBody, Pojo.class);
    }
...
```
