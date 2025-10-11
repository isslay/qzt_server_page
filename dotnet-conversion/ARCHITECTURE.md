# .NET 8.0 Architecture Overview

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client Applications                       │
│                    (Web Browser, Mobile App)                     │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
         ┌───────────────────────────────────────────┐
         │            Nginx / Load Balancer           │
         └───────────────────┬───────────────────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│  ServerWeb   │    │  ServerBack  │    │  RpcService  │
│  Port 8003   │    │  Port 8002   │    │  Port 8001   │
│              │    │              │    │              │
│ ASP.NET Core │    │ ASP.NET Core │    │ ASP.NET Core │
│   Web API    │    │   Web API    │    │   Web API    │
└──────┬───────┘    └──────┬───────┘    └──────┬───────┘
       │                   │                    │
       └───────────────────┼────────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   Common.    │    │   Common.    │    │   Common.    │
│     Web      │    │   Database   │    │    Redis     │
└──────────────┘    └──────┬───────┘    └──────┬───────┘
                           │                    │
                           ▼                    ▼
                    ┌──────────────┐    ┌──────────────┐
                    │    MySQL     │    │    Redis     │
                    │   Database   │    │    Cache     │
                    └──────────────┘    └──────────────┘
```

## Project Dependencies

```
┌─────────────────────────────────────────────────────────────────┐
│                      QztServerPage.sln                           │
└─────────────────────────────────────────────────────────────────┘
                             │
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│ Qzt.Ump.     │    │ Qzt.Ump.     │    │ Qzt.Ump.     │
│ ServerWeb    │    │ ServerBack   │    │ RpcService   │
│              │    │              │    │              │
│ (Web API)    │    │ (Web API)    │    │ (Web API)    │
└──────┬───────┘    └──────┬───────┘    └──────┬───────┘
       │                   │                    │
       └───────────────────┼────────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│ Qzt.Common.  │    │ Qzt.Common.  │    │ Qzt.Common.  │
│    Web       │    │   Database   │    │    Redis     │
└──────┬───────┘    └──────┬───────┘    └──────┬───────┘
       │                   │                    │
       └───────────────────┼────────────────────┘
                           │
                           ▼
                    ┌──────────────┐
                    │ Qzt.Common.  │
                    │    Core      │
                    └──────────────┘
```

## Component Responsibilities

### Service Applications

#### Qzt.Ump.RpcService (Port 8001)
**Purpose**: RPC service provider (equivalent to qzt-ump-rpc-service)

**Responsibilities**:
- Provides backend business logic services
- Data persistence operations
- Database access through Entity Framework Core
- Message queue integration
- Scheduled background tasks

**Key Features**:
- Entity Framework Core for ORM
- MyBatis mapper scanning equivalent
- Transaction management
- Scheduled tasks with IHostedService
- Redis caching

**Java Equivalent**:
```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.qzt.*.rpc.service.impl"})
@MapperScan(basePackages = {"com.qzt.*.dao.mapper"})
```

**C# Implementation**:
```csharp
builder.Services.AddDbContext<ApplicationDbContext>();
builder.Services.AddHostedService<ScheduledTaskService>();
```

---

#### Qzt.Ump.ServerBack (Port 8002)
**Purpose**: Backend administration server (equivalent to qzt-ump-server-back)

**Responsibilities**:
- Administrative API endpoints
- User management
- System configuration
- Monitoring and logging
- Session management
- XSS protection

**Key Features**:
- ASP.NET Core Web API
- Distributed session with Redis
- XSS protection middleware
- Swagger/OpenAPI documentation
- CORS configuration

**Java Equivalent**:
```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.qzt.ump.server", "com.qzt.common.web"})
@ServletComponentScan({"com.qzt.common.web.filter"})
```

**C# Implementation**:
```csharp
builder.Services.AddSession();
builder.Services.AddScoped<XssProtectionMiddleware>();
app.UseMiddleware<XssProtectionMiddleware>();
```

---

#### Qzt.Ump.ServerWeb (Port 8003)
**Purpose**: Web frontend server (equivalent to qzt-ump-server-web)

**Responsibilities**:
- Public-facing API endpoints
- Web application endpoints
- Payment integration
- Message queue consumers
- CORS for frontend integration

**Key Features**:
- ASP.NET Core Web API
- Payment service integration
- Message queue handling
- CORS enabled
- Static file serving

**Java Equivalent**:
```java
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.qzt.ump.server",
    "com.qzt.common.pay",
    "com.qzt.common.mq"
})
```

**C# Implementation**:
```csharp
builder.Services.AddCors();
builder.Services.AddControllers();
app.UseCors();
```

---

### Common Libraries

#### Qzt.Common.Core
**Purpose**: Core utilities and shared functionality

**Responsibilities**:
- Common utility classes
- Extension methods
- Constants and enumerations
- Helper functions
- Base classes

**Typical Contents**:
```csharp
// String utilities
// Date/time helpers
// Validation helpers
// Encryption utilities
// Common interfaces
```

---

#### Qzt.Common.Database
**Purpose**: Database access layer

**Responsibilities**:
- Entity Framework Core DbContext
- Entity models
- Database configurations
- Repository pattern implementations
- Migration support

**Typical Contents**:
```csharp
// ApplicationDbContext
// Entity models (User, Role, Menu, etc.)
// Repository interfaces
// Repository implementations
// Database configuration
```

**Usage Example**:
```csharp
public class ApplicationDbContext : DbContext
{
    public DbSet<User> Users { get; set; }
    public DbSet<Role> Roles { get; set; }
    
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        // Entity configurations
    }
}
```

---

#### Qzt.Common.Redis
**Purpose**: Redis cache management

**Responsibilities**:
- Redis connection management
- Cache helper methods
- Distributed cache implementations
- Session storage

**Typical Contents**:
```csharp
// Redis connection factory
// Cache manager
// Distributed lock implementation
// Session manager
```

**Usage Example**:
```csharp
public class CacheManager
{
    private readonly IDistributedCache _cache;
    
    public async Task<T> GetOrSetAsync<T>(string key, Func<Task<T>> factory)
    {
        // Cache logic
    }
}
```

---

#### Qzt.Common.Web
**Purpose**: Web-related common functionality

**Responsibilities**:
- Middleware components
- Filters and action filters
- Web utilities
- HTTP context helpers
- Result formatters

**Typical Contents**:
```csharp
// XSS protection middleware
// Exception handling middleware
// Response formatting
// Authentication helpers
```

---

## Data Flow Example

### User Login Flow

```
1. Client Request
   │
   ├─→ POST /api/auth/login
   │   {username: "admin", password: "123456"}
   │
2. ServerWeb Controller
   │
   ├─→ UserController.Login(LoginRequest)
   │
3. Service Layer
   │
   ├─→ IUserService.AuthenticateAsync()
   │
4. Cache Check (Redis)
   │
   ├─→ Check cached user session
   │   └─→ If found, return cached data
   │
5. Database Query (if cache miss)
   │
   ├─→ IUserRepository.GetByUsernameAsync()
   │   └─→ Entity Framework Core query
   │       └─→ MySQL Database
   │
6. Password Verification
   │
   ├─→ Password hashing and comparison
   │
7. Cache Result
   │
   ├─→ Store user session in Redis
   │
8. Return Response
   │
   └─→ JSON response with JWT token
       {success: true, token: "eyJhbGc..."}
```

## Technology Stack Details

### Presentation Layer
- **Framework**: ASP.NET Core 8.0
- **API Style**: RESTful Web API
- **Documentation**: Swagger/OpenAPI (Swashbuckle)
- **Serialization**: System.Text.Json

### Business Layer
- **Dependency Injection**: Built-in DI container
- **Logging**: Serilog with file and console sinks
- **Validation**: Data Annotations + FluentValidation
- **Mapping**: AutoMapper (recommended)

### Data Access Layer
- **ORM**: Entity Framework Core 9.0
- **Database**: MySQL (via Pomelo.EntityFrameworkCore.MySql)
- **Connection Pooling**: Built-in EF Core pooling
- **Migrations**: EF Core Migrations

### Caching Layer
- **Cache Provider**: StackExchange.Redis
- **Session Storage**: Redis-based distributed session
- **Cache Strategy**: Read-through, write-through patterns

### Cross-Cutting Concerns
- **Configuration**: appsettings.json (IConfiguration)
- **Logging**: Serilog (structured logging)
- **Monitoring**: Application Insights (optional)
- **Error Handling**: Exception middleware

## Deployment Architecture

### Development Environment
```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│  Developer   │───▶│ Visual Studio│───▶│  dotnet run  │
│   Machine    │    │     2022     │    │   or F5      │
└──────────────┘    └──────────────┘    └──────────────┘
```

### Production Environment
```
┌──────────────┐
│   Clients    │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│    Nginx     │ (Reverse Proxy)
└──────┬───────┘
       │
       ├─→ http://localhost:8001 (RpcService)
       ├─→ http://localhost:8002 (ServerBack)
       └─→ http://localhost:8003 (ServerWeb)
```

### Docker Deployment (Optional)
```
┌─────────────────────────────────────────┐
│           Docker Compose                 │
│                                          │
│  ┌────────────────────────────────────┐ │
│  │  nginx:alpine                      │ │
│  └────────────┬───────────────────────┘ │
│               │                          │
│  ┌────────────┼───────────────────────┐ │
│  │  qzt-rpcservice:latest            │ │
│  │  qzt-serverback:latest            │ │
│  │  qzt-serverweb:latest             │ │
│  └────────────┬───────────────────────┘ │
│               │                          │
│  ┌────────────┼───────────────────────┐ │
│  │  mysql:8.0                        │ │
│  │  redis:alpine                     │ │
│  └───────────────────────────────────┘ │
└─────────────────────────────────────────┘
```

## Performance Considerations

### Database Optimization
- Connection pooling enabled by default
- Async/await for all database operations
- Query result caching with Redis
- Compiled queries for frequent operations

### Caching Strategy
- Distributed cache with Redis
- In-memory cache for frequently accessed data
- Cache invalidation strategies
- Sliding and absolute expiration policies

### Scalability
- Stateless services for horizontal scaling
- Session stored in Redis (distributed)
- Load balancing ready
- Microservices architecture support

## Security Architecture

```
Request Flow with Security Layers:

Client Request
    │
    ▼
[1] HTTPS/TLS Encryption
    │
    ▼
[2] XSS Protection Middleware
    │
    ▼
[3] CORS Policy Check
    │
    ▼
[4] Authentication Middleware
    │
    ▼
[5] Authorization Middleware
    │
    ▼
[6] Controller Action
    │
    ▼
Response
```

### Security Features Implemented
- ✅ HTTPS redirection
- ✅ XSS protection headers
- ✅ CSRF protection ready
- ✅ Content Security Policy ready
- ✅ Distributed session security
- ✅ SQL injection prevention (EF Core)
- 📋 JWT authentication (to be implemented)
- 📋 Role-based authorization (to be implemented)

## Monitoring and Logging

### Log Levels
```
Trace    → Detailed diagnostic information
Debug    → Internal system events
Info     → General informational messages
Warning  → Unexpected but handled situations
Error    → Error events
Critical → Critical failures requiring immediate attention
```

### Log Outputs
- Console (for development)
- File (rolling daily logs)
- Database (optional)
- Application Insights (optional)

## Next Steps for Implementation

1. **Phase 1**: Implement database models and repositories
2. **Phase 2**: Migrate business services
3. **Phase 3**: Implement controllers and API endpoints
4. **Phase 4**: Add authentication and authorization
5. **Phase 5**: Implement RPC/gRPC communication
6. **Phase 6**: Add message queue integration
7. **Phase 7**: Performance testing and optimization
8. **Phase 8**: Production deployment

---

For code-level details, refer to:
- **README.md** - Setup and configuration
- **MIGRATION_GUIDE.md** - Code conversion patterns
- **QUICKSTART.md** - Getting started guide
