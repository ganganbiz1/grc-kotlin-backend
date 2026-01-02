package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7
import java.util.UUID

@JvmInline
value class FrameworkControlPredecessorId(val value: UUID) {
    companion object {
        fun generate(): FrameworkControlPredecessorId = FrameworkControlPredecessorId(UUIDv7.generate())
        fun fromString(value: String): FrameworkControlPredecessorId = FrameworkControlPredecessorId(UUID.fromString(value))
    }
}

enum class PredecessorStatus {
    SUGGESTED,
    CONFIRMED,
    REJECTED
}

/**
 * FrameworkControlの前版との対応（改訂引き継ぎ用）
 */
class FrameworkControlPredecessor(
    val id: FrameworkControlPredecessorId,
    val frameworkControlId: FrameworkControlId,
    val predecessorId: FrameworkControlId,
    val status: PredecessorStatus
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FrameworkControlPredecessor) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "FrameworkControlPredecessor(id=$id, frameworkControlId=$frameworkControlId, predecessorId=$predecessorId)"
}
