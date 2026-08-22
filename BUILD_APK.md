# WorldCode APK direkt bauen

Der APK-Builder funktioniert direkt aus dem Repository und benötigt keinen GitHub-Actions-Workflow.

## Linux / Termux / Ubuntu

```bash
chmod +x build-apk.sh
./build-apk.sh
```

Die fertige APK liegt danach hier:

```text
releases/WorldCode.apk
```

Der Builder führt `clean` und danach `assembleDebug` aus und kopiert die erzeugte APK automatisch nach `releases/WorldCode.apk`.
