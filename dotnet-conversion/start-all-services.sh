#!/bin/bash
# Script to start all three QZT services on Linux/macOS
# Equivalent to running the three Java jar files

echo "Starting QZT Services..."

# Start RPC Service in background
cd Qzt.Ump.RpcService
dotnet run &
RPC_PID=$!
cd ..

# Wait a moment before starting next service
sleep 2

# Start Server Back in background
cd Qzt.Ump.ServerBack
dotnet run &
BACK_PID=$!
cd ..

# Wait a moment before starting next service
sleep 2

# Start Server Web in background
cd Qzt.Ump.ServerWeb
dotnet run &
WEB_PID=$!
cd ..

echo ""
echo "All services are starting..."
echo ""
echo "RPC Service (PID: $RPC_PID) will be available at: http://localhost:8001"
echo "Server Back (PID: $BACK_PID) will be available at: http://localhost:8002"
echo "Server Web (PID: $WEB_PID) will be available at: http://localhost:8003"
echo ""
echo "To stop all services, run:"
echo "  kill $RPC_PID $BACK_PID $WEB_PID"
echo ""
echo "Press Ctrl+C to exit (services will continue running in background)"

# Wait for user interrupt
trap "echo 'Exiting...'; exit 0" INT
wait
