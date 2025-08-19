# movie-booking

This is a project only for exercise. This is based on a system design interview of Microsoft based on my own experience.

### Overview
It's a online booking system for movie theatres, users can get available movies online, and place orders.  
#### Type of application: Monolithic  
Although the interviewer asked how to deal with large amount of request, supposed it's a large system connected to multiple
theatres and serving for tens of thousand of people. Because it's for exercise only I don't apply micro services to it to save my time.

#### Tech stacks
Spring Boot for web service and dependency injection.  
Mybatis ORM for accessing MySQL database.  
JSON web token technique and Spring Security for authentication.  
JUnit for unit testing and Mockito for mocking services.   
Docker for building images to publish.

#### Access:
[Click to Go](https://moviebookingprod.youtiao.dev)  
Though it's called prod, there shouldn't be anything to be booked in real world, and the payment page is only a placeholder.  

#### CI:  
Jenkins is used to perform continuous integration. It's connected to gitlab with webhook so on every push, it will trigger a build.
On each build, it will run unit tests, do sonar scan and build docker image, then finally push to harbor registry. Pipeline is defined in Jenkinsfile.  
[Click to access CI](https://jenkins.youtiao.dev)  
I have opened read-only access to visitors.  

#### Configuration Management  
Because I want to try cloud-managed configuration, I have deployed a [nacos](https://nacos.io/en/) system by Alibaba on the kubernetes cluster.  
The application will access the nacos at: nacos-0.nacos-headless.nacos.svc.cluster.local:8848
```properties
spring.datasource.url=jdbc:mysql://192.168.*.*:3306/movie_booking
spring.datasource.username=*****
spring.datasource.password=*****
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
security.jwt.privkey=<base64 encoded PKCS8 RSA Private key>
security.jwt.pubkey=<base64 encoded X509 RSA Public key>
```

#### Deployment:
The application is deployed on a kubernetes cluster on my all-in-one server.  
It creates:   
Namespace 'movie-booking-{ENV}', while ENV can be 'devk8s' and 'prod'. All other things are created under this.  
Secret for pulling images from harbor.  
Ingress for exposing service. The ingress-nginx controller is created already.  
Deployment of pods and services in cluster.  

### Monitoring
To be done lol.  
I think it will be created later someday.  
