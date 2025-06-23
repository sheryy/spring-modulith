# Spring Modulith Event-Driven Architecture Demo

This project demonstrates how to implement **Event-Driven Architecture (EDA)** using **Spring Modulith**, showcasing loose coupling between modules through domain events, reliable event processing, and modular design principles.

## 🎯 Project Overview

This demo simulates a **blog post management system** with three main modules:
- **Post Module**: Core domain for creating and managing posts
- **Publisher Module**: Automatically publishes posts when created
- **Archiver Module**: Archives posts after they are published

The system demonstrates how modules can communicate asynchronously through events without direct dependencies, following the principles of Domain-Driven Design (DDD) and Event-Driven Architecture.

## 🏗️ Architecture Overview

```
┌─────────────────┐    PostCreated Event    ┌─────────────────┐
│   Post Module   │ ──────────────────────► │ Publisher Module│
│                 │                         │                 │
│ • Create Post   │                         │ • Auto Publish  │
│ • Publish Post  │                         │ • Handle Events │
│ • Archive Post  │                         └─────────────────┘
└─────────────────┘                                │
                                                   │ PostPublished Event
                                                   ▼
                                          ┌─────────────────┐
                                          │ Archiver Module │
                                          │                 │
                                          │ • Auto Archive  │
                                          │ • Handle Events │
                                          └─────────────────┘
```

## 📁 Project Structure

```
src/main/java/dev/sheraz/events/
├── EventsApplication.java              # Main Spring Boot application
├── post/                              # Post Module
│   ├── domain/                        # Domain layer
│   │   ├── Post.java                  # Post entity
│   │   ├── PostService.java           # Domain service
│   │   ├── PostRepository.java        # Repository interface
│   │   └── PostCreateJob.java         # Scheduled job to create posts
│   ├── event/                         # Domain events
│   │   ├── PostCreated.java           # Event when post is created
│   │   └── PostPublished.java         # Event when post is published
│   └── exception/                     # Domain exceptions
├── publisher/                         # Publisher Module
│   └── PublisherEventListener.java    # Listens to PostCreated events
├── archiver/                          # Archiver Module
│   └── ArchiverEventListener.java     # Listens to PostPublished events
└── common/                            # Shared components
    ├── config/                        # Configuration
    │   └── SchedulerConfig.java       # Scheduler and ShedLock config
    ├── job/                           # Background jobs
    │   └── RepublishEventJob.java     # Event republishing job
    └── Random.java                    # Utility for simulating failures
```

## 🔧 Key Components

### 1. Post Module (`post/`)

**Core domain module** that manages blog posts:

- **Post Entity**: JPA entity with status lifecycle (CREATED → PUBLISHED → ARCHIVED)
- **PostService**: Domain service that publishes events after state changes
- **PostCreateJob**: Scheduled job that automatically creates sample posts every 15 seconds
- **Domain Events**: `PostCreated` and `PostPublished` events

```java
// Example: Creating a post triggers an event
void create(Post post) {
    Post createdPost = postRepository.save(post);
    events.publishEvent(new PostCreated(createdPost.id())); // Event published
}
```

### 2. Publisher Module (`publisher/`)

**Independent module** that listens to post creation events:

- **PublisherEventListener**: Automatically publishes posts when `PostCreated` events are received
- **Loose Coupling**: No direct dependency on the Post module
- **Failure Simulation**: 30% chance of random failures to demonstrate reliability

```java
@ApplicationModuleListener
public void on(PostCreated event) {
    random.exception(0.3); // 30% chance of failure
    log.info("PostCreated event: {}", event.id());
    postService.publish(event.id()); // Triggers PostPublished event
}
```

### 3. Archiver Module (`archiver/`)

**Independent module** that archives published posts:

- **ArchiverEventListener**: Listens to `PostPublished` events and archives posts
- **Asynchronous Processing**: Handles events independently
- **Failure Simulation**: 30% chance of random failures

```java
@ApplicationModuleListener
public void on(PostPublished event) {
    random.exception(0.3); // 30% chance of failure
    log.info("PostPublished event: {}", event.id());
    postService.archive(event.id());
}
```

### 4. Event Reliability (`common/job/`)

**Ensures event processing reliability**:

- **RepublishEventJob**: Automatically retries failed event publications
- **ShedLock Integration**: Prevents duplicate job execution in clustered environments
- **Configurable Retry**: Retries events older than 5 seconds

```java
@Scheduled(fixedDelay = 5_000)
@SchedulerLock(name = "republishEvents")
public void runJob1() {
    incompleteEventPublications.resubmitIncompletePublicationsOlderThan(duration);
}
```

## 🚀 Getting Started

### Prerequisites

- Java 24
- Maven 3.6+
- Docker and Docker Compose

### Running the Application

1. **Clone and navigate to the project**:
   ```bash
   cd spring-modulith
   ```

2. **Ensure Docker daemon is running**:
   The application will automatically start PostgreSQL via Docker Compose when you run the application.

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```
   
   Spring Boot will automatically:
   - Start the PostgreSQL container using Docker Compose
   - Initialize the database schema
   - Start the application

4. **Monitor the logs** to see the event-driven workflow:
   The application logs to the console. You'll see output like:
   ```
   Posts created (5): task-1
   PostCreated event: 1
   PostPublished event: 1
   Running republishEvents: task-2
   ```

### What You'll See

The application will automatically:

1. **Create posts** every 15 seconds (2-10 posts per batch)
2. **Publish posts** when `PostCreated` events are processed
3. **Archive posts** when `PostPublished` events are processed
4. **Retry failed events** every 5 seconds

The logs will appear directly in your console as the application runs.

## 🧪 Testing

### Modularity Verification

Run the modularity test to verify the module structure:

```bash
mvn test -Dtest=ModularityTest#verifyModularStructure
```

### Documentation Generation

Generate module documentation:

```bash
mvn test -Dtest=ModularityTest#generateDocumentation
```

This creates documentation in `target/spring-modulith-docs/`.

## 🔍 Key Features Demonstrated

### 1. **Modular Design**
- Clear module boundaries with `@NamedInterface`
- No circular dependencies between modules
- Each module has its own package structure

### 2. **Event-Driven Communication**
- Modules communicate through domain events
- Loose coupling between modules
- Asynchronous event processing

### 3. **Reliable Event Processing**
- Event persistence in database
- Automatic retry of failed events
- ShedLock for distributed job coordination

### 4. **Failure Handling**
- Simulated random failures (30% probability)
- Automatic event republishing
- Graceful degradation

### 5. **Scheduled Jobs**
- Automatic post creation every 15 seconds
- Event republishing every 5 seconds
- Distributed job coordination with ShedLock

## 📊 Database Schema

The application uses PostgreSQL with the following key tables:

- **`posts`**: Stores blog posts with status lifecycle
- **`event_publication`**: Persists events for reliable processing
- **`shedlock`**: Coordinates distributed job execution

## ⚙️ Configuration

Key configuration in `application.properties`:

```properties
# Event processing
spring.modulith.events.completion-mode=delete

# Job scheduling
spring.task.scheduling.pool.size=5

# Database
spring.sql.init.mode=always
spring.jpa.show-sql=false
```

## 🎓 Learning Outcomes

This demo showcases:

1. **Spring Modulith** for modular application design
2. **Event-Driven Architecture** principles
3. **Reliable event processing** with persistence and retry
4. **Domain-Driven Design** with bounded contexts
5. **Distributed job coordination** with ShedLock
6. **Asynchronous communication** between modules

## 🔗 Related Resources

- [Spring Modulith Documentation](https://docs.spring.io/spring-modulith/reference/)
- [Event-Driven Architecture Patterns](https://martinfowler.com/articles/201701-event-driven.html)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)

## 🤝 Contributing

Feel free to extend this demo with additional modules, events, or features to explore more advanced Spring Modulith capabilities! 
