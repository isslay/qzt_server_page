# QZT Server Page - .NET 8.0 Conversion

This directory contains the C#/.NET 8.0 conversion of the Java/Spring Boot QZT Server Page project.

## Overview

This conversion migrates the Java-based distributed system architecture to C#/.NET 8.0, maintaining the same architectural principles and functionality while leveraging modern .NET technologies.

## Project Structure

```
dotnet-conversion/
├── QztServerPage.sln                    # Main solution file
├── Qzt.Ump.RpcService/                  # RPC Service (formerly qzt-ump-rpc-service)
├── Qzt.Ump.ServerBack/                  # Backend Server (formerly qzt-ump-server-back)
├── Qzt.Ump.ServerWeb/                   # Web Server (formerly qzt-ump-server-web)
├── Qzt.Common.Core/                     # Core utilities and helpers
├── Qzt.Common.Database/                 # Database access components
├── Qzt.Common.Redis/                    # Redis cache components
└── Qzt.Common.Web/                      # Web components and middleware
```

## Technology Stack Comparison

| Java Stack | .NET 8.0 Equivalent |
|------------|---------------------|
| Spring Boot 1.5.8 | ASP.NET Core 8.0 |
| MyBatis/MyBatis-Plus | Entity Framework Core 9.0 |
| Druid Connection Pool | Built-in EF Core connection pooling |
| Jedis (Redis) | StackExchange.Redis 2.9+ |
| Lombok | C# Records and Properties |
| Logback/SLF4J | Serilog |
| Dubbo RPC | gRPC (recommended) or custom HTTP/REST |
| Swagger2 | Swashbuckle.AspNetCore |
| Maven | .NET SDK / NuGet |
| Alibaba Fastjson | System.Text.Json |
| Hibernate Validator | Data Annotations + FluentValidation |
| Apache Shiro | ASP.NET Core Identity / Authorization |
| ActiveMQ | RabbitMQ / Azure Service Bus |
| Zookeeper | Consul / etcd (for service discovery) |

## Prerequisites

- **.NET 8.0 SDK** or later
- **Visual Studio 2022** (v17.8 or later recommended)
- **MySQL 5.5+** (same as Java version)
- **Redis 3.0+** (same as Java version)

## Getting Started with Visual Studio 2022

### 1. Open the Solution

1. Launch Visual Studio 2022
2. Click **File** → **Open** → **Project/Solution**
3. Navigate to `dotnet-conversion/QztServerPage.sln` and open it

### 2. Configure Connection Strings

Update the `appsettings.json` file in each project:

**Qzt.Ump.RpcService/appsettings.json:**
```json
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=localhost;Port=3306;Database=qzt_ump;Uid=root;Pwd=your_mysql_password;",
    "Redis": "localhost:6379,password=your_redis_password"
  }
}
```

**Qzt.Ump.ServerBack/appsettings.json:**
```json
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=localhost;Port=3306;Database=qzt_ump;Uid=root;Pwd=your_mysql_password;",
    "Redis": "localhost:6379,password=your_redis_password"
  }
}
```

**Qzt.Ump.ServerWeb/appsettings.json:**
```json
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=localhost;Port=3306;Database=qzt_ump;Uid=root;Pwd=your_mysql_password;",
    "Redis": "localhost:6379,password=your_redis_password"
  }
}
```

### 3. Restore NuGet Packages

In Visual Studio:
1. Right-click on the solution in Solution Explorer
2. Select **Restore NuGet Packages**

Or via command line:
```bash
cd dotnet-conversion
dotnet restore
```

### 4. Build the Solution

In Visual Studio:
1. Click **Build** → **Build Solution** (or press Ctrl+Shift+B)

Or via command line:
```bash
dotnet build
```

### 5. Run the Applications

You can run all three services simultaneously or individually.

#### Option A: Using Visual Studio (Recommended)

1. Right-click on the solution in Solution Explorer
2. Select **Properties**
3. Go to **Common Properties** → **Startup Project**
4. Select **Multiple startup projects**
5. Set Action to **Start** for:
   - Qzt.Ump.RpcService
   - Qzt.Ump.ServerBack
   - Qzt.Ump.ServerWeb
6. Click **OK** and press F5 to start debugging

#### Option B: Using Command Line

Open three separate terminal windows:

**Terminal 1 - RPC Service:**
```bash
cd dotnet-conversion/Qzt.Ump.RpcService
dotnet run
```
Access at: http://localhost:8001

**Terminal 2 - Server Back:**
```bash
cd dotnet-conversion/Qzt.Ump.ServerBack
dotnet run
```
Access at: http://localhost:8002

**Terminal 3 - Server Web:**
```bash
cd dotnet-conversion/Qzt.Ump.ServerWeb
dotnet run
```
Access at: http://localhost:8003

### 6. Access Swagger UI

Once the applications are running, you can access the Swagger UI for each service:

- RPC Service: http://localhost:8001/swagger
- Server Back: http://localhost:8002/swagger
- Server Web: http://localhost:8003/swagger

## Key Differences from Java Version

### 1. Configuration Files

- **Java**: `application.yml` / `application.properties`
- **.NET**: `appsettings.json` / `appsettings.Development.json`

### 2. Dependency Injection

- **Java**: Spring `@Autowired`, `@Component`, `@Service`
- **.NET**: Constructor injection with `IServiceCollection`

### 3. Data Access

- **Java**: MyBatis with XML mappers
- **.NET**: Entity Framework Core with LINQ queries

Example:
```csharp
// Java (MyBatis)
@Select("SELECT * FROM users WHERE id = #{id}")
User findById(Long id);

// C# (Entity Framework Core)
var user = await dbContext.Users.FirstOrDefaultAsync(u => u.Id == id);
```

### 4. Logging

- **Java**: Lombok's `@Slf4j` with Logback
- **.NET**: Serilog with structured logging

Example:
```csharp
// Java
log.info("User logged in: {}", username);

// C#
_logger.LogInformation("User logged in: {Username}", username);
```

### 5. Scheduled Tasks

- **Java**: `@Scheduled` annotation
- **.NET**: `IHostedService` implementation

Example:
```csharp
public class ScheduledTaskService : IHostedService
{
    private Timer _timer;

    public Task StartAsync(CancellationToken cancellationToken)
    {
        _timer = new Timer(DoWork, null, TimeSpan.Zero, TimeSpan.FromMinutes(5));
        return Task.CompletedTask;
    }

    private void DoWork(object state)
    {
        // Your scheduled task logic here
    }

    public Task StopAsync(CancellationToken cancellationToken)
    {
        _timer?.Dispose();
        return Task.CompletedTask;
    }
}
```

## Migration Notes

### Components Requiring Manual Implementation

The following components from the Java version need to be implemented based on your specific business logic:

1. **Database Models and Repositories**
   - Create Entity classes in `Qzt.Common.Database`
   - Implement DbContext with your database schema
   - Add repositories for data access

2. **Controllers and API Endpoints**
   - Convert Java `@RestController` classes to C# Controller classes
   - Migrate request/response models

3. **Business Logic Services**
   - Convert Java `@Service` classes to C# service classes
   - Register services in Program.cs using `builder.Services.AddScoped<T>()`

4. **Authentication and Authorization**
   - Migrate from Apache Shiro to ASP.NET Core Identity
   - Implement JWT token authentication if needed

5. **RPC Communication**
   - Replace Dubbo with gRPC or REST API calls
   - Implement service discovery mechanism (Consul, etcd, etc.)

6. **Message Queue Integration**
   - Replace ActiveMQ with RabbitMQ or Azure Service Bus
   - Implement message consumers and publishers

7. **Payment Integration**
   - Migrate Alipay/WeChat Pay integrations
   - Update payment service implementations

### Database Migration

The existing MySQL database schema can be used as-is. To generate Entity Framework Core models from the database:

```bash
# Install the EF Core tools
dotnet tool install --global dotnet-ef

# Scaffold models from existing database
cd Qzt.Common.Database
dotnet ef dbcontext scaffold "Server=localhost;Database=qzt_ump;User=root;Password=your_password;" Pomelo.EntityFrameworkCore.MySql --output-dir Models --context-dir . --context ApplicationDbContext
```

## Building for Production

### Create Release Builds

```bash
# Build all projects in Release mode
dotnet build --configuration Release

# Or build and publish
dotnet publish Qzt.Ump.RpcService -c Release -o ./publish/rpc-service
dotnet publish Qzt.Ump.ServerBack -c Release -o ./publish/server-back
dotnet publish Qzt.Ump.ServerWeb -c Release -o ./publish/server-web
```

### Running in Production

The three services can be started with:

```bash
# RPC Service
dotnet Qzt.Ump.RpcService.dll

# Server Back
dotnet Qzt.Ump.ServerBack.dll

# Server Web
dotnet Qzt.Ump.ServerWeb.dll
```

These commands are equivalent to the Java commands:
```bash
# Original Java commands
java -jar qzt-ump-rpc-service-1.0.0.jar
java -jar qzt-ump-server-back-1.0.0.jar
java -jar qzt-ump-server-web-1.0.0.jar
```

## Deployment Options

### 1. Windows Service

Install as Windows Services using:
```bash
sc create QztRpcService binPath="C:\path\to\Qzt.Ump.RpcService.exe"
sc create QztServerBack binPath="C:\path\to\Qzt.Ump.ServerBack.exe"
sc create QztServerWeb binPath="C:\path\to\Qzt.Ump.ServerWeb.exe"
```

### 2. Docker

Create Dockerfiles for each service:

```dockerfile
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS base
WORKDIR /app
EXPOSE 8001

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src
COPY ["Qzt.Ump.RpcService/Qzt.Ump.RpcService.csproj", "Qzt.Ump.RpcService/"]
RUN dotnet restore "Qzt.Ump.RpcService/Qzt.Ump.RpcService.csproj"
COPY . .
WORKDIR "/src/Qzt.Ump.RpcService"
RUN dotnet build "Qzt.Ump.RpcService.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "Qzt.Ump.RpcService.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Qzt.Ump.RpcService.dll"]
```

### 3. IIS Deployment

1. Install the .NET 8.0 Hosting Bundle on the IIS server
2. Create an Application Pool for each service
3. Deploy the published files to IIS website directories
4. Configure the web.config file for each application

## Additional Resources

- [ASP.NET Core Documentation](https://docs.microsoft.com/aspnet/core)
- [Entity Framework Core Documentation](https://docs.microsoft.com/ef/core)
- [Serilog Documentation](https://serilog.net/)
- [StackExchange.Redis Documentation](https://stackexchange.github.io/StackExchange.Redis/)

## Support and Migration Assistance

This conversion provides the foundational structure for migrating from Java/Spring Boot to C#/.NET 8.0. Additional business logic, specific implementations, and integrations will need to be migrated based on your specific requirements.

For questions or assistance with the migration, please refer to the original Java codebase documentation.

## License

This project maintains the same license as the original Java project: [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
