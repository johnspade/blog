CREATE TABLE users
(
  username CHARACTER VARYING(50) NOT NULL,
  password CHARACTER VARYING(60) NOT NULL,
  enabled  BOOLEAN               NOT NULL,
  CONSTRAINT users_pkey PRIMARY KEY (username)
);
CREATE TABLE authorities
(
  username  CHARACTER VARYING(50) NOT NULL,
  authority CHARACTER VARYING(50) NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY (username)
  REFERENCES users (username) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);