# CSC180 Final Project - Song Application

A JavaFX desktop application for managing and discovering songs, built as a final project for CSC180.

## Features

- User authentication (login/signup)
- Song database management
- Web scraping for song data
- Interactive user interface
- MySQL database integration

## Technologies Used

- **Java 18** - Core programming language
- **JavaFX 18.0.2** - Desktop application framework
- **Maven** - Build automation and dependency management
- **MySQL 8.0.32** - Database for user and song data
- **JSoup 1.14.3** - Web scraping library
- **Apache HttpClient 4.5.13** - HTTP client for web requests
- **JUnit 5.10.2** - Testing framework

## Prerequisites

- Java 18 or higher
- Maven 3.6+
- MySQL server
- JavaFX runtime (if not included with your JDK)

## Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/snxethan/CSC180-FINAL.git
cd CSC180-FINAL
```

### 2. Database Setup
Make sure you have MySQL running and create the necessary database and tables for the application.

### 3. Build the Project
```bash
mvn clean compile
```

### 4. Run the Application
```bash
mvn javafx:run
```

Alternatively, you can use the Maven wrapper:
```bash
./mvnw javafx:run
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── csc180/townsend/ethan/finalcsc180/
│   │       ├── Controller/
│   │       │   ├── DatabaseController.java    # Database operations
│   │       │   ├── HomeViewController.java    # Home screen controller
│   │       │   ├── LoginController.java       # Login screen controller
│   │       │   ├── SignupController.java      # Signup screen controller
│   │       │   ├── Scraper/                   # Web scraping components
│   │       │   └── Validator/                 # Input validation
│   │       ├── SongApplication.java           # Main application class
│   │       └── ChangeScene.java              # Scene management utility
│   └── resources/                            # FXML files and resources
└── test/                                     # Unit tests
```

## Application Flow

1. **Login Screen** - Users can log in with existing credentials or navigate to signup
2. **Signup Screen** - New users can create accounts
3. **Home Screen** - Main application interface for song management
4. **Database Integration** - All user data and songs are stored in MySQL
5. **Web Scraping** - Application can scrape song data from external sources

## Development

### Running Tests
```bash
mvn test
```

### Building Distribution
```bash
mvn clean package
```

## Configuration

The application uses a modular structure defined in `module-info.java` and requires the following modules:
- `javafx.controls`
- `javafx.fxml`
- `java.sql`
- `org.jsoup`
- `org.apache.httpcomponents.httpcore`
- `org.apache.httpcomponents.httpclient`

## Author(s)

- [**Ethan Townsend (snxethan)**](www.ethantownsend.dev)
