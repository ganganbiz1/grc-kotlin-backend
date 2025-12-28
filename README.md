# GRC Kotlin Backend

GRC（Governance, Risk, Compliance）SaaSのバックエンドアプリケーション。

## 技術スタック

| 項目 | 技術 | 説明 |
|------|------|------|
| 言語 | Kotlin 2.1 | JVM上で動作するモダンな言語。Javaと100%互換 |
| フレームワーク | Spring Boot 3.4 | Javaエコシステムで最も人気のあるWebフレームワーク |
| ビルドツール | Gradle 8.12 | 依存関係管理とビルド自動化ツール |
| データベース | PostgreSQL 17 | オープンソースのリレーショナルDB |
| JDK | Temurin 21 | Java実行環境（LTS版） |

---

## プロジェクト構造

```
grc-kotlin-backend/
├── build.gradle.kts      # ビルド設定（依存関係、プラグイン）
├── settings.gradle.kts   # プロジェクト名の設定
├── gradlew / gradlew.bat # Gradle Wrapper（後述）
├── gradle/
│   └── wrapper/          # Gradle Wrapperの実体
├── Dockerfile            # 本番用コンテナイメージ定義
├── docker-compose.yml    # ローカル開発環境
├── Makefile              # よく使うコマンドのショートカット
└── src/
    ├── main/
    │   ├── kotlin/       # Kotlinソースコード
    │   │   └── com/grc/platform/
    │   │       └── GrcApplication.kt  # エントリーポイント
    │   └── resources/
    │       ├── application.yml        # 共通設定
    │       └── application-local.yml  # ローカル環境設定
    └── test/
        ├── kotlin/       # テストコード
        └── resources/
            └── application-test.yml   # テスト用設定
```

---

## Gradleとは？

**Gradle**はビルドツールです。以下の役割を担います：

1. **依存関係の管理** - ライブラリのダウンロードとバージョン管理
2. **コンパイル** - KotlinコードをJVMバイトコードに変換
3. **テスト実行** - 自動テストの実行
4. **パッケージング** - 実行可能なJARファイルの作成

### Gradle Wrapper とは？

```bash
./gradlew build   # これを使う（推奨）
gradle build      # これは使わない
```

`./gradlew`（Gradle Wrapper）を使う理由：
- **バージョン固定**: プロジェクトで指定したGradleバージョン（8.12）を自動でダウンロード・使用
- **チーム開発**: 全員が同じバージョンを使用できる
- **CI/CD**: サーバーにGradleがなくても動作

### build.gradle.kts の読み方

```kotlin
plugins {
    // 使用するプラグイン（機能拡張）
    kotlin("jvm") version "2.1.0"              // Kotlinコンパイラ
    kotlin("plugin.spring") version "2.1.0"   // Spring用のKotlin拡張
    kotlin("plugin.jpa") version "2.1.0"      // JPA用のKotlin拡張
    id("org.springframework.boot") version "3.4.1"  // Spring Boot
    id("io.spring.dependency-management") version "1.1.7"  // 依存関係バージョン管理
}

dependencies {
    // アプリケーションが使用するライブラリ
    implementation("...")   // 本番コードで使用
    runtimeOnly("...")      // 実行時のみ必要
    testImplementation("...") // テストコードで使用
    developmentOnly("...")  // 開発時のみ（本番JARに含まれない）
}
```

### よく使うGradleコマンド

| コマンド | 説明 |
|----------|------|
| `./gradlew build` | コンパイル + テスト + JAR作成 |
| `./gradlew test` | テストのみ実行 |
| `./gradlew bootRun` | アプリケーション起動 |
| `./gradlew clean` | ビルド成果物を削除 |
| `./gradlew dependencies` | 依存関係ツリーを表示 |

---

## Spring Boot とは？

**Spring Boot**は、Spring Frameworkを簡単に使えるようにしたフレームワークです。

### 主な特徴

1. **自動設定（Auto Configuration）**
   - 依存関係を追加するだけで、適切な設定が自動適用される
   - 例: `spring-boot-starter-data-jpa`を追加 → JPA/Hibernateが自動設定

2. **スターター依存関係**
   - 関連ライブラリをまとめたパッケージ
   - `spring-boot-starter-web` = Web開発に必要なライブラリ一式

3. **組み込みサーバー**
   - Tomcatが内蔵されているので、JARを実行するだけでWebサーバーが起動

### アプリケーションの起動の流れ

```
GrcApplication.kt
    ↓
@SpringBootApplication  ← この1つのアノテーションが以下を含む
    ├── @Configuration      （設定クラスとして認識）
    ├── @EnableAutoConfiguration （自動設定を有効化）
    └── @ComponentScan      （コンポーネントを自動検出）
    ↓
runApplication<GrcApplication>(*args)
    ↓
Spring コンテナ起動 → 組み込みTomcat起動 → リクエスト待機
```

### application.yml の仕組み

Springは`application.yml`から設定を読み込みます。

```yaml
spring:
  profiles:
    active: local  # 有効なプロファイル
```

**プロファイル**とは、環境ごとの設定切り替え機能：
- `application.yml` - 共通設定
- `application-local.yml` - ローカル環境（`local`プロファイル時に読み込み）
- `application-prod.yml` - 本番環境（`prod`プロファイル時に読み込み）

起動時の指定方法：
```bash
# 環境変数で指定
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun

# 引数で指定
./gradlew bootRun --args='--spring.profiles.active=local'
```

---

## JVM / JDK / JRE の違い

```
┌─────────────────────────────────────────┐
│  JDK (Java Development Kit)             │
│  開発者向け。コンパイラ(javac)を含む      │
│  ┌─────────────────────────────────────┐│
│  │  JRE (Java Runtime Environment)    ││
│  │  実行環境。アプリを動かすのに必要     ││
│  │  ┌─────────────────────────────┐   ││
│  │  │  JVM (Java Virtual Machine) │   ││
│  │  │  バイトコードを実行する仮想マシン│   ││
│  │  └─────────────────────────────┘   ││
│  └─────────────────────────────────────┘│
└─────────────────────────────────────────┘
```

- **開発時**: JDK 21が必要（コンパイルするため）
- **本番実行時**: JRE 21があればOK（Dockerfileで`eclipse-temurin:21-jre`を使用）

### Kotlinの位置づけ

```
Kotlinコード (.kt)
    ↓ Kotlinコンパイラ
JVMバイトコード (.class)
    ↓ JVM
実行
```

KotlinはJVM上で動作するため：
- Javaのライブラリをそのまま使える
- Javaと同じパフォーマンス
- 既存のJavaエコシステム（Spring等）と完全互換

---

## ローカル開発環境

### 前提条件

- Docker Desktop がインストールされていること
- JDK 21 がインストールされていること

### 起動方法

```bash
# 方法1: 全てDockerで起動
make up

# 方法2: DBはDocker、アプリはGradleで起動（ホットリロード可能）
make db              # PostgreSQLを起動
make app             # アプリを起動
```

### 動作確認

```bash
# ヘルスチェック
curl http://localhost:8080/actuator/health

# 期待するレスポンス
{"status":"UP"}
```

### 停止

```bash
make down
```

---

## Makefileコマンド一覧

| コマンド | 説明 |
|----------|------|
| `make up` | 全サービス起動（app + db） |
| `make up-build` | イメージを再ビルドして起動 |
| `make down` | 全サービス停止 |
| `make restart` | 再起動 |
| `make logs` | ログ表示（リアルタイム） |
| `make ps` | コンテナ状態確認 |
| `make db` | DBのみ起動 |
| `make app` | Gradleでアプリ起動 |
| `make build` | ビルド |
| `make test` | テスト実行 |
| `make clean` | ビルド成果物削除 |

---

## ディレクトリ規約

Spring Bootプロジェクトの標準的な構成：

```
src/main/kotlin/com/grc/platform/
├── GrcApplication.kt          # エントリーポイント
├── controller/                # REST APIエンドポイント（これから作成）
├── service/                   # ビジネスロジック
├── repository/                # データベースアクセス
├── entity/                    # データベーステーブルに対応するクラス
├── dto/                       # APIリクエスト/レスポンス用クラス
└── config/                    # 設定クラス
```

---

## 次のステップ

1. **REST APIの作成** - Controllerクラスを作成してエンドポイントを定義
2. **エンティティの作成** - データベーステーブルを定義
3. **リポジトリの作成** - Spring Data JPAでCRUD操作を実装
4. **認証/認可** - Spring Securityの導入

---

## 参考リンク

- [Kotlin公式ドキュメント](https://kotlinlang.org/docs/home.html)
- [Spring Boot公式ドキュメント](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Initializr](https://start.spring.io/) - Spring Bootプロジェクト生成ツール
- [Gradle公式ドキュメント](https://docs.gradle.org/current/userguide/userguide.html)
