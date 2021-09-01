### Curl commands to test API:

#### sign up a new User

`curl -s -i -X POST -d '{"name": "New User","email": "new-user@mail.ru","password": "new-password"}' -H 'Content-Type: application/json;charset=UTF-8' http://localhost:8080/rest/profile`

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

`curl -s -i -X POST -d '{"name":"New User","email":"newMail@mail.ru","password":"new-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/admin/users --user admin@gmail.com:admin`

#### get User unAuthorized via admin

`curl -s http://localhost:8080/rest/admin/users/100001`

#### disable user

`curl -s -i -X PATCH http://localhost:8080/rest/admin/users/100000?enabled=false --user admin@gmail.com:admin`

#### get all Restaurants

`curl -s http://localhost:8080/rest/restaurants --user user@yandex.ru:password`

#### get all restaurants with today's menu

`curl -s http://localhost:8080/rest/restaurants/with-today-menu --user user@yandex.ru:password`

#### get Restaurant with id=100002 without today menu

`curl -s http://localhost:8080/rest/restaurants/100002 --user user@yandex.ru:password`

#### get Restaurant with name=rest1

`curl -s http://localhost:8080/rest/restaurants/by?name=rest1 --user user@yandex.ru:password`

#### create a new Restaurant

`curl -s -i -X POST -d '{"name":"New Restaurant","address":"New address"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurants --user admin@gmail.com:admin`

#### update Restaurant with id=100002

`curl -s -X PUT -d '{"name":"Updated Restaurant","address":"Updated address"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurants/100002 --user admin@gmail.com:admin`

#### delete Restaurant with id=100003

`curl -s -X DELETE http://localhost:8080/rest/restaurants/100003 --user admin@gmail.com:admin`

#### create not valid restaurant

`curl -s -i -X POST -d '{"name":"","address":"New address"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurants --user admin@gmail.com:admin`

#### delete restaurant forbidden

`curl -s -X DELETE http://localhost:8080/rest/restaurants/100002 --user user@yandex.ru:password`

#### get menu on 2021-07-17 for Restaurant with id = 100002

`curl -s http://localhost:8080/rest/restaurants/100002/menus/by?date=2021-07-17 --user user@yandex.ru:password`

#### get menus list history for restaurant with id = 100002

`curl -s http://localhost:8080/rest/restaurants/100002/menus --user user@yandex.ru:password`

#### create not valid today's menu for Restaurant with id=100003

`curl -s -i -X POST -d '{"dishes":[{"name":"dish1","price":20000},{"name":"dish1","price":30000}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurants/100003/menus --user admin@gmail.com:admin`

#### create today's Menu for Restaurant with id=100003

`curl -s -i -X POST -d '{"dishes":[{"name":"dish1","price":20000},{"name":"dish2","price":30000}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurants/100003/menus --user admin@gmail.com:admin`

#### update today's Menu for Restaurant with id=100002

`curl -s -X PUT -d '{"dishes":[{"name":"dish1 Updated","price":25000},{"name":"dish2 Updated","price":35000}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurants/100002/menus --user admin@gmail.com:admin`

#### delete today's menu unAuthorized

`curl -s -X DELETE http://localhost:8080/rest/restaurants/100002/menus`

#### delete today's Menu for Restaurant with id=100002

`curl -s -X DELETE http://localhost:8080/rest/restaurants/100002/menus --user admin@gmail.com:admin`

#### vote for today's menu of Restaurant with id=100003

`curl -s -i -X POST -d '100003' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/profile/votes --user admin@gmail.com:admin`

#### update vote for today's menu of Restaurant with id=100002

`curl -s -X PUT -d '100002' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/profile/votes --user admin@gmail.com:admin`

#### get vote for authorized user on date

`curl -s http://localhost:8080/rest/profile/votes/by?date=2021-07-17 --user user@yandex.ru:password`

#### get all vote for authorized user

`curl -s http://localhost:8080/rest/profile/votes --user user@yandex.ru:password`

#### delete today's vote for authorized user

`curl -s -X DELETE http://localhost:8080/rest/profile/votes --user user@yandex.ru:password`
