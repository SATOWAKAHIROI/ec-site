# ============================================================
# 本番相当の実行用イメージ（学習中は compose.yaml の app サービスを使う）
#   ビルド : docker build -t ec-site .
#   実行   : docker run --rm -p 8080:8080 -e DB_HOST=host.docker.internal ec-site
# ============================================================

# ---- ビルドステージ ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# 依存関係のダウンロードをキャッシュさせるため、先にビルド定義だけコピーする
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# ---- 実行ステージ ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
