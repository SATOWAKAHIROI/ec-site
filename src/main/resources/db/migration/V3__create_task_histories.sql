CREATE TABLE task_histories (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    task_id    BIGINT       NOT NULL,
    title      VARCHAR(255) NOT NULL,
    changed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;