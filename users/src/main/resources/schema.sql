CREATE TABLE IF NOT EXISTS users (
                                      user_id BIGINT GENERATED BY DEFAULT AS IDENTITY,
                                      user_name VARCHAR(255),
                                      CONSTRAINT pk_user PRIMARY KEY (user_id)
);