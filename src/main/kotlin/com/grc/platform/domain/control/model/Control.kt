package com.grc.platform.domain.control.model

import com.grc.platform.domain.shared.UUIDv7
import java.util.UUID

@JvmInline
value class ControlId(val value: UUID) {
    companion object {
        fun generate(): ControlId = ControlId(UUIDv7.generate())
        fun fromString(value: String): ControlId = ControlId(UUID.fromString(value))
    }
}

@JvmInline
value class TenantId(val value: UUID) {
    companion object {
        fun generate(): TenantId = TenantId(UUIDv7.generate())
        fun fromString(value: String): TenantId = TenantId(UUID.fromString(value))
    }
}

@JvmInline
value class OwnerId(val value: UUID) {
    companion object {
        fun generate(): OwnerId = OwnerId(UUIDv7.generate())
        fun fromString(value: String): OwnerId = OwnerId(UUID.fromString(value))
    }
}

enum class ControlStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}

/**
 * テナントの運用管理対象
 */
class Control(
    val id: ControlId,
    val tenantId: TenantId,
    val name: String,
    val description: String?,
    val status: ControlStatus,
    val ownerId: OwnerId?,
    val note: String?,
    val customFields: Map<String, Any>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Control) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "Control(id=$id, name=$name, status=$status)"
}
