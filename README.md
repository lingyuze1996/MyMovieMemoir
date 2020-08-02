# MyMovieMemoir
Want to have the best movie watching experience? With MyMovieMemoir, you can search movie, add in your watch list, and search for cinemas.
It gets better, you can share your and view other people's experience of watching a movie, helps you to decide where and what to watch.
You can also check how twitter reacted to the movie with our service.

——————

### Motivation

Me and Yuze have completed the FIT5046 final pratical assignment @Monash University. We were asked to build an andorid app with external APIs such as Google Maps and MovieDB.
We wanted to polish and deploy our Android app during this winter break (in Australia), to be able to proudly add this project to our resume also learn about Severless architecture and working knowledge of relevant AWS services.
We have decided to use Yuze's client side Android app. Yuncheng is responsible for building and configuring the infrastructure of deployment of the server side with AWS.Yuze has been the main drive force of the development and refinement.


### Architecture

AWS Serverless architecture contains:

1. Database: Amazon DynamoDB
2. AWS Lambda: runs code virtually without using server, which will replace the controller layer
3. Amazon API Gateway: Allows us to create endpoints without a server.
4. Amazon Cognito: Handles authentication of the APIs. User can be created in the user pool.

![Image of serverless architecture](https://github.com/lingyuze1996/MyMovieMemoir/blob/master/MyMovieMemoirArchitecture.png)
