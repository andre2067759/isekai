@echo off
echo ISEKAI - Compilazione Forge 1.20.1
if not exist gradlew.bat (
  echo Gradle wrapper non trovato. Usa il progetto con Gradle 8.8 installato.
  pause
  exit /b 1
)
call gradlew.bat build --no-daemon
pause
