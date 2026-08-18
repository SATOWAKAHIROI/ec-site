# ec-site（学習用ECサイト REST API）

Spring Boot + MySQL で ECサイトのバックエンド（REST API）を学ぶためのプロジェクトです。
**ホストにJDKをインストールせず、すべてDockerコンテナ内でビルド・実行します。**

## 構成

| 項目 | 内容 |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.16 |
| ビルドツール | Gradle (Kotlin DSL) ※Gradle Wrapper 8.14.3 |
| DB | MySQL 8.4（Dockerコンテナ） |
| マイグレーション | Flyway |
| 主な依存 | Spring Web / Spring Data JPA / Spring Security / Validation / Lombok / DevTools |

### サービス構成（compose.yaml）

| サービス | 役割 | ポート |
|---|---|---|
| `db` | MySQL 8.4 | 3306（ホストに公開） |
| `app` | Spring Bootアプリ（`eclipse-temurin:21-jdk` 上で `./gradlew bootRun`） | 8080 |

## 起動方法

```bash
# 起動（初回はGradleの依存ダウンロードで数分かかります）
docker compose up -d

# ログを見る（"Started EcSiteApplication" が出れば起動完了）
docker compose logs -f app

# 停止
docker compose down

# DBのデータごと削除してやり直す
docker compose down -v
```

## API の動作確認

```bash
# 商品一覧
curl http://localhost:8080/api/products

# カテゴリで絞り込み
curl "http://localhost:8080/api/products?categoryId=1"

# 商品詳細
curl http://localhost:8080/api/products/1

# 存在しないID → 404 のJSONが返る
curl http://localhost:8080/api/products/9999

# 商品登録
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"categoryId":1,"name":"実践Spring Boot","description":"応用編","price":3800,"stock":10}'

# 入力値エラー → 400 とフィールドごとのメッセージが返る
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"","price":-1}'
```

## よく使うコマンド

```bash
# テスト実行
docker compose exec app ./gradlew test

# ビルド
docker compose exec app ./gradlew build

# MySQLに接続
docker compose exec db mysql -uecuser -pecpass ecsite
```

## ディレクトリ構成

```
src/main/java/com/example/ecsite/
├── EcSiteApplication.java   起動クラス
├── config/                  設定（SecurityConfig）
├── controller/              REST APIの入口（@RestController）
├── service/                 業務ロジック（トランザクション境界）
├── repository/              DBアクセス（Spring Data JPA）
├── domain/                  エンティティ（DBのテーブルに対応）
├── dto/                     APIの入出力用オブジェクト
└── exception/               例外と共通エラーハンドラ

src/main/resources/
├── application.yml          アプリ設定
└── db/migration/
    └── V1__init.sql         テーブル定義＋サンプルデータ
```

## データモデル

| テーブル | 内容 |
|---|---|
| `categories` | 商品カテゴリ |
| `products` | 商品（カテゴリに属する） |
| `users` | 会員 |
| `cart_items` | カート（会員×商品×数量） |
| `orders` | 注文 |
| `order_items` | 注文明細（注文時点の単価を保持） |

## 学習の進め方（おすすめ順）

1. `ProductController` → `ProductService` → `ProductRepository` の流れを追い、リクエストがDBに届くまでを理解する
2. カテゴリAPI（`/api/categories`）を自分で追加してみる（Controller/Service/DTOを写経）
3. 商品の更新（`PUT`）・削除（`DELETE`）を追加する
4. カートAPI（追加・一覧・削除）を実装する
5. 注文API（カートから注文を確定し、在庫を減らす）を実装する。ここでトランザクションの重要性を学べる
6. `SecurityConfig` を編集して会員登録・ログイン（JWT等）を導入し、`permitAll()` を `authenticated()` に変更する
7. `ProductControllerTest` などのテストを書く

## 注意事項

- `spring.jpa.hibernate.ddl-auto` は `validate` です。テーブル定義の変更は **Flywayのマイグレーションファイルを追加**して行ってください（例: `V2__add_xxx.sql`）。エンティティだけ変更すると起動時にエラーになります。
- 現在は学習しやすさを優先し、`/api/**` を認証なしで公開しています。そのまま公開環境で使用しないでください。
- サンプル会員のパスワードは `password`（BCryptハッシュ済み）です。
