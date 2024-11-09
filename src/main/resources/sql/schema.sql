CREATE TABLE userinfo (
                          id SERIAL PRIMARY KEY,
                          user_id varchar(255) NOT NULL,-- email
                          user_nm varchar(20) NOT NULL,
                          user_pw varchar(100) NOT NULL,
                          use_flag char(1) NOT NULL,
                          created_at timestamp NOT NULL DEFAULT now(),
                          created_by varchar(20) NULL,
                          updated_at timestamp NULL,
                          updated_by varchar(20) NULL,
                          CONSTRAINT userinfo_uk unique (user_id)
);

CREATE TABLE subject (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255)
);
