# MyMovieMemoir
Want to have the best movie watching experience? With MyMovieMemoir, you can search movie, add in your watch list, and search for cinemas.
It gets better, you can share your and view other people's experience of watching a movie, helps you to decide where and what to watch.
You can also check how twitter reacted to the movie with our service.

——————

### Motivation

As we have completed the FIT5046 @Monash University assignment, we shall polish and deploy our Android app to be able to proudly add this project to our resume.
We have decided to use Yuze's client side Android app. Yuncheng is responsible for building the infrastructure of deployment of the server side with AWS serverless 
architecture.

The RestService in the repo is the Java web application Yuze has built for the first assessable part of the porject during the semester.
We are going to use the same database schema and test data after migrating it to cloud.


### Architecture
We would either use AWS serverless architecture

AWS server-less architecture contains:

1. Database: Amazon DynamoDB
2. AWS Lambda: runs code virtually without using server, which will replace the controller layer
3. Amazon API Gateway: Allows us to create endpoints without a server.
4. Amazon Cognito: Handles authentication of the APIs. User can be created in the user pool.

