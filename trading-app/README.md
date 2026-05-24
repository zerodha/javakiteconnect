# Kite Connect Trading App (MVP)

A web-based trading application built with Spring Boot and Kite Connect API for automated trading on Indian stock exchanges.

## Project Structure

```
trading-app/
├── pom.xml                          # Maven configuration with dependencies
├── src/
│   ├── main/
│   │   ├── java/com/example/kiteconnectapp/
│   │   │   ├── KiteConnectTradingApp.java    # Spring Boot entry point
│   │   │   ├── config/                       # Spring configurations
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── WebSocketConfig.java
│   │   │   ├── controller/                   # REST controllers
│   │   │   │   └── AuthController.java       # OAuth authentication endpoints
│   │   │   ├── service/                      # Business logic
│   │   │   │   └── AuthenticationService.java
│   │   │   ├── dto/                          # Data Transfer Objects
│   │   │   │   ├── GenerateSessionRequest.java
│   │   │   │   └── SessionResponse.java
│   │   │   ├── util/                         # Utilities
│   │   │   │   ├── KiteConnectWrapper.java   # KiteConnect initialization
│   │   │   │   └── TokenManager.java         # Token management
│   │   │   └── exception/
│   │   │       └── TradingAppException.java
│   │   └── resources/
│   │       ├── application.properties         # App configuration
│   │       └── static/                        # Static web resources
│   │           ├── index.html                 # Login page
│   │           ├── callback.html              # OAuth callback handler
│   │           ├── dashboard.html             # Main dashboard
│   │           ├── css/
│   │           │   └── style.css              # Shared styling
│   │           └── js/
│   │               └── api.js                 # API client utilities
│   └── test/
│       └── java/com/example/kiteconnectapp/
```

## Prerequisites

- **Java 11+** — Required for Spring Boot 2.7.x
- **Maven 3.6+** — Build tool
- **Kite Connect Account** — Get from [Zerodha](https://kite.zerodha.com)
- **API Key & Secret** — From Kite Connect Dashboard under App Tokens section

## Setup Instructions

### 1. Configure API Credentials

Edit `src/main/resources/application.properties`:

```properties
kite.api.key=YOUR_API_KEY_HERE
kite.api.secret=YOUR_API_SECRET_HERE
```

Or set environment variables:
```bash
export KITE_API_KEY=your_api_key
export KITE_API_SECRET=your_api_secret
```

### 2. Build the Application

```bash
cd trading-app
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or build and run JAR:
```bash
mvn clean package
java -jar target/kite-connect-trading-app-1.0.0.jar
```

### 4. Access the Application

- **Login Page**: http://localhost:8080/
- **Dashboard** (after login): http://localhost:8080/dashboard.html
- **API Documentation**: http://localhost:8080/swagger-ui.html (when Swagger is added in Phase 2)

## OAuth Authentication Flow

1. User clicks "Login with Kite" on login page
2. Backend generates Kite OAuth login URL
3. User is redirected to Kite OAuth page
4. User authenticates and grants permissions
5. Kite redirects to callback URL with `request_token`
6. App exchanges `request_token` for `access_token`
7. User is redirected to dashboard with authenticated session

## API Endpoints (Phase 1 - Authentication)

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/auth/login-url` | Get Kite OAuth login URL |
| POST | `/api/auth/session` | Generate session (exchange request token) |
| GET | `/api/auth/session` | Get current session details |
| POST | `/api/auth/logout` | Logout and clear tokens |

**Example: Get Login URL**
```bash
curl -X GET http://localhost:8080/api/auth/login-url
```

Response:
```json
{
  "loginUrl": "https://kite.zerodha.com/connect/login?v=3&api_key=YOUR_API_KEY"
}
```

**Example: Generate Session**
```bash
curl -X POST http://localhost:8080/api/auth/session \
  -H "Content-Type: application/json" \
  -d '{"requestToken":"request_token_from_kite"}'
```

Response:
```json
{
  "accessToken": "access_token_xyz",
  "publicToken": "public_token_xyz",
  "userId": "AB1234",
  "authenticated": true,
  "message": "Authentication successful"
}
```

## Configuration Properties

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | 8080 | Server port |
| `spring.session.store-type` | none | Session store (can be changed to redis/db in production) |
| `server.servlet.session.timeout` | 30m | Session timeout duration |
| `logging.level.com.example.kiteconnectapp` | DEBUG | Logging level |

## Development

### Adding New Controllers

Create controller in `com.example.kiteconnectapp.controller` package:

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {
        // Implementation
    }
}
```

### Adding New Services

Create service in `com.example.kiteconnectapp.service` package and inject into controllers.

### Adding DTOs

Create DTO classes in `com.example.kiteconnectapp.dto` package for request/response objects.

## Common Issues

### 1. Invalid API Key / Secret
- Verify credentials in `application.properties`
- Ensure they match your Kite Connect app settings
- Regenerate tokens if needed from Zerodha dashboard

### 2. Maven Build Failures
- Ensure Java 11+ is installed: `java -version`
- Check Maven: `mvn -v`
- Clear Maven cache: `mvn clean`

### 3. Port Already in Use
- Change port in `application.properties`: `server.port=8081`
- Or kill process using port 8080

### 4. Session/Token Issues
- Clear browser cookies
- Check browser console for errors: Press F12
- Enable debug logging in application.properties

## Next Phases (Planned)

- **Phase 2**: Core Trading APIs (Orders, Portfolio, Market Data)
- **Phase 3**: Real-Time WebSocket Tickers
- **Phase 4**: Frontend UI Components
- **Phase 5**: Margin Calculations
- **Phase 6**: Algorithm Bot Framework

## Dependencies

- **Spring Boot 2.7.14** — Web framework
- **Kite Connect 3.5.1** — Trading API client
- **Gson** — JSON serialization
- **Apache Commons Lang** — Utility functions
- **SLF4J** — Logging

## Error Handling

All API errors return JSON responses:

```json
{
  "success": false,
  "code": "ERROR_CODE",
  "message": "Human-readable error message"
}
```

Common error codes:
- `AUTH_ERROR` — Authentication/token exchange failed
- `VALIDATION_ERROR` — Request validation failed
- `INTERNAL_ERROR` — Server error

## Security Notes

⚠️ **Important for Production**:

1. Never commit `application.properties` with real credentials to version control
2. Use environment variables or secure vaults for sensitive data
3. Enable HTTPS in production
4. Enable CSRF protection
5. Implement rate limiting
6. Use strong session timeout values
7. Validate and sanitize all inputs
8. Use CORS carefully (currently allows all origins)

## Support

For issues with:
- **Kite Connect API**: See [Kite Documentation](https://kite.trade/docs/connect/v3)
- **Spring Boot**: See [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- **This App**: Check logs and error messages

## License

MIT License (same as javakiteconnect library)
