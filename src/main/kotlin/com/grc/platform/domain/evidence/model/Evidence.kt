package com.grc.platform.domain.evidence.model

import com.grc.platform.domain.control.model.TenantId
import com.grc.platform.domain.shared.UUIDv7
import java.util.UUID

@JvmInline
value class EvidenceId(val value: UUID) {
    companion object {
        fun generate(): EvidenceId = EvidenceId(UUIDv7.generate())
        fun fromString(value: String): EvidenceId = EvidenceId(UUID.fromString(value))
    }
}

/**
 * 証跡の論理単位
 */
class Evidence(
    val id: EvidenceId,
    val tenantId: TenantId,
    val name: String,
    val description: String?,
    val artifacts: List<EvidenceArtifact> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Evidence) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "Evidence(id=$id, name=$name)"
}
