INSERT INTO gift_certificate (id, name, description, price, duration, create_date, last_update_date)  VALUES
('1','quia','Voluptatem quae error et illo nisi qui. Dolore soluta voluptatibus nobis qui.','7.00','0','2011-10-26 09:54:55','2009-12-20 19:16:20'),
('2','alias','Facere autem et accusantium similique illo cupiditate laudantium. Iste delectus saepe possimus aliquam vel suscipit error. Voluptas nisi rem debitis corporis delectus et sed ducimus.','57.42','0','2018-06-13 03:45:24','1992-12-09 07:53:30'),
('3','quial','Corporis voluptates vitae porro voluptas quibusdam non. Cum non quia voluptas numquam sit ea dolorem delectus. Molestiae voluptatem rerum asperiores vero consequatur labore.','76.67','0','2009-05-22 07:55:24','1999-05-21 00:46:35'),
('4','perferendis','Aliquid dolore minus distinctio. Voluptas est labore ad velit velit in fugiat. Autem dolores corporis ducimus itaque dolor asperiores minus. Veritatis aut dolores dolorem maxime architecto et.','88604.50','0','1995-01-31 12:16:17','2015-08-21 08:29:07'),
('5','possimus','Sequi voluptatem laboriosam assumenda dignissimos non recusandae. Eveniet aliquid ut et occaecati laborum odit quam ea. Fugit laborum cupiditate est labore hic et occaecati.','1.10','0','1980-10-10 04:47:34','1994-04-28 18:25:41'),
('6','sed','Praesentium quisquam unde est in at. Voluptas voluptas numquam commodi non voluptatibus. Inventore vel blanditiis nobis tempora. Laboriosam rerum aliquam animi quis numquam.','22.37','0','1980-10-14 20:05:22','1986-04-02 14:39:42'),
('7','vel','Nisi dolores quaerat nostrum minima exercitationem non ut. Facere est sunt provident sequi et voluptatem. Velit adipisci harum neque.','0.00','0','1973-05-20 08:08:58','1979-01-01 04:50:51'),
('8','ipsum','Ipsam cum culpa nihil dignissimos sunt molestiae fuga. Porro iure et provident et eius eum quam asperiores.','2.00','0','1997-09-09 00:46:37','1987-08-12 04:28:11'),
('9','voluptatem','Et omnis consequatur est vero quaerat. Eos tenetur voluptas a fugiat quos incidunt doloribus. Harum maxime ut eos quia enim.','5.23','0','1998-07-19 16:11:28','1978-06-19 23:47:18'),
('10','corporis','Officia laboriosam necessitatibus corporis quam accusantium aut. Cum at natus est. Rerum cupiditate ab optio est aut facere.','9875000.11','0','1990-05-14 13:25:52','2018-01-12 05:08:57');

INSERT INTO tag (name) VALUES ('ut'),
                                   ('similique'),
                                   ('et'),
                                   ('praesentium'),
                                   ('nihil'),
                                   ('aut'),
                                   ('ullam'),
                                   ('soluta'),
                                   ('accusamus'),
                                   ('perspiciatis'),
                                   ('et'),
                                   ('voluptas'),
                                   ('fugiat'),
                                   ('maxime'),
                                   ('unde'),
                                   ('eos'),
                                   ('sit'),
                                   ('sit'),
                                   ('rem'),
                                   ('doloribus'),
                                   ('eligendi'),
                                   ('odio'),
                                   ('et'),
                                   ('quibusdam'),
                                   ('non'),
                                   ('reiciendis'),
                                   ('laborum'),
                                   ('eaque'),
                                   ('nobis'),
                                   ('est'),
                                   ('sed'),
                                   ('a'),
                                   ('illo'),
                                   ('quasi'),
                                   ('nulla'),
                                   ('voluptatem'),
                                   ('commodi'),
                                   ('voluptatibus'),
                                   ('ex'),
                                   ('est'),
                                   ('aliquam'),
                                   ('esse'),
                                   ('mollitia'),
                                   ('non'),
                                   ('qui'),
                                   ('enim'),
                                   ('voluptatibus'),
                                   ('amet'),
                                   ('praesentium'),
                                   ('voluptatem');

INSERT INTO gift_tags (gift_certificate_id, tag_id) VALUES ('1', '1');
INSERT INTO gift_tags (gift_certificate_id, tag_id) VALUES ('1', '2');
INSERT INTO gift_tags (gift_certificate_id, tag_id) VALUES ('1', '3');
INSERT INTO gift_tags (gift_certificate_id, tag_id) VALUES ('2', '1');
INSERT INTO gift_tags (gift_certificate_id, tag_id) VALUES ('2', '2');
INSERT INTO gift_tags (gift_certificate_id, tag_id) VALUES ('3', '2');
INSERT INTO gift_tags (gift_certificate_id, tag_id) VALUES ('4', '3');
INSERT INTO gift_tags (gift_certificate_id, tag_id) VALUES ('5', '5');
INSERT INTO gift_tags (gift_certificate_id, tag_id) VALUES ('6', '6');
INSERT INTO gift_tags (gift_certificate_id, tag_id) VALUES ('7', '7');
INSERT INTO gift_tags (gift_certificate_id, tag_id) VALUES ('8', '2');
INSERT INTO gift_tags (gift_certificate_id, tag_id) VALUES ('9', '1');

INSERT INTO users (id, username, email, first_name, last_name, password, provider) VALUES
( '1', 'fargus', 'fargus@example.com', 'Daniil', 'Sergeev', 'test', 'HOME_SERVER'),
( '2', 'fargusTest', 'fargusTest@example.com', 'Andrew', 'Dorohovich', 'test', 'HOME_SERVER');

INSERT INTO orders (id, order_date, price, user_id) VALUES
( '1', '2011-10-26 09:54:55', '900.00', '2'),
( '2', '2011-10-27 09:54:55', '500.00', '1');

INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');