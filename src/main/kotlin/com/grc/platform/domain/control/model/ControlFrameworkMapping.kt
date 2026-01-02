package com.grc.platform.domain.control.model

import com.grc.platform.domain.framework.model.FrameworkControlId
import com.grc.platform.domain.shared.UUIDv7
import java.util.UUID

@JvmInline
value class ControlFrameworkMappingId(val value: UUID) {
    companion object {
        fun generate(): ControlFrameworkMappingId = ControlFrameworkMappingId(UUIDv7.generate())
        fun fromString(value: String): ControlFrameworkMappingId = ControlFrameworkMappingId(UUID.fromString(value))
    }
}

/**
 * Control ↔ FrameworkControl（多:多）
 */
class ControlFrameworkMapping(
    val id: ControlFrameworkMappingId,
    val tenantId: TenantId,
    val controlId: ControlId,
    val frameworkControlId: FrameworkControlId
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ControlFrameworkMapping) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "ControlFrameworkMapping(id=$id, controlId=$controlId, frameworkControlId=$frameworkControlId)"
}
