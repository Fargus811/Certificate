CREATE TABLE IF NOT EXISTS gift_certificate (
  id SERIAL,
  name VARCHAR(150) NOT NULL,
  description VARCHAR(255) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  duration INT NULL DEFAULT NULL,
  create_date TIMESTAMP NOT NULL,
  last_update_date TIMESTAMP NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS gift_certificate_in_orders (
  orders_id INT NOT NULL,
  gift_certificate_id INT NOT NULL);

CREATE TABLE IF NOT EXISTS gift_tags (
  gift_certificate_id INT NOT NULL,
  tag_id INT NOT NULL,
  PRIMARY KEY (gift_certificate_id, tag_id));

CREATE TABLE IF NOT EXISTS orders (
  id SERIAL,
  order_date TIMESTAMP NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS roles (
  id SERIAL,
  name VARCHAR(255) UNIQUE NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS tag (
  id SERIAL,
  name VARCHAR(25) NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS users (
  id SERIAL,
  username VARCHAR(150) UNIQUE NOT NULL,
  email VARCHAR(255) NULL DEFAULT NULL,
  first_name VARCHAR(255) NULL DEFAULT NULL,
  last_name VARCHAR(255) NULL DEFAULT NULL,
  password VARCHAR(255) NULL DEFAULT NULL,
  provider VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS users_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (user_id, role_id));
