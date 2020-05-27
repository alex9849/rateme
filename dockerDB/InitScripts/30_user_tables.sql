USE osm01;

DROP TABLE IF EXISTS rateme_user;

CREATE TABLE rateme_user (
user_id int auto_increment primary key
, username varchar(20) not null UNIQUE 
, E_Mail varchar(50) not null
, firstname varchar(20) not null
, lastname varchar(20) not null
, street   varchar(30) not null
, streetNr  varchar(20) not null
, zip  varchar(20) not null
, city  varchar(30) not null
, password VARBINARY (1000) not null
,create_dt DATETIME  not null DEFAULT CURRENT_TIMESTAMP   
,modify_dt DATETIME not null DEFAULT   CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)CHARACTER SET utf8mb4;


