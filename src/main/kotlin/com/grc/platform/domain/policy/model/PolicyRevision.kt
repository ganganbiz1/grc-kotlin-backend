package com.grc.platform.domain.policy.model

import com.grc.platform.domain.shared.UUIDv7
import java.time.LocalDate
import java.util.UUID

@JvmInline
value class PolicyRevisionId(val value: UUID) {
    companion object {
        fun generate(): PolicyRevisionId = PolicyRevisionId(UUIDv7.generate())
        fun fromString(value: String): PolicyRevisionId = PolicyRevisionId(UUID.fromString(value))
    }
}

enum class PolicyRevisionStatus {
    DRAFT,
    ACTIVE,
    ARCHIVED
}

/**
 * 改訂・版
 */
class PolicyRevision(
    val id: PolicyRevisionId,
    val policyId: PolicyId,
    val version: String,
    val status: PolicyRevisionStatus,
    val effectiveDate: LocalDate?,
    val content: String?,
    val sections: List<PolicySection> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PolicyRevision) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "PolicyRevision(id=$id, version=$version)"
}
