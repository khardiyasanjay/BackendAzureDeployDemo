The User Management API provides endpoints for managing user information. It allows you to load users from an external API, fetch users from a database, filter them based on roles, sort by age, and retrieve user details by ID or SSN.

Endpoints
1. POST /api/users/load
   Description: This endpoint loads users from an external API and stores them in the database.
   Request Body: None
   Response:
   Status: 200 OK
   Body: Users loaded successfully.
2. GET /api/users
   Description: Fetches a list of all users stored in the database.
   Response:
   Status: 200 OK
   Body: A list of all users in JSON format.
3. GET /api/users/role/{role}
   Description: Fetches users based on the role specified in the URL.
   Path Parameter:
   role: The role of the user (e.g., admin, user, manager, moderator).
   Response:
   Status: 200 OK
   Body: A list of users that have the specified role.
   Validation: The role parameter must be one of the following: admin, user, manager, moderator.
4. GET /api/users/sort
   Description: Fetches users sorted by age in ascending or descending order.
   Request Parameter:
   order: The sorting order (asc or desc). Default is asc.
   Response:
   Status: 200 OK
   Body: A list of users sorted by age.
5. GET /api/users/{idOrSsn}
   Description: Fetches a user based on either their ID or SSN.
   Path Parameter:
   idOrSsn: The ID or SSN of the user.
   Response:
   Status: 200 OK
   Body: The user details.
   Technologies Used
   Spring Boot: Framework for building the REST API.
   JPA (Hibernate): To interact with the database.
   H2 Database: In-memory database for storing users.
   RestTemplate: To fetch users from an external API.
   Validation: Spring annotations (@Valid, @Pattern) are used to validate input.
   Configuration
   The configuration properties are externalized in the application.properties file.

spring.datasource.url=jdbc:h2:mem:todo: Defines the in-memory H2 database.
external.api.url=https://dummyjson.com/users: The URL for the external API from where user data is fetched.
Steps to Build and Run
1. Clone the Repository or unzip the folder and load into ide
   bash
   Copy
   Edit
   git clone {repo url}
2. Build the Application
   Make sure you have Java 17+ installed and Maven is available in your PATH.

To build the project:

bash
Copy
Edit
cd user-management-api
mvn clean install
3. Run the Application
   To run the Spring Boot application:

bash
Copy
Edit
mvn spring-boot:run
The application will start on http://localhost:8080.

4. Test the Endpoints
   You can use tools like Postman or cURL to test the API endpoints:

Example of calling the /load endpoint:
bash
Copy
Edit
curl -X POST http://localhost:8080/api/users/load
Example of calling the /role/{role} endpoint:
bash
Copy
Edit
curl http://localhost:8080/api/users/role/admin
5. Access the API Documentation (Optional)
   If you are using Springfox or similar libraries for API documentation, you can access Swagger at:

bash
Copy
Edit
http://localhost:8080/swagger-ui/
This will provide interactive documentation of the available endpoints.

Testing
The application includes unit tests for all the service methods, including the validation logic.

To run the tests:

bash
Copy
Edit
mvn test

Error Handling
400 Bad Request: If the role parameter does not match any of the allowed values, or any other validation error occurs.
404 Not Found: If no users are found with the provided idOrSsn.
500 Internal Server Error: If there is an issue loading users from the external API or saving them to the database.
Contributors
Sanjay - Initial API design and implementation.