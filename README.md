# Message Transformer

Message Transformer is a Spring Boot application that receives a message through REST and returns
the result.

## Requirements

- Java JDK 11 or newer
- Maven 3.6 or newer
- Docker 19 or newer

## IDE

- IntelliJ IDEA

## REST API

### Request

```
{
    "message": "<MESSAGE TO BE TRANSFORMED>",
    "messageDataType": "<TYPE OF THE TRANSFORMER TO BE USED>"
}
```

Supported *messageDataType*:
- WORD_REVERSAL

### Response

*200 OK*

```
{
    "result": "<TRANSFORMED MESSAGE>>"
}
```

*4XX ERROR*

```
{
    "error": "<ERROR MESSAGE>"
}
```

## Development

### Testing

To test the application you first need to start the the docker container in the docker folder:

```javascript
$ cd docker
$ docker-compose up
```

To run the application in development you need to run the application with the DEV profile. To do this you can type the
following in the terminal:

```javascript
$ mvn clean install
$ java -jar message-transformer-0.01-SNAPSHOT.jar --spring.profiles.active=DEV
```

It is also possible to run the application in your IDE if that is the preferred

When you are done testing and want to shut down don't forget to stop and remove the docker container. This can
be done by typing the following:

```javascript
$ cd docker
$ docker-compose down
```