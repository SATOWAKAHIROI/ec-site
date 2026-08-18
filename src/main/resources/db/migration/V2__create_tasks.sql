CREATE TABLE tasks (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    title      VARCHAR(255) NOT NULL,
    completed  BOOLEAN      NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO tasks (title, completed) VALUES
    ('SpringBootの学習', FALSE),
    ('Reactの学習', TRUE);