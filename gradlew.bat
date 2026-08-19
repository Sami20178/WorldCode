@ECHO OFF
SET DIR=%~dp0
SET GRADLE_VERSION=8.11.1
SET DIST=%USERPROFILE%\.gradle\wrapper\dists\gradle-%GRADLE_VERSION%-bin
SET ZIP=%DIST%.zip
SET URL=https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip
IF NOT EXIST "%DIST%\gradle-%GRADLE_VERSION%\bin\gradle.bat" (
  IF NOT EXIST "%DIST%" MKDIR "%DIST%"
  powershell -NoProfile -Command "Invoke-WebRequest -Uri '%URL%' -OutFile '%ZIP%'"
  powershell -NoProfile -Command "Expand-Archive -Force '%ZIP%' '%DIST%'"
)
CALL "%DIST%\gradle-%GRADLE_VERSION%\bin\gradle.bat" %*
