# Agent Guide for Musk Framework

## Build and Test Commands
- Build entire project: `mvn clean install`
- Build single module: `mvn clean install -pl musk-module-name`
- Skip tests: `mvn clean install -DskipTests`
- Run tests: `mvn test`
- Run single test: `mvn test -pl module-name -Dtest=TestClassName#testMethodName`
- Run Spring Boot app: `mvn spring-boot:run -pl musk-admin/musk-admin-system`

## Code Style Guidelines
- Java version: Java 21
- Framework: Spring Boot 3.x with Maven multi-module structure
- Naming: 
  - Classes: PascalCase with suffix (DO/DTO/VO/Service/Controller)
  - Methods: camelCase
  - Variables: camelCase
  - Constants: UPPER_SNAKE_CASE
- Documentation: JavaDoc for classes and public methods
- Imports: Organized by type - java, javax, org, com
- Error handling: Use appropriate exceptions and validate inputs
- Testing: JUnit 5 with @SpringBootTest for integration tests
- Structure: Follow existing module architecture
- Security: Never expose sensitive data in logs or responses


1. Core development principles
Faithful design, precise implementation: Strictly follow the established architecture design documents, module interface definitions, data models and interaction processes for coding. If ambiguity, omissions or potential problems are found in the design, they must be raised in time and communicated with the designer for confirmation. Unauthorized modification of the core design is prohibited.
20 years of code craft perspective: The written code must not only run correctly, but also be easy to understand, maintain, and expand, with good readability and robustness. Strive to write elegant, concise and efficient code.
Keep the design principles in place: Continue to implement the "high cohesion, low coupling" principle at the class and method level to ensure that the code has a single responsibility and clear dependencies.

2. Coding standards and style
Follow established patterns: Implement specific implementations according to the excellent design patterns (such as factory pattern, strategy pattern, observer pattern, etc.) clearly defined in the design phase to ensure the correctness of the pattern application and the suitability of the scenario.
Code reuse and encapsulation:
Actively identify reusable business logic or technical functions and encapsulate them into independent tool classes, service methods or components.
Prioritize the use of common modules/libraries already available within the team, understand and correctly use the functions they provide, and avoid reinventing the wheel.
Style consistency: Follow the existing code style, naming conventions, and annotation specifications of the project.
Lombok specification usage: In the entity class, if it is only a simple attribute get/set, constructor, toString, equals/hashCode, etc., Lombok corresponding annotations (such as `@Data`, `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`) should be used to simplify the code.
Eliminate hard coding:
Business-related configurations, magic values, fixed strings, etc. should be defined and managed using constants (`public static final`), enumeration classes, or unified configuration management methods (such as Spring `@Value`, configuration files).
The attributes in the enumeration class must contain at least one field with business description significance and provide the necessary constructors and getters.

3. Code quality and robustness
Exception handling:
Capture and handle foreseeable exceptions, provide clear error information or execute appropriate fallback/compensation logic.
Do not abuse `try-catch` to avoid swallowing exceptions; custom exceptions should inherit the appropriate base class and carry sufficient context information.
Logging:
Record meaningful logs at key business nodes, important state changes, and exceptions, including necessary context information (such as request parameters, user ID, TraceID, etc.).
Use log levels (DEBUG, INFO, WARN, ERROR) reasonably.
Parameter verification: Strict validity verification (such as non-empty, format, range, etc.) of external inputs (such as API input parameters and method public input parameters).
Resource management: Ensure the correct creation, use and timely release of resources such as IO streams, database connections, and thread pools (such as using `try-with-resources`).
Concurrency safety: In scenarios involving shared resources or concurrent operations, thread safety issues must be fully considered, and locks, concurrent collections, or lock-free solutions must be used reasonably.
Performance awareness: When implementing core functions and hotspot paths, pay attention to code execution efficiency and avoid unnecessary performance bottlenecks (such as DB queries in loops, frequent creation of large objects, etc.), but do not over-optimize prematurely.

IV. Testing and verification
Testable coding: The written code should be easy to unit test, avoiding complex static dependencies and components that are difficult to mock.
Unit testing (if required):
If the project has mandatory requirements or the features are complex and important, high-quality unit tests need to be written to cover the main logical branches and boundary conditions.
Test cases should be clear, independent, and repeatable.
(As described in the design prompt: If it is not mandatory to generate a test class, then there is no need to generate a test class and test method).

V. Collaboration and output
Chinese expression: Comments in the code (class comments, method comments, complex logic block comments), commit messages, etc., should be expressed clearly in Chinese as much as possible.
Code expressiveness:
If you implement complex algorithms or logic, you can use some pseudocode or flowcharts as auxiliary comments to explain the ideas, and then give refined actual code.
The final output should be a Java code that is executable and compliant with the specification.
Forward compatibility is not considered: According to the instructions in the design phase, the current development does not need to consider compatibility with the old version of the system.
Modular development: If a new module is added in the design phase, it should be developed under the correct module and ensure the correct configuration of the dependencies between modules.

VI. Solution decision and feedback
Implementation solution selection: If there are multiple feasible ways in the specific implementation details (for example, the implementation of a tool class, the choice of a small algorithm), their advantages and disadvantages (such as readability, performance, complexity) should be evaluated, and the solution that best meets the current design principles and quality requirements should be selected.
Problem feedback: During the development process, if the design is not clear, or there are significant difficulties/risks in the implementation, or a better local implementation idea is found, you should actively communicate with the designer or team.