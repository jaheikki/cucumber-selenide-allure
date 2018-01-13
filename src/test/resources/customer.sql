DROP DATABASE IF EXISTS  customerdb;
CREATE DATABASE IF NOT EXISTS  customerdb
CHARACTER SET = 'utf8'
COLLATE = 'utf8_general_ci';
USE customerdb;
DROP TABLE IF EXISTS customer;
CREATE TABLE IF NOT EXISTS customer (
       lastname varchar(50),
       firstname varchar(50),
       email varchar(50),
       street varchar(50),
       city varchar(50),
       PRIMARY KEY(lastname)
);

INSERT INTO customer (lastname, firstname, email,street, city) VALUES ('Selanne', 'Teemu', 'Teemu@com', 'Manhattan', 'NY');
INSERT INTO customer (lastname, firstname, email,street, city) VALUES ('Messi', 'Lionel', 'Lionel@com', 'Barcelona', 'Spain');
INSERT INTO customer (lastname, firstname, email,street, city) VALUES ('Jong-un', 'Kim', 'Kim@com', ' Pjongjang', 'North Korea');
