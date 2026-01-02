package com.grc.platform.infrastructure.persistence.framework.entity

import com.grc.platform.domain.framework.model.FrameworkControl
import com.grc.platform.domain.framework.model.FrameworkControlId
import com.grc.platform.domain.framework.model.FrameworkVersionId
import com.grc.platform.domain.framework.model.MappingPolicy
import com.grc.platform.domain.framework.model.RequirementId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "framework_controls")
class FrameworkControlJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "requirement_id", nullable = false, columnDefinition = "uuid")
    val requirementId: UUID,

    @Column(name = "framework_version_id", nullable = false, columnDefinition = "uuid")
    val frameworkVersionId: UUID,

    @Column(name = "canonical_key", nullable = false, length = 100)
    val canonicalKey: String,

    @Column(name = "display_code", nullable = false, length = 50)
    val displayCode: String,

    @Column(name = "title", nullable = false, length = 255)
    val title: String,

    @Column(name = "text", columnDefinition = "TEXT")
    val text: String?,

    @Column(name = "content_hash", length = 64)
    val contentHash: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "mapping_policy", nullable = false, length = 20)
    val mappingPolicy: MappingPolicy,

    @Column(name = "display_order", nullable = false)
    val displayOrder: Int,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    fun toDomain(): FrameworkControl = FrameworkControl(
        id = FrameworkControlId(id),
        requirementId = RequirementId(requirementId),
        frameworkVersionId = FrameworkVersionId(frameworkVersionId),
        canonicalKey = canonicalKey,
        displayCode = displayCode,
        title = title,
        text = text,
        contentHash = contentHash,
        mappingPolicy = mappingPolicy,
        displayOrder = displayOrder
    )

    companion object {
        fun fromDomain(domain: FrameworkControl, now: Instant): FrameworkControlJpaEntity = FrameworkControlJpaEntity(
            id = domain.id.value,
            requirementId = domain.requirementId.value,
            frameworkVersionId = domain.frameworkVersionId.value,
            canonicalKey = domain.canonicalKey,
            displayCode = domain.displayCode,
            title = domain.title,
            text = domain.text,
            contentHash = domain.contentHash,
            mappingPolicy = domain.mappingPolicy,
            displayOrder = domain.displayOrder,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: FrameworkControl, createdAt: Instant, updatedAt: Instant): FrameworkControlJpaEntity = FrameworkControlJpaEntity(
            id = domain.id.value,
            requirementId = domain.requirementId.value,
            frameworkVersionId = domain.frameworkVersionId.value,
            canonicalKey = domain.canonicalKey,
            displayCode = domain.displayCode,
            title = domain.title,
            text = domain.text,
            contentHash = domain.contentHash,
            mappingPolicy = domain.mappingPolicy,
            displayOrder = domain.displayOrder,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
