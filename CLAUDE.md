# CLAUDE.md

このファイルはClaude Codeがプロジェクトで作業する際のルールとコンテキストを提供します。

## 開発コマンド

Docker操作は必ずMakefileのコマンドを使用すること:

```bash
make up        # サービス起動
make up-build  # リビルドして起動
make down      # サービス停止
make restart   # 再起動
make logs      # ログ確認
make ps        # コンテナ状態確認
make db        # DBのみ起動
make app       # ローカルでアプリ起動（要DB）
make build     # ビルド
make test      # テスト実行
make clean     # クリーン
```

## API開発

- API-Firstアプローチを採用（OpenAPI Generator）
- API仕様は `openapi/` ディレクトリにYAMLで定義
- `./gradlew openApiGenerate` でController interface/DTOを自動生成
- バリデーションはOpenAPI仕様から自動生成される

## アーキテクチャ

- レイヤードアーキテクチャ + DDD
- パッケージ構成: domain → application → infrastructure → presentation
- CQRS: QueryService（参照系）とCommandService（更新系）を分離
