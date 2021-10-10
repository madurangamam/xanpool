# Xanpool assessment  #



#### Technology STACK

- Java 1.8
- Maven as build tool 3.8
- Spring Boot 2.5.4
- Junit
- REST Api
- Google Guava for caching
- Lombok

### Prerequisites

For building and running the application, you need::

- Java 1.8
- Maven

### Installing and Running

```
    $ mvn clean install  
    $ mvn package spring-boot:repackage  
    $ java -jar target/assessment-0.0.1-SNAPSHOT.jar 
```  
OR
```
    $ mvn clean install  
    $ mvn spring-boot:run  
``` 

- The application will be running on port 8081. If necessary, can change the port via ```application.properties```

### How it works ?

Send the request in following format:<br />

```http://localhost:8080/api/convert?access_key=?&from=?&to=?&amount=?``` <br />

Request Type : ``` GET ``` <br />

```access_key``` : Your access key to the API <br />
```from , to``` : Append the from and to parameters and set them to your preferred base and target currency codes.<br />
```amount ``` : Amount is optional.Represent the amount you want to convert.By default, amount is set to 1

Sample Request: <br />
```http://localhost:8080/api/convert?access_key=649a1b0b0f0a325f910650da84f7a604&from=USD&to=SGD&amount=4``` <br />


 For any clarification, 
please feel free to contact me via my email ```mamaduranga@gmail.com```

