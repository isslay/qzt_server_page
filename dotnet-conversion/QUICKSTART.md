# Quick Start Guide - .NET 8.0 Conversion

## For Developers New to .NET

This guide helps you get started with the converted .NET 8.0 application if you're coming from the Java/Spring Boot version.

## Prerequisites Installation

### 1. Install .NET 8.0 SDK

**Windows:**
- Download from: https://dotnet.microsoft.com/download/dotnet/8.0
- Run the installer
- Verify installation: Open Command Prompt and run `dotnet --version`

**macOS:**
- Download from: https://dotnet.microsoft.com/download/dotnet/8.0
- Or use Homebrew: `brew install dotnet-sdk`
- Verify: `dotnet --version`

**Linux (Ubuntu/Debian):**
```bash
wget https://dot.net/v1/dotnet-install.sh -O dotnet-install.sh
chmod +x dotnet-install.sh
./dotnet-install.sh --channel 8.0
```

### 2. Install Visual Studio 2022

**Windows:**
- Download Visual Studio 2022 Community (free): https://visualstudio.microsoft.com/vs/
- During installation, select "ASP.NET and web development" workload
- Also select ".NET desktop development" workload

**Alternative IDEs:**
- **Visual Studio Code**: Free, cross-platform
  - Install from: https://code.visualstudio.com/
  - Add C# extension: https://marketplace.visualstudio.com/items?itemName=ms-dotnettools.csharp
- **JetBrains Rider**: Paid, similar to IntelliJ IDEA
  - Download from: https://www.jetbrains.com/rider/

### 3. Install MySQL

Same as Java version - no changes needed.

### 4. Install Redis

Same as Java version - no changes needed.

## Opening the Project

### In Visual Studio 2022:

1. Open Visual Studio 2022
2. Click **File** → **Open** → **Project/Solution**
3. Navigate to `dotnet-conversion/QztServerPage.sln`
4. Click **Open**

Visual Studio will automatically:
- Restore NuGet packages (equivalent to Maven dependencies)
- Build the solution
- Show all projects in Solution Explorer

### In Visual Studio Code:

1. Open VS Code
2. Click **File** → **Open Folder**
3. Select the `dotnet-conversion` folder
4. VS Code will prompt to install recommended extensions - accept
5. Open integrated terminal: **View** → **Terminal**
6. Run: `dotnet restore` to restore packages

## Configuration

### Database Configuration

Edit `appsettings.json` in each project:

**Qzt.Ump.RpcService/appsettings.json:**
```json
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=localhost;Port=3306;Database=qzt_ump;Uid=root;Pwd=YOUR_PASSWORD_HERE;",
    "Redis": "localhost:6379,password=YOUR_REDIS_PASSWORD"
  }
}
```

Repeat for `Qzt.Ump.ServerBack` and `Qzt.Ump.ServerWeb`.

**Note:** Unlike Java's `application.yml`, .NET uses JSON format.

### Environment-Specific Configuration

- `appsettings.json` - Base configuration (like application.yml)
- `appsettings.Development.json` - Development overrides (like application-dev.yml)
- `appsettings.Production.json` - Production overrides (you can create this)

## Running the Applications

### Method 1: Visual Studio 2022 (Easiest)

#### Run All Three Services:
1. Right-click solution in Solution Explorer
2. Select **Properties**
3. Go to **Common Properties** → **Startup Project**
4. Select **Multiple startup projects**
5. Set all three services (RpcService, ServerBack, ServerWeb) to **Start**
6. Click **OK**
7. Press **F5** to start debugging (or Ctrl+F5 for no debugging)

All three services will start in separate console windows.

#### Run One Service:
1. Right-click on a specific project (e.g., Qzt.Ump.ServerWeb)
2. Select **Set as Startup Project**
3. Press **F5**

### Method 2: Command Line

Open three terminal windows:

**Terminal 1 - RPC Service:**
```bash
cd dotnet-conversion/Qzt.Ump.RpcService
dotnet run
```

**Terminal 2 - Server Back:**
```bash
cd dotnet-conversion/Qzt.Ump.ServerBack
dotnet run
```

**Terminal 3 - Server Web:**
```bash
cd dotnet-conversion/Qzt.Ump.ServerWeb
dotnet run
```

### Method 3: Using Scripts

**Windows:**
```cmd
cd dotnet-conversion
start-all-services.bat
```

**Linux/macOS:**
```bash
cd dotnet-conversion
./start-all-services.sh
```

## Accessing the Applications

Once running, access the services:

| Service | URL | Swagger/OpenAPI |
|---------|-----|-----------------|
| RPC Service | http://localhost:8001 | http://localhost:8001/swagger |
| Server Back | http://localhost:8002 | http://localhost:8002/swagger |
| Server Web | http://localhost:8003 | http://localhost:8003/swagger |

The Swagger UI is equivalent to the Java version's Swagger2 interface.

## Common Tasks

### Add a NuGet Package (equivalent to Maven dependency)

**In Visual Studio:**
1. Right-click project → **Manage NuGet Packages**
2. Search for package
3. Click **Install**

**Command Line:**
```bash
cd Qzt.Ump.ServerWeb
dotnet add package PackageName
```

### Build the Solution

**Visual Studio:** Press Ctrl+Shift+B

**Command Line:**
```bash
cd dotnet-conversion
dotnet build
```

### Clean and Rebuild

**Visual Studio:** **Build** → **Clean Solution**, then **Build** → **Build Solution**

**Command Line:**
```bash
dotnet clean
dotnet build
```

### Create Production Build

```bash
cd dotnet-conversion
dotnet publish -c Release -o ./publish
```

This creates optimized, ready-to-deploy files in `./publish/` directory.

## Debugging in Visual Studio

### Set Breakpoints:
1. Click in the left margin of code editor (gray bar)
2. Red dot appears = breakpoint set
3. Press F5 to start debugging
4. Execution pauses at breakpoint

### Debug Controls:
- **F5** - Continue/Start
- **F10** - Step Over
- **F11** - Step Into
- **Shift+F11** - Step Out
- **Shift+F5** - Stop Debugging

### Watch Variables:
- Hover over variables to see values
- Use **Watch** window: **Debug** → **Windows** → **Watch**
- Use **Locals** window: **Debug** → **Windows** → **Locals**

## Troubleshooting

### "dotnet command not found"
- Ensure .NET SDK is installed
- Restart terminal/command prompt
- Check PATH environment variable includes .NET SDK

### Build Errors
- Ensure all NuGet packages are restored: `dotnet restore`
- Check that .NET 8.0 SDK is installed: `dotnet --version`
- Clean solution: `dotnet clean`

### Port Already in Use
- Change port in `appsettings.json`:
```json
{
  "Kestrel": {
    "Endpoints": {
      "Http": {
        "Url": "http://localhost:8999"  // Change port number
      }
    }
  }
}
```

### Cannot Connect to MySQL/Redis
- Verify MySQL and Redis are running
- Check connection strings in `appsettings.json`
- Test connection with MySQL client: `mysql -u root -p`
- Test Redis: `redis-cli ping`

## Key Differences from Java Version

### File Organization
| Java | .NET |
|------|------|
| `src/main/java/` | Root of project |
| `src/main/resources/` | Project root or `Resources/` |
| `pom.xml` | `.csproj` file |
| `application.yml` | `appsettings.json` |
| `.jar` files | `.dll` files |

### Command Comparison
| Task | Java (Maven) | .NET |
|------|--------------|------|
| Build | `mvn clean package` | `dotnet build` |
| Run | `java -jar app.jar` | `dotnet run` or `dotnet app.dll` |
| Test | `mvn test` | `dotnet test` |
| Clean | `mvn clean` | `dotnet clean` |
| Add dependency | Edit `pom.xml` | `dotnet add package` |

### Code Conventions
| Java | C# |
|------|-----|
| `myVariable` | `myVariable` (same) |
| `MyClass` | `MyClass` (same) |
| `myMethod()` | `MyMethod()` (PascalCase for methods) |
| `IMyInterface` | `IMyInterface` (same) |
| Getters/Setters | Properties: `{ get; set; }` |

## Next Steps

1. **Read the README.md** for comprehensive setup information
2. **Read MIGRATION_GUIDE.md** for code migration patterns
3. **Explore the code** in Visual Studio - IntelliSense will help you understand APIs
4. **Check Swagger UI** to see available endpoints
5. **Start migrating business logic** from Java to C#

## Learning Resources

### Official Documentation
- [.NET Documentation](https://docs.microsoft.com/dotnet)
- [ASP.NET Core Tutorial](https://docs.microsoft.com/aspnet/core/tutorials)
- [C# Programming Guide](https://docs.microsoft.com/dotnet/csharp/programming-guide)
- [Entity Framework Core](https://docs.microsoft.com/ef/core)

### Video Tutorials
- [.NET for Java Developers](https://www.youtube.com/results?search_query=dotnet+for+java+developers)
- [ASP.NET Core Crash Course](https://www.youtube.com/results?search_query=asp.net+core+crash+course)

### Books
- "C# in Depth" by Jon Skeet
- "ASP.NET Core in Action" by Andrew Lock

## Getting Help

- **Stack Overflow**: Tag questions with `c#`, `asp.net-core`, or `entity-framework-core`
- **.NET Discord**: https://aka.ms/dotnet-discord
- **GitHub Issues**: For project-specific issues

## Summary

You now have a working .NET 8.0 project structure that mirrors your Java/Spring Boot application. The foundational infrastructure (web server, database access, caching, logging, etc.) is in place. The next phase is migrating your specific business logic, controllers, and services.

Good luck with your migration! 🚀
