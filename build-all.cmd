@echo off
setlocal enableextensions enabledelayedexpansion

REM ============================================================
REM Build script for the qzt multi-module project (Windows/cmd)
REM - Uses a project-local .m2 repository to minimize network use
REM - Skips tests and javadocs for a fast, reproducible build
REM - Provides clear errors if Java or Maven are not available
REM ============================================================

set "PROJECT_DIR=%~dp0"
set "MVN_CMD="
set "SETTINGS_OPT="

REM Prefer Maven Wrapper if available
if exist "%PROJECT_DIR%mvnw.cmd" (
  set "MVN_CMD=%PROJECT_DIR%mvnw.cmd"
) else (
  REM Try to locate mvn on PATH
  where mvn >nul 2>&1
  if %ERRORLEVEL% EQU 0 (
    for /f "delims=" %%i in ('where mvn') do set "MVN_CMD=%%i"
  ) else (
    REM Try typical Maven installation via MAVEN_HOME
    if defined MAVEN_HOME (
      if exist "%MAVEN_HOME%\bin\mvn.cmd" set "MVN_CMD=%MAVEN_HOME%\bin\mvn.cmd"
    )
  )
)

if not defined MVN_CMD (
  echo [ERROR] Maven was not found (no mvnw.cmd, mvn on PATH, or MAVEN_HOME).
  echo         Please either:
  echo           1) Install Apache Maven 3.3+ and ensure mvn is on PATH, or
  echo           2) Use the included Maven Wrapper by keeping mvnw.cmd in the project root.
  echo         Download Maven: https://maven.apache.org/download.cgi
  exit /b 2
)

REM Check Java availability (required by Maven and the wrapper)
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
  echo [ERROR] Java runtime not found on PATH. Please install JDK 1.8+ and ensure java is available.
  echo         Download (Temurin): https://adoptium.net/temurin/releases/?version=8
  echo         After install, set JAVA_HOME and update PATH, e.g.:
  echo           setx JAVA_HOME "C:\\Program Files\\Eclipse Adoptium\\jdk-8" /M
  echo           setx PATH "%PATH%;%%JAVA_HOME%%\\bin" /M
  exit /b 3
)

pushd "%PROJECT_DIR%"

REM Ensure local repository folder exists
if not exist ".m2" mkdir ".m2"

REM If a project-local settings.xml exists, use it
if exist ".mvn\settings.xml" (
  set "SETTINGS_OPT=-s %CD%\.mvn\settings.xml"
  echo [INFO] Using project-local Maven settings: %CD%\.mvn\settings.xml
)

REM Show tool versions
"%MVN_CMD%" %SETTINGS_OPT% -v
if %ERRORLEVEL% NEQ 0 (
  echo [ERROR] Failed to run Maven. Aborting.
  popd
  exit /b 4
)

echo.
echo [INFO] Starting full multi-module build ...

REM Use a project-local repository and skip tests/javadocs for speed and fewer external deps
set "MVN_ARGS=-T 1C -Dmaven.repo.local=%CD%\.m2 -DskipTests=true -Dmaven.javadoc.skip=true -Dcheckstyle.skip=true"

"%MVN_CMD%" %SETTINGS_OPT% %MVN_ARGS% clean install
set "BUILD_RC=%ERRORLEVEL%"

if %BUILD_RC% NEQ 0 (
  echo.
  echo [ERROR] Build failed with exit code %BUILD_RC%.
  echo         Check the error messages above. Common issues:
  echo           - Missing/blocked internet access to download dependencies
  echo           - Incompatible JDK version (use JDK 1.8 if unsure)
  echo           - Corporate proxy: configure Maven settings.xml with proxy
  popd
  exit /b %BUILD_RC%
)

echo.
echo [SUCCESS] Build completed. Artifacts are in each module's target/ directory.

popd
exit /b 0
