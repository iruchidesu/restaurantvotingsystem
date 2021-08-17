[comment]: <> ([![Codacy Badge]&#40;https://app.codacy.com/project/badge/Grade/85c77aa024734ac5b06bc9d3418a97d0&#41;]&#40;https://www.codacy.com/gh/iruchidesu/topjava/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=iruchidesu/topjava&amp;utm_campaign=Badge_Grade&#41;)
[comment]: <> ([![Build Status]&#40;https://travis-ci.com/iruchidesu/topjava.svg?branch=master&#41;]&#40;https://travis-ci.com/iruchidesu/topjava&#41;)
Restaurant Voting System Project 
===============================

This is the REST API implementation of voting system for deciding where to have lunch.

### Technology stack used:
* Maven
* Spring Boot
* Spring Security
* Spring Cache
* Spring Data JPA(Hibernate)
* REST(Jackson)
* JUnit

### Project key logic:
* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

### API documentation:
#### Swagger documentation
- (/v2/api-docs)
- (/swagger-ui.html)
#### Users
- POST /rest/profile/sign-up (sign up a new user)
- GET /rest/profile (get user profile)
- PUT /rest/profile (update user profile)
- DELETE /rest/profile (delete user profile)
#### Admins
- POST /rest/admin/users (register a new user)
- GET /rest/admin/users (get all users)
- GET /rest/admin/users/{id} (get user profile)
- GET /rest/admin/users/by?email={email} (get user profile with email = email)
- PUT /rest/admin/users/{id} (update user profile with id = id)
- DELETE /rest/admin/users/{id} (delete user profile with id = id)
#### Restaurants
- POST /rest/restaurant (create a new restaurant)
- GET /rest/restaurant (get list of restaurants)
- GET /rest/restaurant/{id}/with-today-menu (get restaurant with id = id with today menu)
- GET /rest/restaurant/{id} (get restaurant with id = id without today menu)
- GET /rest/admin/users/by?name={name} (get restaurant with name = name)
- PUT /rest/restaurant/{id} (update restaurant with id = id)
- DELETE /rest/restaurant/{id} (delete restaurant with id = id)
#### Menus
- POST /rest/restaurant/{restaurantId}/menu (create today's menu for restaurant with id = restaurantId)
- GET /rest/restaurant/{restaurantId}/menu (get today's menu for restaurant with id = restaurantId)
- GET /rest/restaurant/{restaurantId}/menu/history (get menus list history for restaurant with id = restaurantId)
- PUT /rest/restaurant/{restaurantId}/menu (update today's menu for restaurant with id = restaurantId)
- DELETE /rest/restaurant/{restaurantId}/menu (delete today's menu for restaurant with id = restaurantId)
#### Votes
- POST /rest/profile/vote?restaurantId={restaurantId} (create vote from authorized user for restaurant with id = restaurantId)
- PUT /rest/profile/vote?restaurantId={restaurantId} (update today's vote from authorized user for restaurant with id = restaurantId)
- GET /rest/profile/vote (get today's vote for authorized user)
- GET /rest/profile/vote/all (get all vote for authorized user)
- DELETE /rest/profile/vote (delete today's vote for authorized user)

### Caching strategy
Spring caching (Ehcache provider):
- Get all restaurants (singleNonExpiryCache, evicts when create/update/delete any restaurant)
- Get today's menu for a restaurant (expiryCache, cache key = {restaurantId} + currentDate, evicts by key, when create/update/delete today's menu for the restaurant, and when the restaurant deletes)

### Curl commands to test API:
#### sign up a new User
`curl -s -i -X POST -d '{"name": "New User","email": "new-user@mail.ru","password": "new-password"}' -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/rest/profile/sign-up`
#### get User profile
`curl -s http://localhost:8080/rest/profile --user new-user@mail.ru:new-password`
#### update User profile
`curl -s -X PUT -d '{"name":"Updated User","email":"updated@mail.ru","password":"updated-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/profile --user new-user@mail.ru:new-password`
#### delete User profile
`curl -s -X DELETE http://localhost:8080/rest/profile --user updated@mail.ru:updated-password`
#### register not valid user
`curl -s -i -X POST -d '{"name":"New User","password":"new-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/profile/sign-up`
#### get User profile unAuthorized
`curl -s http://localhost:8080/rest/profile`

#### sign up a new User via admin
`curl -s -i -X POST -d '{"name":"New User2","email":"new-user2@mail.ru","password":"new-password2"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/admin/users --user admin@gmail.com:admin`
#### get all users via admin
`curl -s http://localhost:8080/rest/admin/users --user admin@gmail.com:admin`
#### get user with id=100000 via admin 
`curl -s http://localhost:8080/rest/admin/users/100000 --user admin@gmail.com:admin`
#### update User via admin
`curl -s -X PUT -d '{"name":"Updated User2","email":"updated2@mail.ru","password":"updated-password2"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/admin/users/100000 --user admin@gmail.com:admin`
#### delete User with id=100000 via admin 
`curl -s -X DELETE http://localhost:8080/rest/admin/users/100000 --user admin@gmail.com:admin`
#### create not valid user via admin
`curl -s -i -X POST -d '{"name":"New User","password":"new-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/admin/users --user admin@gmail.com:admin`
#### get User unAuthorized via admin
`curl -s http://localhost:8080/rest/admin/users/100001`

#### get all Restaurants
`curl -s http://localhost:8080/rest/restaurant --user user@yandex.ru:password`
#### get Restaurant with id=100002 with today menu
`curl -s http://localhost:8080/rest/restaurant/100002/with-today-menu --user user@yandex.ru:password`
#### get Restaurant with id=100002 without today menu
`curl -s http://localhost:8080/rest/restaurant/100002 --user user@yandex.ru:password`
#### get Restaurant with name=rest1
`curl -s http://localhost:8080/rest/restaurant/by?name=rest1 --user user@yandex.ru:password`
#### create a new Restaurant
`curl -s -i -X POST -d '{"name":"New Restaurant","address":"New address"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurant --user admin@gmail.com:admin`
#### update Restaurant with id=100002
`curl -s -X PUT -d '{"name":"Updated Restaurant","address":"Updated address"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurant/100002 --user admin@gmail.com:admin`
#### delete Restaurant with id=100003
`curl -s -X DELETE http://localhost:8080/rest/restaurant/100003 --user admin@gmail.com:admin`
#### create not valid restaurant
`curl -s -i -X POST -d '{"name":"","address":"New address"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurant --user admin@gmail.com:admin`
#### delete restaurant forbidden
`curl -s -X DELETE http://localhost:8080/rest/restaurant/100002 --user user@yandex.ru:password`

#### get today's Menu for Restaurant with id = 100002
`curl -s http://localhost:8080/rest/restaurant/100002/menu --user user@yandex.ru:password`
#### get menus list history for restaurant with id = 100002
`curl -s http://localhost:8080/rest/restaurant/100002/menu/history --user user@yandex.ru:password`
#### create not valid today's menu for Restaurant with id=100003
`curl -s -i -X POST -d '{"dishes":[{"name":"dish1","price":20000},{"name":"dish1","price":30000}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurant/100003/menu --user admin@gmail.com:admin`
#### create today's Menu for Restaurant with id=100003
`curl -s -i -X POST -d '{"dishes":[{"name":"dish1","price":20000},{"name":"dish2","price":30000}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurant/100003/menu --user admin@gmail.com:admin`
#### update today's Menu for Restaurant with id=100002
`curl -s -X PUT -d '{"dishes":[{"name":"dish1 Updated","price":25000},{"name":"dish2 Updated","price":35000}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurant/100002/menu --user admin@gmail.com:admin`
#### delete today's menu unAuthorized
`curl -s -X DELETE http://localhost:8080/rest/restaurant/100002/menu`
#### delete today's Menu for Restaurant with id=100002
`curl -s -X DELETE http://localhost:8080/rest/restaurant/100002/menu --user admin@gmail.com:admin`

#### vote for today's menu of Restaurant with id=100003
`curl -s -i -X POST http://localhost:8080/rest/profile/vote?restaurantId=100003 --user admin@gmail.com:admin`
#### update vote for today's menu of Restaurant with id=100002
`curl -s -X PUT http://localhost:8080/rest/profile/vote?restaurantId=100002 --user admin@gmail.com:admin`
#### get today's vote for authorized user
`curl -s http://localhost:8080/rest/profile/vote --user user@yandex.ru:password`
#### get all vote for authorized user
`curl -s http://localhost:8080/rest/profile/vote/all --user user@yandex.ru:password`
#### delete today's vote for authorized user
`curl -s -X DELETE http://localhost:8080/rest/profile/vote --user user@yandex.ru:password`
