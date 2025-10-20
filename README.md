# java REST API Kaiburr Assessment - Task 1

This repository has the code of a Java-based REST API that was developed during the Kaiburr assessment. The application is created with the help of the framework **Spring Boot**, it handles the task objects and stores all the information in a database based on the framework **MongoDB**. The API offers a complete range of endpoints to create, search, delete, and execute tasks, which are formulated as shell commands.

---

## Project Overview

The application architecture is designed based on the **Spring Boot** framework with **Maven** as a dependency management tool. The following POJOs (data model) with an annotation of the format of the tasks (Workflow) are defined as data models (**Task.java** and **TaskExecution.java**): It has a **TaskRepository** access point, that is based on **MongoRepository** and employs all the standard **CRUD (Create, Read, Update, Delete)** functions and a custom query method (**findByNameContaining**) to allow browsing by name.

All the needed API endpoints are opened by a central **TaskController** (marked with an annotation of annotation **RestController**). This controller processes all the HTTP requests and authenticates the input as well as coordinates the business logic. One of the most notable ones is the endpoint of the type of **PUT /tasks**, which is accompanied by a security verification step. The incoming command string is then checked against a denylist of unsafe keywords (e.g., soda, rm, |, and &) with the help of a **CommandValidator** utility class, which saves the task in case an unsafe command is found.

The other fundamental property is the endpoint of the method of executing the tasks of a specified id, which is the endpoint of the following type: **PUT /tasks / {id)/execute**. This approach retrieves a task by its ID and executes its stored command by means of **Java Processbuilder** which is set to run on the host OS ( **cmd.exe /c** to execute a Windows command ). The endpoint logic takes the standard output or error stream of the command, spawns a new **TaskExecution** object with the start, end time and the output and appends the execution object to the task execution history. The new task object is stored once again in the database.

## How to Run

**Java (Java Development Kit 17 or later)** and an instance of **MongoDB** should be installed and running locally to run this application. By default, the application is configured to connect to a local MongoDB instance on a port of 27017 by default in the **application.properties** located in the resources directory to use the **kaiburr-db** database. It is possible to start the application by executing the main method of the **KaiburrTaskApplication.java**. The embedded Tomcat server will be accessible at the port **8080** of the localhost once it is started.

---

## API Endpoint Tests (Postman)

Below are the Postman screenshots demonstrating the functionality of all required API endpoints, tested in sequence.

### 1. Test: Create a Task (PUT /tasks)

<img width="956" height="453" alt="1" src="https://github.com/user-attachments/assets/203615be-c623-4da7-9132-d183094e7d43" />


### 2. Test: Get All Tasks (GET /tasks)

<img width="956" height="455" alt="2" src="https://github.com/user-attachments/assets/585eb0de-7ac2-4b34-b737-5ec26b314bfc" />


### 3. Test: Get Task by ID (GET /tasks?id=123)

<img width="959" height="453" alt="3" src="https://github.com/user-attachments/assets/1edbf92e-b34a-4c89-bb0d-74e415603201" />


### 4. Test: Find Task by Name (GET /tasks/findByName)

<img width="958" height="456" alt="4" src="https://github.com/user-attachments/assets/3af1dc89-cfed-4ad9-bcab-10ea0e346d91" />


### 5. Test: Execute Task (PUT /tasks/123/execute)

<img width="959" height="455" alt="5" src="https://github.com/user-attachments/assets/08ee6ca4-bacf-4aed-abb7-67ff26f32b48" />


### 6. Test: Delete Task (DELETE /tasks/123)

<img width="959" height="455" alt="6" src="https://github.com/user-attachments/assets/c5dedef3-d961-476f-ad96-6ee9759d728b" />


### 7. Test: Verify Deletion (GET /tasks)

<img width="959" height="455" alt="7" src="https://github.com/user-attachments/assets/bc18edb1-4f45-465d-905d-d98292fd36b1" />

