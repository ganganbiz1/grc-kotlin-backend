package com.grc.platform.infrastructure.persistence.control.entity

import com.grc.platform.domain.control.model.ControlEvidenceMapping
import com.grc.platform.domain.control.model.ControlEvidenceMappingId
import com.grc.platform.domain.control.model.ControlId
import com.grc.platform.domain.control.model.TenantId
import com.grc.platform.domain.evidence.model.EvidenceId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "control_evidence_mappings")
class ControlEvidenceMappingJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "tenant_id", nullable = false, columnDefinition = "uuid")
    val tenantId: UUID,

    @Column(name = "control_id", nullable = false, columnDefinition = "uuid")
    val controlId: UUID,

    @Column(name = "evidence_id", nullable = false, columnDefinition = "uuid")
    val evidenceId: UUID,

    @Column(name = "note", columnDefinition = "TEXT")
    val note: String?,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant
) {
    fun toDomain(): ControlEvidenceMapping = ControlEvidenceMapping(
        id = ControlEvidenceMappingId(id),
        tenantId = TenantId(tenantId),
        controlId = ControlId(controlId),
        evidenceId = EvidenceId(evidenceId),
        note = note
    )

    companion object {
        fun fromDomain(domain: ControlEvidenceMapping, now: Instant): ControlEvidenceMappingJpaEntity = ControlEvidenceMappingJpaEntity(
            id = domain.id.value,
            tenantId = domain.tenantId.value,
            controlId = domain.controlId.value,
            evidenceId = domain.evidenceId.value,
            note = domain.note,
            createdAt = now
        )
    }
}
