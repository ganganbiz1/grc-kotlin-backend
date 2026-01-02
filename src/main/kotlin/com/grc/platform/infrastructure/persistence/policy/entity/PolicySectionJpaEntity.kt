package com.grc.platform.infrastructure.persistence.policy.entity

import com.grc.platform.domain.policy.model.PolicyRevisionId
import com.grc.platform.domain.policy.model.PolicySection
import com.grc.platform.domain.policy.model.PolicySectionId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "policy_sections")
class PolicySectionJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "policy_revision_id", nullable = false, columnDefinition = "uuid")
    val policyRevisionId: UUID,

    @Column(name = "parent_id", columnDefinition = "uuid")
    val parentId: UUID?,

    @Column(name = "section_number", nullable = false, length = 20)
    val sectionNumber: String,

    @Column(name = "title", nullable = false, length = 200)
    val title: String,

    @Column(name = "content", columnDefinition = "TEXT")
    val content: String?,

    @Column(name = "display_order", nullable = false)
    val displayOrder: Int,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    fun toDomain(): PolicySection = PolicySection(
        id = PolicySectionId(id),
        policyRevisionId = PolicyRevisionId(policyRevisionId),
        parentId = parentId?.let { PolicySectionId(it) },
        sectionNumber = sectionNumber,
        title = title,
        content = content,
        displayOrder = displayOrder
    )

    companion object {
        fun fromDomain(domain: PolicySection, now: Instant): PolicySectionJpaEntity = PolicySectionJpaEntity(
            id = domain.id.value,
            policyRevisionId = domain.policyRevisionId.value,
            parentId = domain.parentId?.value,
            sectionNumber = domain.sectionNumber,
            title = domain.title,
            content = domain.content,
            displayOrder = domain.displayOrder,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: PolicySection, createdAt: Instant, updatedAt: Instant): PolicySectionJpaEntity = PolicySectionJpaEntity(
            id = domain.id.value,
            policyRevisionId = domain.policyRevisionId.value,
            parentId = domain.parentId?.value,
            sectionNumber = domain.sectionNumber,
            title = domain.title,
            content = domain.content,
            displayOrder = domain.displayOrder,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
