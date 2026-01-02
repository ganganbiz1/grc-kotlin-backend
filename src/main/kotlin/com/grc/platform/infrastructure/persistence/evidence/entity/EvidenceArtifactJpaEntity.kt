package com.grc.platform.infrastructure.persistence.evidence.entity

import com.grc.platform.domain.evidence.model.ArtifactType
import com.grc.platform.domain.evidence.model.EvidenceArtifact
import com.grc.platform.domain.evidence.model.EvidenceArtifactId
import com.grc.platform.domain.evidence.model.EvidenceId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "evidence_artifacts")
class EvidenceArtifactJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "evidence_id", nullable = false, columnDefinition = "uuid")
    val evidenceId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(name = "artifact_type", nullable = false, length = 20)
    val artifactType: ArtifactType,

    @Column(name = "file_path", length = 500)
    val filePath: String?,

    @Column(name = "url", length = 2000)
    val url: String?,

    @Column(name = "hash", length = 64)
    val hash: String?,

    @Column(name = "size_bytes")
    val sizeBytes: Long?,

    @Column(name = "collected_at")
    val collectedAt: Instant?,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    fun toDomain(): EvidenceArtifact = EvidenceArtifact(
        id = EvidenceArtifactId(id),
        evidenceId = EvidenceId(evidenceId),
        artifactType = artifactType,
        filePath = filePath,
        url = url,
        hash = hash,
        sizeBytes = sizeBytes,
        collectedAt = collectedAt
    )

    companion object {
        fun fromDomain(domain: EvidenceArtifact, now: Instant): EvidenceArtifactJpaEntity = EvidenceArtifactJpaEntity(
            id = domain.id.value,
            evidenceId = domain.evidenceId.value,
            artifactType = domain.artifactType,
            filePath = domain.filePath,
            url = domain.url,
            hash = domain.hash,
            sizeBytes = domain.sizeBytes,
            collectedAt = domain.collectedAt,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: EvidenceArtifact, createdAt: Instant, updatedAt: Instant): EvidenceArtifactJpaEntity = EvidenceArtifactJpaEntity(
            id = domain.id.value,
            evidenceId = domain.evidenceId.value,
            artifactType = domain.artifactType,
            filePath = domain.filePath,
            url = domain.url,
            hash = domain.hash,
            sizeBytes = domain.sizeBytes,
            collectedAt = domain.collectedAt,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
