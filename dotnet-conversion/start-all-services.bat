@echo off
REM Script to start all three QZT services on Windows
REM Equivalent to running the three Java jar files

echo Starting QZT Services...

REM Start RPC Service
start "QZT RPC Service" cmd /k "cd Qzt.Ump.RpcService && dotnet run"

REM Wait a moment before starting next service
timeout /t 2 /nobreak >nul

REM Start Server Back
start "QZT Server Back" cmd /k "cd Qzt.Ump.ServerBack && dotnet run"

REM Wait a moment before starting next service
timeout /t 2 /nobreak >nul

REM Start Server Web
start "QZT Server Web" cmd /k "cd Qzt.Ump.ServerWeb && dotnet run"

echo.
echo All services are starting...
echo.
echo RPC Service will be available at: http://localhost:8001
echo Server Back will be available at: http://localhost:8002
echo Server Web will be available at: http://localhost:8003
echo.
echo Press any key to exit this window (services will continue running)
pause >nul
