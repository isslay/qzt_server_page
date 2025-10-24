@echo off
setlocal ENABLEDELAYEDEXPANSION

set SERVICES=qzt-ump-rpc-service-1.0.0.jar qzt-ump-server-back-1.0.0.jar qzt-ump-server-web-1.0.0.jar

for %%J in (%SERVICES%) do (
  echo Attempting to stop %%J ...
  wmic process where "name='java.exe' and CommandLine like '%%%~J%%'" call terminate >nul 2>&1
)

echo Done. Current Java processes:
tasklist /FI "IMAGENAME eq java.exe" | findstr /I java

endlocal
exit /b 0

