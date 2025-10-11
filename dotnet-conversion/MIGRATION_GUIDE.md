# Java to C#/.NET 8.0 Migration Guide

This guide provides detailed instructions for migrating the QZT Server Page Java/Spring Boot application to C#/.NET 8.0.

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Step-by-Step Migration Process](#step-by-step-migration-process)
3. [Code Conversion Patterns](#code-conversion-patterns)
4. [Common Pitfalls and Solutions](#common-pitfalls-and-solutions)
5. [Testing Strategy](#testing-strategy)

## Architecture Overview

### Original Java Architecture
- **Framework**: Spring Boot 1.5.8
- **ORM**: MyBatis + MyBatis-Plus
- **RPC**: Dubbo 2.5.7
- **Cache**: Jedis (Redis)
- **MQ**: Apache ActiveMQ
- **Service Discovery**: Zookeeper

### New .NET Architecture
- **Framework**: ASP.NET Core 8.0
- **ORM**: Entity Framework Core 9.0
- **RPC**: gRPC (recommended) or REST
- **Cache**: StackExchange.Redis
- **MQ**: RabbitMQ or Azure Service Bus
- **Service Discovery**: Consul or custom implementation

## Step-by-Step Migration Process

### Phase 1: Infrastructure Setup (Completed)

✅ Create .NET solution structure
✅ Add necessary NuGet packages
✅ Configure logging with Serilog
✅ Set up basic API structure

### Phase 2: Data Layer Migration

#### 2.1 Database Models

Convert Java entity classes to C# models:

**Java (MyBatis):**
```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private Date createTime;
    // getters and setters
}
```

**C# (Entity Framework Core):**
```csharp
[Table("sys_user")]
public class User
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long Id { get; set; }
    
    [Required]
    [StringLength(50)]
    public string Username { get; set; } = string.Empty;
    
    [Required]
    [StringLength(200)]
    public string Password { get; set; } = string.Empty;
    
    public DateTime CreateTime { get; set; }
}
```

#### 2.2 Data Access Layer

Convert MyBatis mappers to Entity Framework repositories:

**Java (MyBatis Mapper):**
```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    User findByUsername(String username);
}
```

**C# (Repository Pattern):**
```csharp
public interface IUserRepository
{
    Task<User?> GetByUsernameAsync(string username);
    Task<User?> GetByIdAsync(long id);
    Task<IEnumerable<User>> GetAllAsync();
    Task AddAsync(User user);
    Task UpdateAsync(User user);
    Task DeleteAsync(long id);
}

public class UserRepository : IUserRepository
{
    private readonly ApplicationDbContext _context;

    public UserRepository(ApplicationDbContext context)
    {
        _context = context;
    }

    public async Task<User?> GetByUsernameAsync(string username)
    {
        return await _context.Users
            .FirstOrDefaultAsync(u => u.Username == username);
    }

    // Implement other methods...
}
```

#### 2.3 DbContext Configuration

Create and configure your DbContext:

```csharp
public class ApplicationDbContext : DbContext
{
    public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
        : base(options)
    {
    }

    public DbSet<User> Users { get; set; }
    public DbSet<Role> Roles { get; set; }
    public DbSet<Menu> Menus { get; set; }
    // Add other DbSets...

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        // Configure relationships
        modelBuilder.Entity<User>()
            .HasMany(u => u.Roles)
            .WithMany(r => r.Users)
            .UsingEntity(j => j.ToTable("sys_user_role"));

        // Configure indexes
        modelBuilder.Entity<User>()
            .HasIndex(u => u.Username)
            .IsUnique();
    }
}
```

### Phase 3: Business Logic Migration

#### 3.1 Service Layer

Convert Spring `@Service` classes to C# services:

**Java:**
```java
@Slf4j
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public User findByUsername(String username) {
        String cacheKey = "user:" + username;
        User user = (User) redisTemplate.opsForValue().get(cacheKey);
        if (user == null) {
            user = userMapper.findByUsername(username);
            if (user != null) {
                redisTemplate.opsForValue().set(cacheKey, user, 30, TimeUnit.MINUTES);
            }
        }
        return user;
    }
}
```

**C#:**
```csharp
public class UserService : IUserService
{
    private readonly IUserRepository _userRepository;
    private readonly IDistributedCache _cache;
    private readonly ILogger<UserService> _logger;

    public UserService(
        IUserRepository userRepository,
        IDistributedCache cache,
        ILogger<UserService> logger)
    {
        _userRepository = userRepository;
        _cache = cache;
        _logger = logger;
    }

    public async Task<User?> GetByUsernameAsync(string username)
    {
        var cacheKey = $"user:{username}";
        
        // Try to get from cache
        var cachedUser = await _cache.GetStringAsync(cacheKey);
        if (cachedUser != null)
        {
            return JsonSerializer.Deserialize<User>(cachedUser);
        }

        // Get from database
        var user = await _userRepository.GetByUsernameAsync(username);
        if (user != null)
        {
            // Cache for 30 minutes
            var options = new DistributedCacheEntryOptions
            {
                AbsoluteExpirationRelativeToNow = TimeSpan.FromMinutes(30)
            };
            await _cache.SetStringAsync(
                cacheKey,
                JsonSerializer.Serialize(user),
                options);
        }

        return user;
    }
}
```

#### 3.2 Register Services

In Program.cs:
```csharp
// Register repositories
builder.Services.AddScoped<IUserRepository, UserRepository>();
builder.Services.AddScoped<IRoleRepository, RoleRepository>();

// Register services
builder.Services.AddScoped<IUserService, UserService>();
builder.Services.AddScoped<IRoleService, RoleService>();
```

### Phase 4: Controller Migration

Convert Spring REST controllers to ASP.NET Core controllers:

**Java:**
```java
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        User created = userService.save(user);
        return ResponseEntity.ok(created);
    }
}
```

**C#:**
```csharp
[ApiController]
[Route("api/users")]
public class UserController : ControllerBase
{
    private readonly IUserService _userService;
    private readonly ILogger<UserController> _logger;

    public UserController(IUserService userService, ILogger<UserController> logger)
    {
        _userService = userService;
        _logger = logger;
    }

    [HttpGet("{id}")]
    [ProducesResponseType(typeof(User), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<User>> GetUser(long id)
    {
        var user = await _userService.GetByIdAsync(id);
        if (user == null)
        {
            return NotFound();
        }
        return Ok(user);
    }

    [HttpPost]
    [ProducesResponseType(typeof(User), StatusCodes.Status201Created)]
    [ProducesResponseType(StatusCodes.Status400BadRequest)]
    public async Task<ActionResult<User>> CreateUser([FromBody] CreateUserRequest request)
    {
        if (!ModelState.IsValid)
        {
            return BadRequest(ModelState);
        }

        var user = await _userService.CreateAsync(request);
        return CreatedAtAction(nameof(GetUser), new { id = user.Id }, user);
    }
}
```

### Phase 5: Middleware and Filters

#### 5.1 XSS Protection

**Java (Servlet Filter):**
```java
@Slf4j
@Order(1)
@WebFilter(filterName = "xssFilter", urlPatterns = "/*")
public class XssFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        XssHttpRequestWrapper xssRequest = new XssHttpRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }
}
```

**C# (Middleware):**
```csharp
public class XssProtectionMiddleware
{
    private readonly RequestDelegate _next;
    private readonly ILogger<XssProtectionMiddleware> _logger;

    public XssProtectionMiddleware(RequestDelegate next, ILogger<XssProtectionMiddleware> logger)
    {
        _next = next;
        _logger = logger;
    }

    public async Task InvokeAsync(HttpContext context)
    {
        // Add security headers
        context.Response.Headers.Add("X-Content-Type-Options", "nosniff");
        context.Response.Headers.Add("X-Frame-Options", "SAMEORIGIN");
        context.Response.Headers.Add("X-XSS-Protection", "1; mode=block");

        // Enable request buffering to allow multiple reads
        context.Request.EnableBuffering();

        // Validate request body if needed
        if (context.Request.ContentType?.Contains("application/json") == true)
        {
            // Implement XSS validation logic here
        }

        await _next(context);
    }
}
```

### Phase 6: Scheduled Tasks

**Java:**
```java
@Slf4j
@Component
public class ScheduledTasks {
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupTask() {
        log.info("Running cleanup task");
        // Task logic
    }
}
```

**C#:**
```csharp
public class CleanupBackgroundService : BackgroundService
{
    private readonly ILogger<CleanupBackgroundService> _logger;
    private readonly IServiceProvider _serviceProvider;

    public CleanupBackgroundService(
        ILogger<CleanupBackgroundService> logger,
        IServiceProvider serviceProvider)
    {
        _logger = logger;
        _serviceProvider = serviceProvider;
    }

    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        while (!stoppingToken.IsCancellationRequested)
        {
            // Calculate delay until 2 AM
            var now = DateTime.Now;
            var nextRun = now.Date.AddDays(1).AddHours(2);
            if (now.Hour < 2)
            {
                nextRun = now.Date.AddHours(2);
            }

            var delay = nextRun - now;
            await Task.Delay(delay, stoppingToken);

            _logger.LogInformation("Running cleanup task");
            
            using var scope = _serviceProvider.CreateScope();
            var service = scope.ServiceProvider.GetRequiredService<ICleanupService>();
            await service.CleanupAsync();
        }
    }
}
```

Register in Program.cs:
```csharp
builder.Services.AddHostedService<CleanupBackgroundService>();
```

### Phase 7: Configuration Migration

**Java (application.yml):**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/qzt_ump
    username: root
    password: password
  redis:
    host: localhost
    port: 6379
    password: redis_password
server:
  port: 8080
```

**C# (appsettings.json):**
```json
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=localhost;Port=3306;Database=qzt_ump;Uid=root;Pwd=password;",
    "Redis": "localhost:6379,password=redis_password"
  },
  "Kestrel": {
    "Endpoints": {
      "Http": {
        "Url": "http://localhost:8080"
      }
    }
  }
}
```

## Code Conversion Patterns

### Annotations to Attributes

| Java | C# |
|------|-----|
| `@Autowired` | Constructor injection |
| `@Component` / `@Service` | Register in DI container |
| `@RestController` | `[ApiController]` |
| `@RequestMapping` | `[Route]` |
| `@GetMapping` | `[HttpGet]` |
| `@PostMapping` | `[HttpPost]` |
| `@PathVariable` | `[FromRoute]` |
| `@RequestBody` | `[FromBody]` |
| `@RequestParam` | `[FromQuery]` |
| `@Validated` | `[FromBody]` with ModelState |
| `@Transactional` | Transaction scope or `[Transaction]` attribute |

### Common Type Conversions

| Java | C# |
|------|-----|
| `String` | `string` |
| `Integer` | `int` |
| `Long` | `long` |
| `Boolean` | `bool` |
| `Date` | `DateTime` |
| `List<T>` | `List<T>` or `IEnumerable<T>` |
| `Map<K,V>` | `Dictionary<K,V>` |
| `Optional<T>` | `T?` (nullable) |
| `void` | `void` or `Task` (async) |

### Async/Await Pattern

Java (CompletableFuture):
```java
public CompletableFuture<User> getUserAsync(Long id) {
    return CompletableFuture.supplyAsync(() -> userMapper.selectById(id));
}
```

C# (async/await):
```csharp
public async Task<User?> GetUserAsync(long id)
{
    return await _context.Users.FindAsync(id);
}
```

## Common Pitfalls and Solutions

### 1. Null Reference Handling

**Java:**
```java
User user = userService.findById(id);
if (user != null) {
    return user.getUsername();
}
return null;
```

**C# (with nullable reference types):**
```csharp
var user = await _userService.GetByIdAsync(id);
return user?.Username;  // Null-conditional operator
```

### 2. Collection Handling

**Java:**
```java
List<User> users = userMapper.selectList(null);
users.stream()
    .filter(u -> u.getAge() > 18)
    .map(User::getName)
    .collect(Collectors.toList());
```

**C# (LINQ):**
```csharp
var users = await _context.Users.ToListAsync();
var names = users
    .Where(u => u.Age > 18)
    .Select(u => u.Name)
    .ToList();
```

### 3. Exception Handling

**Java:**
```java
try {
    userService.save(user);
} catch (Exception e) {
    log.error("Error saving user", e);
    throw new BusinessException("Failed to save user");
}
```

**C#:**
```csharp
try
{
    await _userService.SaveAsync(user);
}
catch (Exception ex)
{
    _logger.LogError(ex, "Error saving user");
    throw new BusinessException("Failed to save user");
}
```

## Testing Strategy

### Unit Tests

**Java (JUnit + Mockito):**
```java
@Test
public void testFindUser() {
    when(userMapper.selectById(1L)).thenReturn(new User());
    User user = userService.findById(1L);
    assertNotNull(user);
}
```

**C# (xUnit + Moq):**
```csharp
[Fact]
public async Task GetUser_ReturnsUser()
{
    // Arrange
    var mockRepo = new Mock<IUserRepository>();
    mockRepo.Setup(r => r.GetByIdAsync(1)).ReturnsAsync(new User());
    var service = new UserService(mockRepo.Object, null, null);

    // Act
    var user = await service.GetByIdAsync(1);

    // Assert
    Assert.NotNull(user);
}
```

### Integration Tests

```csharp
public class UserControllerTests : IClassFixture<WebApplicationFactory<Program>>
{
    private readonly WebApplicationFactory<Program> _factory;
    private readonly HttpClient _client;

    public UserControllerTests(WebApplicationFactory<Program> factory)
    {
        _factory = factory;
        _client = factory.CreateClient();
    }

    [Fact]
    public async Task GetUser_ReturnsSuccessStatusCode()
    {
        // Act
        var response = await _client.GetAsync("/api/users/1");

        // Assert
        response.EnsureSuccessStatusCode();
    }
}
```

## Next Steps

1. **Phase 1-2**: Migrate database models and repositories
2. **Phase 3**: Migrate business services
3. **Phase 4**: Migrate controllers and API endpoints
4. **Phase 5**: Implement middleware and filters
5. **Phase 6**: Migrate scheduled tasks
6. **Phase 7**: Set up integration tests
7. **Phase 8**: Performance testing and optimization

## Additional Resources

- [ASP.NET Core to Spring Boot Mapping](https://docs.microsoft.com/aspnet/core/migration)
- [Entity Framework Core vs MyBatis](https://docs.microsoft.com/ef/core)
- [Dependency Injection in .NET](https://docs.microsoft.com/dotnet/core/extensions/dependency-injection)

## Support

For specific migration questions or issues, please refer to:
- .NET Documentation: https://docs.microsoft.com/dotnet
- Stack Overflow: https://stackoverflow.com/questions/tagged/.net-core
