package com.grc.platform.domain.policy.model

import com.grc.platform.domain.control.model.TenantId
import com.grc.platform.domain.shared.UUIDv7
import java.util.UUID

@JvmInline
value class PolicyId(val value: UUID) {
    companion object {
        fun generate(): PolicyId = PolicyId(UUIDv7.generate())
        fun fromString(value: String): PolicyId = PolicyId(UUID.fromString(value))
    }
}

/**
 * 社内規程
 */
class Policy(
    val id: PolicyId,
    val tenantId: TenantId,
    val name: String,
    val description: String?,
    val revisions: List<PolicyRevision> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Policy) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "Policy(id=$id, name=$name)"
}
