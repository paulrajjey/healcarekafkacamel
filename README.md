# Apache Camel Kafka Example

This application is a Spring boot example for connecting Apache Camel to Apache
Kafka for Producing and Consuming messages.

## Running

To get the application going:
```
$ mvn clean install # Build the project
$ sudo kafka-server-start config/server.properties # Start Kafka
$ java -jar target/ApacheCamelKafka-v0.0.1.war --server.port=8280 # Start the war
```
#server.contextPath=/javapointers

## Messaging with Kafka

Run the API and hit /messages with a GET

Run the API and hit /messages with a POST body of JSON:
```
{
  "title": "Simple Notification",
  "body": "The simple notification body!"
}
```
