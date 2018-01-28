# Discussions microservice - LozziKit

## Introduction

This microservice has been developed as part of the course AMT from the HEIG-VD (HES-SO) swiss university. 

This service helps you to deploy a discussion system service. Once the server has been started, it exposes some REST endpoints your frontend can request to comment, react or retrieve all of the comments of a particular _articleID_ you define yourself. 

## Basic ideas

You have developed an app or a website and you want to add some kind of interaction between the users you have. Your are dealing with user authorization with JWTs (JSON Web Token) - maybe using the _User and Team_ microservice from Lozzikit. (https://github.com/LozziKit/microservice-users-and-teams) - and now want the user to be able to comment on some of the pages of your app. 

### Commenting

With this service, your user will be able to comment on a specific page or article, defined by an integer _articleID_. When a user wants to comment a specific article, he will POST to the _/comments_ endpoints, with the _articleID_ as a parameter. The fact that it is the first comment or not does not make any difference: you don't have to deal with article creation. 

### Reacting

A user can react to a comment that has been already posted. For now, this service only handle upvotes, but is ready to handle more (like "dislike", "angry", etc.). When a user wants to react to a comment, the ID of the comment is passed as a path parameter when doing a POST to the _comments/{id}/reactions_ endpoint. The user can DELETE some of the reactions he has made too. 

### Authorization

This service uses JWTs to let a user comment, react or modify a comment he has already posted. The JWT has to be within the _Authorization_ HTTP header, as a _Bearer_ token. The payload of a minimal JWT has to look like this:

```
{
  "sub": "remij1", // The username
  "userID": "4" // The userID
}
```

The username will be used to display an author's name, but the user is really defined by the userID.

## How to deploy

_Docker_ has been used to deploy this app easily. For more informations about its installation and execution, please follow https://www.docker.com/.

Once _Docker_ has been installed, clone this repo and go to the _/docker_ folder. From there, execute this command:

```
docker-compose up --build
``` 

Everything should now be up and running, including the server exposing the REST endpoints and the database persisting the comments. 

_**Using Docker for Windows**_: you will have to use the _Docker Quickstart Terminal_ for the _docker-compose_ up to work properly.

_**Using it on linux or Mac OS**_: you will have to change the IP of the database from the code and recompile the whole project. You will have to modify the file located at _/swagger/spring-server/src/main/resources/application.properties_ like so:
### Before
```
spring.datasource.url = jdbc:mysql://192.168.99.100:3306/microservice-discussions?useSSL=false 
```
### After
```
spring.datasource.url = jdbc:mysql://localhost:3306/microservice-discussions?useSSL=false 
```



## REST API

Before cloning this repo, you may want to check all of the endpoints this server exposes. Theses are defined publicly here: https://app.swaggerhub.com/apis/HEIG-VD/microservice-discussion_api/0.0.1#/

It mainly exposes these endpoints:
 - _/comments_: used to POST, DELETE, PUT or GET comments.
 - _/comments/{id}/reactions_: used to POST, DELETE or GET reactions for a specific comment. 