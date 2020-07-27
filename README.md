# Getting Started

### Prerequisites to run the application on your machine
* Java 11 and Maven
###  Run application 
* mvn spring-boot:run
     <br /> (or) 
* Run StoremanagerApplication class as java application from IDE
     <br /> (or) 
* docker build using Dockerfile 
   NOTE : Make sure "mvn package" step is executed before docker build  
   docker build --quiet --build-arg ENVIRONMENT=local  --tag creditlimittracker:1.0.0 .
   docker run -d -p 8080:8080 creditlimittracker:1.0.0
 
### Using the application
* Open http://localhost:8080/ to see home page with the retrieved users from files sorted by Name  <br />
* The Latitude & Longitude input values are populated by current geo location when you allow the browser to access your computer geo location  <br />
* Click on Submit button to see five near by Jumbo stores based on Latitude and Longitude  <br />
 
 Application has all the crud API's to manage Stores  

### API Swagger documentation 
 * Link to API documentation (http://localhost:8080/swagger-ui/index.html#/)

## Why?

We are interested in your skills as a developer. As part of our assessment, we want to see your code.

## Instructions

In this archive, you'll find two files, `Workbook2.csv` and `Workbook2.prn`, that represent exports from legacy 
systems. You should consider your work to be a proof of concept for a system that can keep track of credit limits from 
several sources. Your users will access your application through a browser and they do not expect a sophisticated user 
interface. The output should be served by a web server provided by your application. No particular HTML style is 
required. 

Your solution does not need to be production-ready and you will likely find yourself having to make some trade-offs 
here and there. A reviewer should still be able to follow your thoughts for such trade-offs or major design decisions 
or shortcuts you took. It would be helpful if you could briefly describe such things in the README or comments in the 
source code.

It should build, work and be readable, but there’s not binding code style to use.

Feel free to use any library or framework that you think adds value to your project. Document steps to build the 
project from scratch in the README.

This repository is created specially for you, so you can push anything you like. Please update this README to provide 
instructions, notes and/or comments for us.

## The Constraints

The solution has to be implemented in Java and we expect this to be done within a week.
    
## Questions?

If you have any questions please ask dl-ecg-technical-assessment@ebay.com.

## Finished?

Make sure the steps to run the solution are provided in the Readme. Then, please send an email to 
`dl-ecg-technical-assessment@ebay.com` to let us know you're done.

Have fun!


Copyright (C) 1995 - 2019 by eBay Inc. All rights reserved.