DELETE
FROM vote;
DELETE
FROM dish;
DELETE
FROM menu;
DELETE
FROM restaurant;
DELETE
FROM user_roles;
DELETE
FROM users;
ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO restaurant (name, address)
VALUES ('rest1', 'address_rest1'),
       ('rest2', 'address_rest2');

INSERT INTO menu (restaurant_id, day)
VALUES (100002, '2021-07-17'),
       (100002, '2021-07-18'),
       (100002, now());

INSERT INTO dish (menu_id, name, price)
VALUES (100004, 'menu1_dish1', 15600),
       (100004, 'menu1_dish2', 15700),
       (100004, 'menu1_dish3', 6500),
       (100004, 'menu1_dish4', 25700),
       (100004, 'menu1_dish5', 35050),
       (100005, 'menu2_dish1', 87000),
       (100005, 'menu2_dish2', 31500),
       (100005, 'menu2_dish3', 1500),
       (100005, 'menu2_dish4', 104000),
       (100005, 'menu2_dish5', 28700),
       (100006, 'menu3_dish1', 31544),
       (100006, 'menu3_dish2', 14400),
       (100006, 'menu3_dish3', 40000);

INSERT INTO vote (user_id, restaurant_id, voting_date)
VALUES (100000, 100002, '2021-07-17'),
       (100001, 100002, '2021-07-18'),
       (100000, 100002, now());