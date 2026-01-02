---
name: kotlin-style
description: Kotlinコーディング規約。Kotlinコードを書く時、レビューする時、リファクタリングする時に使用。命名規則、Null安全、Spring Boot固有のパターン、ファイル構成のガイドライン。
---

# Kotlin コーディング規約

## 命名規則

### クラス・インターフェース
- **PascalCase** を使用
- 名詞または名詞句

```kotlin
// Good
class UserService
class RiskAssessmentController
interface PolicyRepository

// Bad
class userService
class Handle_Risk
```

### 関数・プロパティ
- **camelCase** を使用
- 関数は動詞で始める

```kotlin
// Good
fun findById(id: Long): User?
fun calculateRiskScore(): Int
val userName: String

// Bad
fun FindById(id: Long)
fun risk_score(): Int
```

### 定数
- **SCREAMING_SNAKE_CASE** を使用

```kotlin
companion object {
    const val MAX_RETRY_COUNT = 3
    const val DEFAULT_PAGE_SIZE = 20
}
```

## Null安全

### Nullable型は明示的に
```kotlin
// Good - 意図が明確
fun findById(id: Long): User?  // 見つからない可能性がある
fun getById(id: Long): User    // 必ず存在する（なければ例外）

// Bad - 曖昧
fun fetchUser(id: Long): User?  // find? get? どっち？
```

### Elvis演算子の活用
```kotlin
// Good
val name = user?.name ?: "Unknown"

// Bad
val name = if (user?.name != null) user.name else "Unknown"
```

### let / also / apply の使い分け
```kotlin
// let - 変換して返す
user?.let { it.toDto() }

// also - 副作用（ログなど）
user.also { logger.info("Created: $it") }

// apply - オブジェクト設定
User().apply {
    name = "John"
    email = "john@example.com"
}
```

## Spring Boot 固有

### コンストラクタインジェクション
```kotlin
// Good - コンストラクタインジェクション（推奨）
@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
)

// Bad - フィールドインジェクション
@Service
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository
}
```

### Data Class の活用
```kotlin
// Entity
@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var email: String
)

// DTO - data class を使用
data class UserResponse(
    val id: Long,
    val name: String,
    val email: String
)
```

## ファイル構成

### 1ファイル1クラスを基本とする
```
src/main/kotlin/com/grc/platform/
├── user/
│   ├── UserController.kt
│   ├── UserService.kt
│   ├── UserRepository.kt
│   ├── User.kt              # Entity
│   └── UserDto.kt           # DTOs（複数のDTOをまとめてもOK）
```

### 関連するDTOは1ファイルにまとめてもよい
```kotlin
// UserDto.kt
data class UserCreateRequest(...)
data class UserUpdateRequest(...)
data class UserResponse(...)
```
