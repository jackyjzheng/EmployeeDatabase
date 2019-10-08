**Employee Rest Database**

---

Application is a Java 8 REST server storing employee information that can be retrieved/modified via HTTP calls. Application is using Jersey servlet framework.

**EmployeeDatabase uses singleton design pattern to ensure only one instance exists, all that's necessary for simple application**

**Run the project by importing pom.xml as a Maven project in Eclipse/Intellij using Java 1.8 as SDK**

**On startup application asks for default security credentials which is used to authenticate DELETE calls**

**Furthermore, server will read from a CSV file in the project root directory to populate database with employees. Everyline shoud be of the format: id,firstName,middleInitial,lastName,birthDate,employmentDate**

---

* **URL:**

  ​	localhost:8080/kenzan/employees

*  **Method:**

  * `GET` | `POST`

---

- **URL:**

  ​	localhost:8080/kenzan/employees/{id}

-  **Method:**

  - `GET` | `PUT` | `DELETE`

---

`GET` localhost:8080/kenzan/employees

	- Returns a list of employees in database

`GET` localhost:8080/kenzan/employees/{id}

- Returns employee at request id. Response either NOT_FOUND or OK

`POST`  localhost:8080/kenzan/employees{id} 

* Creates employee, POST request must include an Employee JSON object in  body with the following fields:
  * {
        "id": "4",
        "firstName": "Jacky",
        "middleInitial": "J",
        "lastName": "Zheng",
        "dateOfBirth": "1997-02-10",
        "dateOfEmployment": "2019-10-10"
    }
  * If there is already an employee with that id, it will return Response CONFLICT else CREATED

`PUT` localhost:8080/kenzan/employees/{id}

* Updates employee at that id by either creating or replacing with employee you give. Attach employee in JSON as Request Body just like with POST
* If there is already an employee at that id, replaces with Response OK, else CREATED

`DELETE` localhost:8080/kenzan/employees/{id}

* Deletes the employee at that id. Requires following fields in header for authentication:
  * "key":"hash","value":"MeqHKblkMFnT6fcoSKdQJh=="
  * "key":"username","value":"Jacky"
  * "key":"time","value":"1570509502000"
* Username and Password are set at server startup. The authentication hash is simply md5(user+password+currentTime)
* Prevents man in the middle by not revealing password, can counterattack replay attacks by making sure that the time included and the server time don't differ by a large amount.
* Returns Response NOT_AUTHORIZED if credentials not correct, OK if success and NOT_FOUND if id not in database
* Use python script computeCredentials to generate valid hash

