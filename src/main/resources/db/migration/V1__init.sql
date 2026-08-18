-- ============================================================
-- 学習用ECサイト 初期スキーマ
-- ============================================================

-- カテゴリ
CREATE TABLE categories (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(100) NOT NULL,
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_categories_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 会員
CREATE TABLE users (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    name          VARCHAR(100) NOT NULL,
    created_at    DATETIME(6)  NOT NULL,
    updated_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_email (email)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 商品
CREATE TABLE products (
    id          BIGINT         NOT NULL AUTO_INCREMENT,
    category_id BIGINT         NOT NULL,
    name        VARCHAR(200)   NOT NULL,
    description VARCHAR(1000)  NULL,
    price       DECIMAL(12, 2) NOT NULL,
    stock       INT            NOT NULL,
    created_at  DATETIME(6)    NOT NULL,
    updated_at  DATETIME(6)    NOT NULL,
    PRIMARY KEY (id),
    KEY idx_products_category_id (category_id),
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- カート
CREATE TABLE cart_items (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    user_id    BIGINT      NOT NULL,
    product_id BIGINT      NOT NULL,
    quantity   INT         NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_cart_items_user_product (user_id, product_id),
    CONSTRAINT fk_cart_items_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_cart_items_product FOREIGN KEY (product_id) REFERENCES products (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 注文
CREATE TABLE orders (
    id          BIGINT         NOT NULL AUTO_INCREMENT,
    user_id     BIGINT         NOT NULL,
    total_price DECIMAL(12, 2) NOT NULL,
    status      VARCHAR(20)    NOT NULL,
    ordered_at  DATETIME(6)    NOT NULL,
    created_at  DATETIME(6)    NOT NULL,
    updated_at  DATETIME(6)    NOT NULL,
    PRIMARY KEY (id),
    KEY idx_orders_user_id (user_id),
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 注文明細
CREATE TABLE order_items (
    id         BIGINT         NOT NULL AUTO_INCREMENT,
    order_id   BIGINT         NOT NULL,
    product_id BIGINT         NOT NULL,
    quantity   INT            NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    created_at DATETIME(6)    NOT NULL,
    PRIMARY KEY (id),
    KEY idx_order_items_order_id (order_id),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ============================================================
-- サンプルデータ
-- ============================================================
INSERT INTO categories (name, created_at, updated_at) VALUES
    ('本', NOW(6), NOW(6)),
    ('家電', NOW(6), NOW(6)),
    ('食品', NOW(6), NOW(6));

INSERT INTO products (category_id, name, description, price, stock, created_at, updated_at) VALUES
    (1, 'Spring Boot入門', 'Spring Bootの基礎を学べる入門書', 3200.00, 15, NOW(6), NOW(6)),
    (1, 'はじめてのSQL', 'データベース操作の基本を学ぶ一冊', 2800.00, 8, NOW(6), NOW(6)),
    (2, 'ワイヤレスマウス', '静音設計のBluetoothマウス', 2980.00, 30, NOW(6), NOW(6)),
    (2, 'メカニカルキーボード', '打鍵感にこだわった日本語配列キーボード', 12800.00, 5, NOW(6), NOW(6)),
    (3, 'ドリップコーヒー30袋', '深煎りブレンドのドリップバッグ', 1980.00, 50, NOW(6), NOW(6)),
    (3, '有機栽培りんごジュース', '果汁100%のストレートジュース', 880.00, 0, NOW(6), NOW(6));

-- パスワードは 'password' をBCryptでハッシュ化したもの（学習用）
INSERT INTO users (email, password_hash, name, created_at, updated_at) VALUES
    ('taro@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '山田太郎', NOW(6), NOW(6));
