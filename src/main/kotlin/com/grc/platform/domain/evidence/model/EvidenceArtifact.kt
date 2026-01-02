package com.grc.platform.domain.evidence.model

import com.grc.platform.domain.shared.UUIDv7
import java.time.Instant
import java.util.UUID

@JvmInline
value class EvidenceArtifactId(val value: UUID) {
    companion object {
        fun generate(): EvidenceArtifactId = EvidenceArtifactId(UUIDv7.generate())
        fun fromString(value: String): EvidenceArtifactId = EvidenceArtifactId(UUID.fromString(value))
    }
}

enum class ArtifactType {
    FILE,
    URL,
    SNAPSHOT
}

/**
 * ファイル/URL/スナップショット等の実体
 */
class EvidenceArtifact(
    val id: EvidenceArtifactId,
    val evidenceId: EvidenceId,
    val artifactType: ArtifactType,
    val filePath: String?,
    val url: String?,
    val hash: String?,
    val sizeBytes: Long?,
    val collectedAt: Instant?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EvidenceArtifact) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "EvidenceArtifact(id=$id, artifactType=$artifactType)"
}
