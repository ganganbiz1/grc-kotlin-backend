---
name: api-design
description: REST API設計ガイド。APIエンドポイントを設計・実装する時、Controllerを作成する時に使用。URL設計、HTTPメソッド、レスポンス形式、ステータスコードのガイドライン。
---

# REST API 設計ガイド

## URL設計

### 基本ルール
- **複数形の名詞** を使用（動詞は使わない）
- **小文字のケバブケース** を使用
- 階層は浅く保つ（3階層まで）

```
# Good
GET    /api/users
GET    /api/users/{id}
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}

GET    /api/risk-assessments
GET    /api/policies/{id}/controls

# Bad
GET    /api/getUsers
GET    /api/user/{id}
POST   /api/createUser
GET    /api/riskAssessments
```

### リソースの関連
```
# 1階層目: 親リソース
GET /api/policies

# 2階層目: 子リソース（親に従属）
GET /api/policies/{policyId}/controls

# 別々のリソースとして扱う場合
GET /api/controls?policyId={policyId}
```

## HTTPメソッド

| メソッド | 用途 | べき等性 | 安全性 |
|----------|------|----------|--------|
| GET | 取得 | Yes | Yes |
| POST | 作成 | No | No |
| PUT | 全体更新 | Yes | No |
| PATCH | 部分更新 | Yes | No |
| DELETE | 削除 | Yes | No |

## レスポンス形式

### 成功時
```kotlin
// 単一リソース
data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val createdAt: Instant
)

// コレクション（ページネーション付き）
data class PagedResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
```

### エラー時
```kotlin
data class ErrorResponse(
    val timestamp: Instant,
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)
```

## HTTPステータスコード

### 成功系
| コード | 用途 |
|--------|------|
| 200 OK | GET成功、PUT/PATCH成功 |
| 201 Created | POST成功（リソース作成） |
| 204 No Content | DELETE成功 |

### クライアントエラー
| コード | 用途 |
|--------|------|
| 400 Bad Request | リクエスト形式不正 |
| 401 Unauthorized | 認証エラー |
| 403 Forbidden | 認可エラー（権限なし） |
| 404 Not Found | リソースが存在しない |
| 409 Conflict | 競合（重複など） |
| 422 Unprocessable Entity | バリデーションエラー |

### サーバーエラー
| コード | 用途 |
|--------|------|
| 500 Internal Server Error | サーバー内部エラー |

## Controller実装例

```kotlin
@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    fun findAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): PagedResponse<UserResponse> {
        return userService.findAll(page, size)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): UserResponse {
        return userService.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: UserCreateRequest): UserResponse {
        return userService.create(request)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UserUpdateRequest
    ): UserResponse {
        return userService.update(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        userService.delete(id)
    }
}
```

## クエリパラメータ

### フィルタリング
```
GET /api/users?status=active&role=admin
```

### ソート
```
GET /api/users?sort=createdAt,desc
GET /api/users?sort=name,asc
```

### ページネーション
```
GET /api/users?page=0&size=20
```

### 検索
```
GET /api/users?q=keyword
GET /api/users?search=keyword
```
