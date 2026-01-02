package com.grc.platform.domain.control.model

import com.grc.platform.domain.policy.model.PolicyId
import com.grc.platform.domain.shared.UUIDv7
import java.util.UUID

@JvmInline
value class ControlPolicyMappingId(val value: UUID) {
    companion object {
        fun generate(): ControlPolicyMappingId = ControlPolicyMappingId(UUIDv7.generate())
        fun fromString(value: String): ControlPolicyMappingId = ControlPolicyMappingId(UUID.fromString(value))
    }
}

/**
 * Control ↔ Policy（多:多）
 */
class ControlPolicyMapping(
    val id: ControlPolicyMappingId,
    val tenantId: TenantId,
    val controlId: ControlId,
    val policyId: PolicyId
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ControlPolicyMapping) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "ControlPolicyMapping(id=$id, controlId=$controlId, policyId=$policyId)"
}
