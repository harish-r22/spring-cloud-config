# spring-cloud-config

Spring Cloud Config Analysis and Architecture
Spring Cloud Config provides server and client-side support for externalized configuration in a distributed system, With the Config Server, you have a central place to manage external properties for applications across all environments. 
Features
Spring Cloud Config Server features:
•	HTTP, resource-based API for external configuration (name-value pairs, or equivalent YAML content)
•	Encrypt and decrypt property values (symmetric or asymmetric)
•	Embeddable easily in a Spring Boot application using @EnableConfigServer
Config Client features (for Spring applications):
•	Bind to the Config Server and initialize Spring Environment with remote property sources
•	Encrypt and decrypt property values (symmetric or asymmetric).
As long as Spring Boot Actuator and Spring Config Client are on the classpath any Spring Boot application will try to contact a config server on http://localhost:8888, the default value of spring.cloud.config.uri. If you would like to change this default, you can set spring.cloud.config.uri in bootstrap.[yml | properties] or via system properties or environment variables.

@Configuration
@EnableAutoConfiguration
@RestController
public class Application {

  @Value("${config.name}")
  String name = "World";

  @RequestMapping("/")
  public String home() {
    return "Hello " + name;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}




Architecture diagram for product service and other spring boot application 

![image](https://user-images.githubusercontent.com/93630979/169794948-9d55f4bc-6095-468a-bdbd-1c0fe671c082.png)

 
Current Implementation: Config client ->product-service
Config server -> Spring config server (New Service)
Data Store->Git
Adding actuator dependency for refresh config and get updated config data

Spring Cloud config with using the core project.
Through the core java classes, we can do database read and write action from the spring cloud config application
![image](https://user-images.githubusercontent.com/93630979/169795150-b126f6d9-d72a-46eb-a848-25c9e52558dd.png)







Analysis based on Magellan project 

![image](https://user-images.githubusercontent.com/93630979/169795341-424b203f-9d7b-451b-95f2-3428553187c7.png)

1)	Adding core dependency to spring cloud config app (new service), and exposing endpoints for view config and update config.
2)	Application-specific config comes directly from git.
3)	Core config remains as its (Database)
4)	Application-specific config come from git
5)	Core-specific config remain has its.
6)	This not gone affect old architecture
https://www.youtube.com/watch?v=x0wC3SH631U
https://cloud.spring.io/spring-cloud-config/reference/html/#config-client-fail-fast

Advantages of the above Cloud config server :

1)	Database dependency can be easily removed in feature
2)	We can avoid writing redundant code to create a Magellan object.
3)	One-time update to multiple clients’ services.
4)	All the configs provided by the server only (including core config).
5)	Externalized App config which is maintain in git
Disadvantages in the current framework;
1)	All the config maintained in the database which leads to the maintenance effort
2)	And each client app has a separate endpoint to refresh config.




![image](https://user-images.githubusercontent.com/93630979/169795414-9934ec94-2091-453b-a527-12292ee27368.png)






Spring Cloud Bus -> on client app
When we make changes in configuration, and the changes are called on any of the instances, the microservice sends an event over the Spring Cloud Bus. The Spring Cloud Bus propagates that event to all the microservice instances that are registered with it.
<dependency> <groupId>org.springframework.cloud</groupId> <artifactId>spring-cloud-starter-bus-amqp</artifactId> <version>2.2.1.RELEASE</version> </dependency>
To complete config client changes we need to add RabbitMQ details and enable cloud bus in an application.yml file:
Now, the client will have another endpoint ‘/bus-refresh'. Calling this endpoint will cause:

 

Spring cloud config Server
Finally, let's add two dependencies to config server to automate configuration changes fully.

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-monitor</artifactId>
    <version>2.2.2.RELEASE</version>
</dependency>





<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
    <version>3.0.4.RELEASE</version>
</dependency>

The most recent version can be found here.

We will use spring-cloud-config-monitor to monitor configuration changes and broadcast events using RabbitMQ as transport.

We just need to update application.properties and give RabbitMQ details:







