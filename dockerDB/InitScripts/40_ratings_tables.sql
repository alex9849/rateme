USE osm01;

DROP TABLE IF EXISTS rateme_rating;

CREATE TABLE rateme_rating (
rating_id int auto_increment primary key 
, user_id int NOT NULL
, osm_id bigint NOT null
, rating_type varchar(10) not null -- IMAGE indicates Image, GRADE indicates grade (both types might have txt)
, grade int NULL 
, txt  varchar(2000) not null
, image mediumtext null
, create_dt DATETIME  not null DEFAULT CURRENT_TIMESTAMP   
, modify_dt DATETIME not null DEFAULT   CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
,FOREIGN KEY (user_id) REFERENCES rateme_user(user_id)
,FOREIGN KEY (osm_id) REFERENCES rateme_poi(osm_id)
) CHARACTER SET utf8mb4;
