# roterOktober-server
"Comrades, our own fleet doesn't know our full potential. They will do everything possible to test us; but they will only test their own embarassment."
 -- Capt. Marko Ramius

[![Build Status](https://drone.io/github.com/SchweizerischeBundesbahnen/roterOktober-server/status.png)](https://drone.io/github.com/SchweizerischeBundesbahnen/roterOktober-server/latest)

# Getting started
Der Server unterstützt MySQL und PostgreSQL Datenbanken. Diese müssen ein Schema mit dem Namen `roteroktober` haben. Die Zugriffsdaten werden beim Start übergeben.

**MySQL**
```
java -jar roteroktober-server.jar -Dspring.profiles.active=mysql --dbuser=user --dbpassword=secret
```

**PostgreSQL**
```
java -jar roteroktober-server.jar -Dspring.profiles.active=postgresql --dbuser=user --dbpassword=secret
```

**API-Dokumentation**

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
