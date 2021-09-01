[![Codacy Badge](https://app.codacy.com/project/badge/Grade/2bc78c25851e4898b8e952f94cfcf967)](https://www.codacy.com/gh/iruchidesu/restaurantvotingsystem/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=iruchidesu/restaurantvotingsystem&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.com/iruchidesu/restaurantvotingsystem.svg?branch=master)](https://travis-ci.com/iruchidesu/restaurantvotingsystem)
Restaurant Voting System Project
=================================
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

* (/v3/api-docs/REST%20API)
* (/swagger-ui.html)

#### Preset users

* Admin (Username: admin@gmail.com, password: admin)
* User (Username: user@yandex.ru, password: password)

#### Users

* POST /rest/profile (sign up a new user)
* GET /rest/profile (get user profile)
* PUT /rest/profile (update user profile)
* DELETE /rest/profile (delete user profile)

#### Users management

* POST /rest/admin/users (register a new user)
* GET /rest/admin/users (get all users)
* GET /rest/admin/users/{id} (get user profile)
* GET /rest/admin/users/by?email={email} (get user profile with email = email)
* PUT /rest/admin/users/{id} (update user profile with id = id)
* DELETE /rest/admin/users/{id} (delete user profile with id = id)
* PATCH /rest/admin/users/{id} (enable/disable user profile with id = id)

#### Restaurants

* POST /rest/restaurants (create a new restaurant)
* GET /rest/restaurants (get list of restaurants)
* GET /rest/restaurants/with-today-menu (get list of restaurants with today's menu)
* GET /rest/restaurants/{id} (get restaurant with id = id without today menu)
* GET /rest/restaurants/by?name={name} (get restaurant with name = name)
* PUT /rest/restaurants/{id} (update restaurant with id = id)
* DELETE /rest/restaurants/{id} (delete restaurant with id = id)

#### Menus

* POST /rest/restaurants/{restaurantId}/menus (create today's menu for restaurant with id = restaurantId)
* GET /rest/restaurants/{restaurantId}/menus/by?date={date} (get menu on date for restaurant with id = restaurantId)
* GET /rest/restaurants/{restaurantId}/menus (get menus list history for restaurant with id = restaurantId)
* PUT /rest/restaurants/{restaurantId}/menus (update today's menu for restaurant with id = restaurantId)
* DELETE /rest/restaurants/{restaurantId}/menus (delete today's menu for restaurant with id = restaurantId)

#### Votes

* POST /rest/profile/votes?restaurantId={restaurantId} (create vote from authorized user for restaurant with id =
  restaurantId)
* PUT /rest/profile/votes?restaurantId={restaurantId} (update today's vote from authorized user for restaurant with id =
  restaurantId)
* GET /rest/profile/votes/by?date={date} (get vote for authorized user on date)
* GET /rest/profile/votes (get all vote for authorized user)
* DELETE /rest/profile/votes (delete today's vote for authorized user)

### Caching strategy

Spring caching (Ehcache provider):

* Get all restaurants (singleNonExpiryCache, evicts when create/update/delete any restaurant)
* Get today's menu for a restaurant (expiryCache, cache key = {restaurantId} + currentDate, evicts by key, when
  create/update/delete today's menu for the restaurant, and when the restaurant deletes)

<a href="curl.md">Curl commands to test API</a>
