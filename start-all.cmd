@echo off
setlocal ENABLEDELAYEDEXPANSION

rem Root of this repo (this script's folder)
set ROOT=%~dp0
set ROOT=%ROOT:~0,-1%

set BASE=%ROOT%\qzt-ump
set LOGDIR=%ROOT%\run-logs
if not exist "%LOGDIR%" mkdir "%LOGDIR%"

rem Optional tuning and profile
if not defined JAVA_OPTS set JAVA_OPTS=-Xms256m -Xmx512m
if not defined PROFILE set PROFILE=dev
set ARGS=
if defined PROFILE set ARGS=--spring.profiles.active=%PROFILE%

rem Resolve Java command (prefer JAVA_HOME if available)
set "JAVA_CMD=java"
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"

rem Verify Java is available
"%JAVA_CMD%" -version >nul 2>&1
if errorlevel 1 (
  echo [ERROR] Java not found. Install Java 8+ and set either JAVA_HOME or add java to PATH.
  echo   Current JAVA_HOME=%JAVA_HOME%
  exit /b 1
)

rem Touch logs up front to ensure visibility
echo [%date% %time%] Preparing to launch zookeeper-embedded >> "%LOGDIR%\zookeeper.out.log"
echo [%date% %time%] Preparing to launch qzt-ump-rpc-service >> "%LOGDIR%\rpc-service.out.log"
echo [%date% %time%] Preparing to launch qzt-ump-server-back >> "%LOGDIR%\server-back.out.log"
echo [%date% %time%] Preparing to launch qzt-ump-server-web >> "%LOGDIR%\server-web.out.log"

echo [%date% %time%] JAVA_CMD=%JAVA_CMD%, JAVA_OPTS=%JAVA_OPTS%, PROFILE=%PROFILE% >> "%LOGDIR%\zookeeper.out.log"
echo [%date% %time%] JAVA_CMD=%JAVA_CMD%, JAVA_OPTS=%JAVA_OPTS%, PROFILE=%PROFILE% >> "%LOGDIR%\rpc-service.out.log"
echo [%date% %time%] JAVA_CMD=%JAVA_CMD%, JAVA_OPTS=%JAVA_OPTS%, PROFILE=%PROFILE% >> "%LOGDIR%\server-back.out.log"
echo [%date% %time%] JAVA_CMD=%JAVA_CMD%, JAVA_OPTS=%JAVA_OPTS%, PROFILE=%PROFILE% >> "%LOGDIR%\server-web.out.log"

rem Optionally start embedded ZooKeeper if its shaded JAR exists
set ZK_DIR=%ROOT%\zookeeper-embedded\target
set ZK_JAR=%ZK_DIR%\zookeeper-embedded-1.0.0.jar
if exist "%ZK_JAR%" (
  echo [%date% %time%] Launching zookeeper-embedded >> "%LOGDIR%\zookeeper.out.log"
  start "zookeeper-embedded" /D "%ZK_DIR%" cmd /c ""%JAVA_CMD%" %JAVA_OPTS% -jar "%ZK_JAR%" 2181 >> "%LOGDIR%\zookeeper.out.log" 2>&1"
) else (
  echo [%date% %time%] [WARN] zookeeper-embedded JAR not found at %ZK_JAR%. Skipping embedded ZK start. >> "%LOGDIR%\zookeeper.out.log"
)

rem Launch RPC service, then back-end, then web (DicLoad has retry to tolerate provider not ready)
echo [%date% %time%] Launching qzt-ump-rpc-service >> "%LOGDIR%\rpc-service.out.log"
call :start_service "qzt-ump-rpc-service" "%BASE%\qzt-ump-rpc-service\target" "%BASE%\qzt-ump-rpc-service\target\qzt-ump-rpc-service-1.0.0.jar" "%LOGDIR%\rpc-service.log" "%LOGDIR%\rpc-service.out.log"

echo [%date% %time%] Launching qzt-ump-server-back >> "%LOGDIR%\server-back.out.log"
call :start_service "qzt-ump-server-back" "%BASE%\qzt-ump-server-back\target" "%BASE%\qzt-ump-server-back\target\qzt-ump-server-back-1.0.0.jar" "%LOGDIR%\server-back.log" "%LOGDIR%\server-back.out.log"

echo [%date% %time%] Launching qzt-ump-server-web >> "%LOGDIR%\server-web.out.log"
call :start_service "qzt-ump-server-web" "%BASE%\qzt-ump-server-web\target" "%BASE%\qzt-ump-server-web\target\qzt-ump-server-web-1.0.0.jar" "%LOGDIR%\server-web.log" "%LOGDIR%\server-web.out.log"

echo.
echo Started launch commands for:
echo  - zookeeper-embedded (if available)
echo  - qzt-ump-rpc-service

echo  - qzt-ump-server-back

echo  - qzt-ump-server-web

echo Logs are under: %LOGDIR%

echo.
echo Current files in log dir:
dir /-C "%LOGDIR%"

echo.
echo Java processes:
tasklist /FI "IMAGENAME eq java.exe" | findstr /I java

endlocal
exit /b 0

:start_service
rem Args: %1=name %2=workdir %3=jarPath %4=appLog %5=outLog
set "_NAME=%~1"
set "_DIR=%~2"
set "_JAR=%~3"
set "_APPLOG=%~4"
set "_OUT=%~5"

if not exist "%_DIR%" (
  echo [%date% %time%] [ERROR] Missing directory: %_DIR% >> "%_OUT%"
  goto :eof
)
if not exist "%_JAR%" (
  echo [%date% %time%] [ERROR] Missing jar: %_JAR% >> "%_OUT%"
  goto :eof
)

echo [%date% %time%] Starting %_NAME% with JAR %_JAR% >> "%_OUT%"
start "%_NAME%" /D "%_DIR%" cmd /c ""%JAVA_CMD%" %JAVA_OPTS% -Dlogging.file="%_APPLOG%" -jar "%_JAR%" %ARGS% >> "%_OUT%" 2>&1"

goto :eof
