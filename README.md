# roterOktober-server
"Comrades, our own fleet doesn't know our full potential. They will do everything possible to test us; but they will only test their own embarassment."
 -- Capt. Marko Ramius

# Getting started
Der Server erwartet eine MySQL-Datenbank. Diese muss ein Schema mit dem Namen `roteroktober` haben. Der Benutzername und das Passwort können beim Starten der Anwendung übergeben werden:
```
java -jar roteroktober-server.jar --dbuser=user --dbpassword=secret
```

Wenn alles gut geht kann jetzt die API-Dokumentation geöffnet werden:
http://localhost:8080/swagger-ui.html