# --- !Ups

CREATE TABLE TB_USER (
  ID INTEGER NOT NULL AUTO_INCREMENT, 
  EMAIL VARCHAR(50), 
  PASSWORD VARCHAR(12),
  ADMIN BOOLEAN NOT NULL DEFAULT 0,
  PRIMARY KEY (ID)
); 


CREATE TABLE TB_MOVIE (
  ID INTEGER NOT NULL AUTO_INCREMENT, 
  TITLE VARCHAR(50), 
  DIRECTOR VARCHAR(50),
  PRODUCTION_YEAR INTEGER(4),
  PRIMARY KEY (ID)
); 

CREATE TABLE TB_RATING (
  ID INTEGER NOT NULL AUTO_INCREMENT,
  STARS FLOAT(1,1),
  USERID INTEGER NOT NULL,
  MOVIEID INTEGER NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (USERID) REFERENCES TB_USER(ID),
  FOREIGN KEY (MOVIEID) REFERENCES TB_MOVIE(ID)
);

CREATE TABLE TB_COMMENT (
  ID INTEGER NOT NULL AUTO_INCREMENT,
  MESSAGE VARCHAR(200),
  USERID INTEGER NOT NULL,
  MOVIEID INTEGER NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (USERID) REFERENCES TB_USER(ID),
  FOREIGN KEY (MOVIEID) REFERENCES TB_MOVIE(ID)
);

# --- !Downs

DROP TABLE TB_USER;
DROP TABLE TB_MOVIE;
DROP TABLE TB_RATING;
DROP TABLE TB_COMMENT;
