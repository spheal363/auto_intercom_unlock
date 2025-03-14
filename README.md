# インターホン自動開錠IoT
自宅のインターホンが押されたことを検知してスマホから開錠

## 実行(maven)
```bash
~/auto_intercom_unlock/auto-intercom-app $ mvn clean package
```

```bash
sdoi@raspberrypi:~/auto_intercom_unlock/auto-intercom-app $ mvn jetty:run
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------------< xyz.sdoi:auto-intercom-app >---------------------
[INFO] Building auto-intercom-app 1.0-SNAPSHOT
[INFO] --------------------------------[ war ]---------------------------------
[INFO] 
[INFO] >>> jetty-maven-plugin:9.4.44.v20210927:run (default-cli) > test-compile @ auto-intercom-app >>>
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ auto-intercom-app ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] Copying 2 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:compile (default-compile) @ auto-intercom-app ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ auto-intercom-app ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /home/sdoi/auto_intercom_unlock/auto-intercom-app/src/test/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:testCompile (default-testCompile) @ auto-intercom-app ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] <<< jetty-maven-plugin:9.4.44.v20210927:run (default-cli) < test-compile @ auto-intercom-app <<<
[INFO] 
[INFO] 
[INFO] --- jetty-maven-plugin:9.4.44.v20210927:run (default-cli) @ auto-intercom-app ---
[INFO] Logging initialized @16602ms to org.eclipse.jetty.util.log.Slf4jLog
[INFO] webAppSourceDirectory not set. Trying src/main/webapp
[INFO] webAppSourceDirectory /home/sdoi/auto_intercom_unlock/auto-intercom-app/src/main/webapp does not exist. Trying /home/sdoi/auto_intercom_unlock/auto-intercom-app/target/webapp-tmp
[INFO] Reload Mechanic: automatic
[INFO] nonBlocking:false
[INFO] Classes = /home/sdoi/auto_intercom_unlock/auto-intercom-app/target/classes
[INFO] Configuring Jetty for project: auto-intercom-app
[INFO] Context path = /
[INFO] Tmp directory = /home/sdoi/auto_intercom_unlock/auto-intercom-app/target/tmp
[INFO] Web defaults = org/eclipse/jetty/webapp/webdefault.xml
[INFO] Web overrides =  none
[INFO] web.xml file = null
[INFO] Webapp directory = /home/sdoi/auto_intercom_unlock/auto-intercom-app/target/webapp-tmp
[INFO] jetty-9.4.44.v20210927; built: 2021-09-27T23:02:44.612Z; git: 8da83308eeca865e495e53ef315a249d63ba9332; jvm 17.0.13+11-Debian-2deb12u1
[INFO] Scanning elapsed time=1205ms
[INFO] DefaultSessionIdManager workerName=node0
[INFO] No SessionScavenger set, using defaults
[INFO] node0 Scavenging every 660000ms
=== AppContextListener: init ===
SLF4J(W): No SLF4J providers were found.
SLF4J(W): Defaulting to no-operation (NOP) logger implementation
SLF4J(W): See https://www.slf4j.org/codes.html#noProviders for further details.
[INFO] Started o.e.j.m.p.JettyWebAppContext@21ba2445{/,file:///home/sdoi/auto_intercom_unlock/auto-intercom-app/target/webapp-tmp/,AVAILABLE}{/home/sdoi/auto_intercom_unlock/auto-intercom-app/target/auto-intercom-app-1.0-SNAPSHOT}
[INFO] Started ServerConnector@2e5e6fc4{HTTP/1.1, (http/1.1)}{0.0.0.0:8080}
[INFO] Started @22400ms
[INFO] Started Jetty Server
App: init完了
赤: 71 緑: 88 青: 51 総和: 210 緑割合: 0.41904761904761906
赤: 71 緑: 87 青: 52 総和: 210 緑割合: 0.4142857142857143
赤: 71 緑: 88 青: 50 総和: 209 緑割合: 0.42105263157894735
赤: 71 緑: 87 青: 51 総和: 209 緑割合: 0.41626794258373206
赤: 71 緑: 88 青: 51 総和: 210 緑割合: 0.41904761904761906
赤: 72 緑: 88 青: 51 総和: 211 緑割合: 0.41706161137440756
赤: 72 緑: 87 青: 51 総和: 210 緑割合: 0.4142857142857143
赤: 71 緑: 87 青: 51 総和: 209 緑割合: 0.41626794258373206
赤: 66 緑: 81 青: 47 総和: 194 緑割合: 0.4175257731958763
赤: 61 緑: 75 青: 42 総和: 178 緑割合: 0.42134831460674155
赤: 42 緑: 48 青: 29 総和: 119 緑割合: 0.40336134453781514
赤: 36 緑: 39 青: 24 総和: 99 緑割合: 0.3939393939393939
赤: 31 緑: 31 青: 19 総和: 81 緑割合: 0.38271604938271603
赤: 42 緑: 57 青: 34 総和: 133 緑割合: 0.42857142857142855
赤: 43 緑: 68 青: 38 総和: 149 緑割合: 0.4563758389261745
Response: {}
緑値が相対的に高いため通しました: 赤 10 緑 27 青 47 緑割合 0.57446808510Received text: あける/au
push
pull
赤: 27 緑: 31 青: 17 総和: 75 緑割合: 0.41333333333333333
赤: 27 緑: 31 青: 18 総和: 76 緑割合: 0.40789473684210525
赤: 27 緑: 31 青: 18 総和: 76 緑割合: 0.40789473684210525
赤: 27 緑: 30 青: 17 総和: 74 緑割合: 0.40540540540540543
赤: 27 緑: 31 青: 17 総和: 75 緑割合: 0.41333333333333333
赤: 27 緑: 31 青: 18 総和: 76 緑割合: 0.40789473684210525
^C[INFO] Stopped ServerConnector@2e5e6fc4{HTTP/1.1, (http/1.1)}{0.0.0.0:8080}
[INFO] node0 Stopped scavenging
```

## システム概要図

![画像2](https://github.com/user-attachments/assets/435bf8f8-8d59-4ba0-877a-9ba30e31c41d)
