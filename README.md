# Spring Boot Application READ ME
### Overview
This Spring Boot application provides a REST API for managing transactions. It includes Swagger for API documentation and supports basic CRUD operations.

### Swagger URL
The Swagger documentation for this API is accessible at:

`
http://localhost:8080/swagger-ui/index.html
`
# Running the Application
To run the Spring Boot application, use the following command:

`
mvn spring-boot:run
`

Alternatively, you can package the application as a JAR file and run it with:

`
java -jar target/<your-application-name>.jar
`
# API Endpoints

#### 1. Update Transaction
##### Endpoint
`
PUT /transactions/{transactionId}
`

##### Request Body:
`
{
"description": "String"
}
`
##### Description : 
This endpoint updates the description of a specific transaction identified by transactionId.


#### 2. Get Transactions
##### Endpoint

`
GET /transactions
`
##### Request Param:

* customerId : (String) The ID of the customer.
* accountNumber : (String) The account number.
* description : (String) The description to filter transactions.

##### Request Body:
`
{
"page": 0,
"size": 1,
"sort": [
"ASC"
]
}
`
##### Description :
This endpoint retrieves a paginated list of transactions based on the provided query parameters.


## Additional Notes

* Ensure the application is configured to run on port 8080 or modify the Swagger URL if the port is changed.
* Authentication and authorization can be added as required for enhanced security.
* Use appropriate request headers such as Content-Type: application/json for API calls.

For further questions or issues, please refer to the Swagger documentation or contact the development team.


