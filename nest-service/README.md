# Nest

How to start the Nest application
---

1. Start a local H2 database server and connect to `jdbc:h2:tcp://localhost/~/db/nest` with user `nest` and password `DevIg$2016`
1. Run `mvn clean install -DskipTests` to build your application
1. Run migrations with `java -jar target/nest-service-1.0.0.jar db migrate config.yml`
1. Run tests using `mvn test`
1. Start application with `java -jar target/nest-service-1.0.0.jar server config.yml`
1. To check that your application is running enter URL `http://localhost:8080`

Health Check
---

To see your applications health enter URL `http://localhost:8081/healthcheck`
