# multi-thread-web-service
This is a web service developed using jersey to handle multiple concurrent API requests


## How to Run


Clone the web service and deploy it a web server


## Usefull Commands

Build the war file : 

```
mvn clean install
```

Run the test cases:

```
mvn test -Djersey.test.host=localhost -Djersey.config.test.container.port=8080
```


## Sample API requests

```
curl -d 50 http://localhost:8080
curl -d 100 http://localhost:8080
curl -d end http://localhost:8080
```

Expected result for above request is the sum of all the numbers until the request with terminating character 'end'
