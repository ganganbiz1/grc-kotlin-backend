package com.grc.platform.infrastructure.persistence.evidence.entity

import com.grc.platform.domain.control.model.TenantId
import com.grc.platform.domain.evidence.model.Evidence
import com.grc.platform.domain.evidence.model.EvidenceArtifact
import com.grc.platform.domain.evidence.model.EvidenceId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "evidences")
class EvidenceJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "tenant_id", nullable = false, columnDefinition = "uuid")
    val tenantId: UUID,

    @Column(name = "name", nullable = false, length = 200)
    val name: String,

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String?,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    fun toDomain(artifacts: List<EvidenceArtifact> = emptyList()): Evidence = Evidence(
        id = EvidenceId(id),
        tenantId = TenantId(tenantId),
        name = name,
        description = description,
        artifacts = artifacts
    )

    companion object {
        fun fromDomain(domain: Evidence, now: Instant): EvidenceJpaEntity = EvidenceJpaEntity(
            id = domain.id.value,
            tenantId = domain.tenantId.value,
            name = domain.name,
            description = domain.description,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: Evidence, createdAt: Instant, updatedAt: Instant): EvidenceJpaEntity = EvidenceJpaEntity(
            id = domain.id.value,
            tenantId = domain.tenantId.value,
            name = domain.name,
            description = domain.description,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
