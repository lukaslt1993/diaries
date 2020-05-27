# diaries
Exploring back-end capabilities of Spring Boot

It's a CRUD diary application with Spring Security, Hibernate and Liquibase database schema versioning plugin.

I used postgresql, but you could comment or delete [application.properties](https://github.com/lukaslt1993/diaries/blob/master/src/main/resources/application.properties), and automatically created h2 database will be used instead.

## Usage examples

You could use [Postman](https://www.postman.com/) or similar tool for sending requests.

### Registering
Send a POST request to localhost:8080/register with a following JSON in the request's body:\
{
  "email": "aaa<span>@bbb.com",
  "password": "abc"
}

### Logging in
Just use basic authentication with user __aaa<span>@bbb.com__ and password __abc__\
Once you are logged in, you don't have to use the authentication anymore, the server will remember you by session cookie.

### Creating a record in a diary
Send a POST request to localhost:8080/diaries with a following JSON in the request's body:\
{
  "title": "title1",
  "text": "text1"
}

### Reading all records
Send a GET request to localhost:8080/diaries

### Updating a record
Send a PUT request to localhost:8080/diaries/__record_id__ with a following JSON:\
{
  "title": "title11",
  "text": "text11"
}\
ID is assigned automatically when you create record. First record's id will be 1, second record's - 2 etc.

### Deleting a record
Send a DELETE request to localhost:8080/diaries/__record_id__
