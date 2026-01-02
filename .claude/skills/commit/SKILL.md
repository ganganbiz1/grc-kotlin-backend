---
name: commit
description: コミットメッセージ規約。gitコミットを作成する時に使用。Conventional Commits形式（feat/fix/docs/refactor等）のフォーマットガイド。
---

# コミットメッセージ規約

## フォーマット

```
<type>: <subject>

<body>
```

## Type（必須）

| Type | 用途 |
|------|------|
| `feat` | 新機能 |
| `fix` | バグ修正 |
| `docs` | ドキュメントのみ |
| `style` | フォーマット変更（コードの動作に影響なし） |
| `refactor` | リファクタリング（機能追加・バグ修正なし） |
| `test` | テスト追加・修正 |
| `chore` | ビルド・ツール・設定の変更 |

## Subject（必須）

- 50文字以内
- 日本語OK
- 文末にピリオドを付けない
- 命令形で書く（英語の場合）

## Body（任意）

- 72文字で改行
- 「何を」「なぜ」を説明
- 「どうやって」は基本的にコードを見ればわかるので不要

## 例

```
feat: ユーザー登録APIを追加

- POST /api/users エンドポイントを追加
- メールアドレスの重複チェックを実装
- パスワードのハッシュ化にBCryptを使用
```

```
fix: リスク評価スコアの計算誤りを修正

重み付け係数が適用されていなかった問題を修正。
影響範囲: RiskAssessmentService.calculateScore()
```

```
refactor: UserServiceのDI方法を変更

フィールドインジェクションからコンストラクタインジェクションに変更。
テスタビリティ向上のため。
```

```
chore: Gradle依存関係を更新

- Spring Boot 3.4.0 -> 3.4.1
- Kotlin 2.0.21 -> 2.1.0
```
