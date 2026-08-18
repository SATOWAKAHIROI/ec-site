# ec-site（学習用ECサイト REST API）

Spring Boot + MySQL で ECサイトのバックエンド（REST API）を学ぶためのプロジェクトです。
**ホストにJDKをインストールせず、すべてDockerコンテナ内でビルド・実行します。**

> **APIの実装は入っていません。** コントローラー・サービスは空の雛形になっており、
> 中身はご自身で実装しながら学ぶ構成です。

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

コードを編集すると DevTools が自動でアプリを再起動します（再ビルド不要）。

## よく使うコマンド

```bash
# テスト実行
docker compose exec app ./gradlew test

# ビルド
docker compose exec app ./gradlew build

# MySQLに接続（サンプルデータの確認に）
docker compose exec db mysql -uecuser -pecpass ecsite
```

## ディレクトリ構成

```
src/main/java/com/example/ecsite/
├── EcSiteApplication.java   起動クラス
├── config/
│   └── SecurityConfig.java  /api/** を認証なしで公開する設定
├── controller/
│   └── ProductController.java  空の雛形（@RestController + DIのみ）
├── service/
│   └── ProductService.java     空の雛形（@Service + DIのみ）
├── repository/              6つのリポジトリ（JpaRepositoryを継承しただけの空インタフェース）
└── domain/                  エンティティ（DBのテーブルに対応）

src/main/resources/
├── application.yml          アプリ設定
└── db/migration/
    └── V1__init.sql         テーブル定義＋サンプルデータ
```

DTO・例外ハンドラのパッケージは用意していません。必要になった時点で
`dto/`・`exception/` などをご自身で作成してください。

## データモデル

| テーブル | 内容 |
|---|---|
| `categories` | 商品カテゴリ（サンプル: 本 / 家電 / 食品） |
| `products` | 商品（サンプル6件。カテゴリに属する） |
| `users` | 会員（サンプル1件） |
| `cart_items` | カート（会員×商品×数量） |
| `orders` | 注文 |
| `order_items` | 注文明細（注文時点の単価を保持） |

## 学習の進め方（おすすめ順）

1. **商品一覧API** `GET /api/products` を実装する
   - `ProductRepository` に `findAll()` は既にあるので、`ProductService` にメソッドを追加し、`ProductController` から呼ぶ
   - まずはエンティティをそのまま返し、次にレスポンス用のDTOを作って詰め替える
2. **商品詳細API** `GET /api/products/{id}` を実装する（`@PathVariable`、`Optional` の扱い）
3. **商品登録API** `POST /api/products` を実装する（`@RequestBody`、`@Valid` による入力検証）
4. **例外処理** `@RestControllerAdvice` を作り、404や400をJSONで返す
5. **検索** `findByCategoryId` のような**クエリメソッド**をリポジトリに追加する
6. **カートAPI**（追加・一覧・削除）を実装する
7. **注文API**（カートから注文を確定し在庫を減らす）を実装する。トランザクションの重要性を学べる
8. **認証** `SecurityConfig` を編集し、会員登録・ログインを導入する
9. **テスト** `@SpringBootTest` / `@WebMvcTest` でテストを書く

### 動作確認の例

```bash
# 実装後、こういったコマンドで確認できます
curl http://localhost:8080/api/products
curl http://localhost:8080/api/products/1
```

## 注意事項

- `spring.jpa.hibernate.ddl-auto` は `validate` です。テーブル定義の変更は **Flywayのマイグレーションファイルを追加**して行ってください（例: `V2__add_xxx.sql`）。エンティティだけ変更すると起動時にエラーになります。
- 現在は学習しやすさを優先し、`/api/**` を認証なしで公開しています。そのまま公開環境で使用しないでください。
- サンプル会員のパスワードは `password`（BCryptハッシュ済み）です。
