package com.grc.platform.domain.control.model

import com.grc.platform.domain.evidence.model.EvidenceId
import com.grc.platform.domain.shared.UUIDv7
import java.util.UUID

@JvmInline
value class ControlEvidenceMappingId(val value: UUID) {
    companion object {
        fun generate(): ControlEvidenceMappingId = ControlEvidenceMappingId(UUIDv7.generate())
        fun fromString(value: String): ControlEvidenceMappingId = ControlEvidenceMappingId(UUID.fromString(value))
    }
}

/**
 * Control ↔ Evidence（多:多）
 */
class ControlEvidenceMapping(
    val id: ControlEvidenceMappingId,
    val tenantId: TenantId,
    val controlId: ControlId,
    val evidenceId: EvidenceId,
    val note: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ControlEvidenceMapping) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "ControlEvidenceMapping(id=$id, controlId=$controlId, evidenceId=$evidenceId)"
}
