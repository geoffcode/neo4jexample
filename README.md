# neo4jexample
An example API for communicating with Neo4j.
This project provides:
* A REST API for CRUD operations on Employee records.
* A sample UI (using Thymeleaf) to drive the API.

# REST API
The REST endpoint is available at `:8080/rest/employees` by default. 
The supported interactions are shown below.
Note that currently, more than one Employee can share the same ID as there is no database constraint.

| URL                  | Method | Input                        | Output                                        |
|----------------------|--------|------------------------------|-----------------------------------------------|
| /rest/employees      | GET    |                              | JSON Array of all employees                   |
| /rest/employees/{id} | GET    | Employee ID                  | JSON Array of all employees with the given ID |
| /rest/employees      | POST   | Employee JSON Representation | Echoes the Employee that was added            |
| /rest/employees      | DELETE |                              | Deletes all Employee records                  |
| /rest/employees/{id} | DELETE | Employee ID                  |                                               |

# Employee JSON Representation
```json
{"emp_id":1, "name":"Agent Smith", "email":"agent.smith@neo4j.com"}
```

# UI Application
The UI application can be viewed at `:8080/ui/employees` by default.

![alt text](https://raw.githubusercontent.com/geoffcode/neo4jexample/master/images/ui.png "UI Application")

# To run the application
Assumes Neo4j instance is running on `localhost` and the Bolt driver can connect to `bolt://localhost:7687`

To build the project: `mvn clean package`

To run the server: `java -jar neo4jexample-{version}.jar`
