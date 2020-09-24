
## URl of the game
Here you can start playing this first version of the game [Minesweeper](http://ec2-3-134-99-212.us-east-2.compute.amazonaws.com/)

## TODO What is missing and known issues
Due to I prioritized the delivery of the Game as soon as possible, and not having so much time to work on this Test, I 
did this game with several drawbacks and things that need to be improved.

## [Backend]
* Add more Unit tests for Backends and for algorithms.  
* Improve the algorithm for showing adjacent cells, when the board is bigger than 100*100 it's causing a Stack Over Flow Error, 
  this is done due to recursion, and the way of how arguments are being passed. 
* Add better logging considering all levels.
  
## [API]
* Improve documentation done in Swagger, put more examples how it's used and the responses.
* Better Error handling for the Rest API, add all the http error codes accordingly. 
* Don't return all values of the entity to the API calls.
* Add better logging considering all levels.

## [UI]
* Unit tests for UI part. 
* Improve UI code using a Framework and improving visualization (I'm not a UI dev, so I did what I know..)
* Endpoint to consume should be taken from a configuration files instead of beign hardcoded internally in the code. 
* Improve performance how the data is being managed in the page and rendering board.
* Add better logging considering all levels.

## [Testing]
* The game need to run mores tests to find some scenarios of failing and tests all component altogether. 

## [Devops]
* Deployment of the Game in EC2 Instance was done manually, the spring-boot application need to run automatically and 
detect any failure in order to recover. Use a container.
* The instance used is the free tier that has some limitations.  
