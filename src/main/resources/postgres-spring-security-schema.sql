CREATE TABLE users
(
  username character varying(50) NOT NULL,
  password character varying(60) NOT NULL,
  enabled boolean NOT NULL,
  CONSTRAINT users_pkey PRIMARY KEY (username));
CREATE TABLE authorities
(
  username character varying(50) NOT NULL,
  authority character varying(50) NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY (username)
      REFERENCES users (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);