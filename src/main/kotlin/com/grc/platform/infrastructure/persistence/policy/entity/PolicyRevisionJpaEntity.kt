package com.grc.platform.infrastructure.persistence.policy.entity

import com.grc.platform.domain.policy.model.PolicyId
import com.grc.platform.domain.policy.model.PolicyRevision
import com.grc.platform.domain.policy.model.PolicyRevisionId
import com.grc.platform.domain.policy.model.PolicyRevisionStatus
import com.grc.platform.domain.policy.model.PolicySection
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "policy_revisions")
class PolicyRevisionJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "policy_id", nullable = false, columnDefinition = "uuid")
    val policyId: UUID,

    @Column(name = "version", nullable = false, length = 50)
    val version: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    val status: PolicyRevisionStatus,

    @Column(name = "effective_date")
    val effectiveDate: LocalDate?,

    @Column(name = "content", columnDefinition = "TEXT")
    val content: String?,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    fun toDomain(sections: List<PolicySection> = emptyList()): PolicyRevision = PolicyRevision(
        id = PolicyRevisionId(id),
        policyId = PolicyId(policyId),
        version = version,
        status = status,
        effectiveDate = effectiveDate,
        content = content,
        sections = sections
    )

    companion object {
        fun fromDomain(domain: PolicyRevision, now: Instant): PolicyRevisionJpaEntity = PolicyRevisionJpaEntity(
            id = domain.id.value,
            policyId = domain.policyId.value,
            version = domain.version,
            status = domain.status,
            effectiveDate = domain.effectiveDate,
            content = domain.content,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: PolicyRevision, createdAt: Instant, updatedAt: Instant): PolicyRevisionJpaEntity = PolicyRevisionJpaEntity(
            id = domain.id.value,
            policyId = domain.policyId.value,
            version = domain.version,
            status = domain.status,
            effectiveDate = domain.effectiveDate,
            content = domain.content,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
