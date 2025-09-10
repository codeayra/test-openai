## Spring AI Demo (OpenAI) — Gradle, Spring Boot 3

A minimal Spring Boot 3.5 app showcasing Spring AI ChatClient with OpenAI, prompt templates, and structured output conversion.

### Requirements
- **Java 17+** (toolchain configured in `build.gradle`)
- **Gradle Wrapper** (use provided `gradlew.bat` on Windows)
- An OpenAI-compatible API key

### Quick Start (Windows PowerShell)
1) Clone and enter the project directory.
2) Set environment variables (temporary for current session):
```powershell
$env:OPENAI_API_KEY = "your_openai_api_key"
# Optional: custom base URL (e.g., for Azure/OpenAI-compatible gateways)
$env:OPENAI_API_BASE_URL = "https://api.openai.com/v1"
```
To persist for future sessions (requires new PowerShell after running):
```powershell
setx OPENAI_API_KEY "your_openai_api_key"
setx OPENAI_API_BASE_URL "https://api.openai.com/v1"
```

3) Run the app:
```powershell
./gradlew.bat bootRun
```

The app reads defaults from `src/main/resources/application.properties`.

### Configuration
Key properties (override via environment variables shown in braces):
- `spring.ai.openai.api-key=${OPENAI_API_KEY}`
- `spring.ai.openai.api-base-url=${OPENAI_API_BASE_URL:https://api.openai.com/v1}`
- `spring.ai.openai.default-model=gpt-5-nano`
- `spring.ai.openai.timeout=30s`
- `spring.ai.openai.max-tokens=2048`

You can also package a runnable JAR:
```powershell
./gradlew.bat clean build
java -jar build/libs/test-openai-0.0.1-SNAPSHOT.jar
```

### REST Endpoints

- **ChatController**
  - `GET /ai?message=...` — simple text completion
  - `POST /chat?message=...` — simple text completion (POST)
  - `GET /stream?message=...` — server-side streaming text

- **PromptController**
  - `GET /ai/prompt?message=...` — send raw prompt
  - `GET /ai/books/popular?genre=...` — recommended books via inline template
  - `GET /ai/test/system-prompt?message=...` — uses system + user messages
  - `GET /ai/books/popular/prompt-template?genre=...` — uses `prompts/books.st`

- **OutputConverterController** (structured output)
  - `GET /ai/movies/artist/{actor}` — string content
  - `GET /ai/movies/artist/{actor}/list` — `List<String>` using `ListOutputConverter`
  - `GET /ai/movies/artist/{actor}/map` — `Map<String,Object>` via `MapOutputConverter`
  - `GET /ai/movies/artist/{actor}/object` — `Movie` object via `BeanOutputConverter`

- **OutputFluentController** (fluent API + entity mapping)
  - `GET /ai/movies-string` — string content (Tom Hanks)
  - `GET /ai/movies` — `Filmography` entity
  - `GET /ai/movies-list` — `List<Filmography>` for multiple actors
  - `GET /ai/movies-by-actor?actor=...` — `Filmography` for a given actor

- **PromptStuffController** (prompt template + optional stuffing)
  - `GET /ai/{city}/temprature?stuff=true|false` — uses `prompts/temprature.st` and optionally includes example context from `prompts/temprature-example.txt`

Static pages for quick manual testing:
- `GET /index.html`
- `GET /stream-index.html` (basic streaming demo)

### Example Requests (PowerShell + curl)
```powershell
curl "http://localhost:8080/ai?message=Hello%20there"

curl -X POST "http://localhost:8080/chat?message=Tell%20me%20a%20joke"

curl "http://localhost:8080/stream?message=Write%20a%20short%20poem" --no-buffer

curl "http://localhost:8080/ai/books/popular?genre=fantasy"

curl "http://localhost:8080/ai/movies/artist/Tom%20Hanks/list"

curl "http://localhost:8080/ai/movies-by-actor?actor=Leonardo%20DiCaprio"

curl "http://localhost:8080/ai/London/temprature?stuff=true"
```

### Project Structure
- `controller` — REST endpoints using Spring AI `ChatClient`
- `dto` — `Movie`, `Filmography` models used by converters/entity mapping
- `resources/prompts` — prompt template files consumed by controllers

### Testing
Run unit tests:
```powershell
./gradlew.bat test
```

### Notes
- This project uses Spring AI BOM and `spring-ai-starter-model-openai`. To switch providers, add the relevant starter and properties.
- Windows users should prefer `./gradlew.bat` over `gradlew`.


