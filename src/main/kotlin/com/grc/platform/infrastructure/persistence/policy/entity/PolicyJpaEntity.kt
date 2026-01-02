package com.grc.platform.infrastructure.persistence.policy.entity

import com.grc.platform.domain.control.model.TenantId
import com.grc.platform.domain.policy.model.Policy
import com.grc.platform.domain.policy.model.PolicyId
import com.grc.platform.domain.policy.model.PolicyRevision
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "policies")
class PolicyJpaEntity(
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
    fun toDomain(revisions: List<PolicyRevision> = emptyList()): Policy = Policy(
        id = PolicyId(id),
        tenantId = TenantId(tenantId),
        name = name,
        description = description,
        revisions = revisions
    )

    companion object {
        fun fromDomain(domain: Policy, now: Instant): PolicyJpaEntity = PolicyJpaEntity(
            id = domain.id.value,
            tenantId = domain.tenantId.value,
            name = domain.name,
            description = domain.description,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: Policy, createdAt: Instant, updatedAt: Instant): PolicyJpaEntity = PolicyJpaEntity(
            id = domain.id.value,
            tenantId = domain.tenantId.value,
            name = domain.name,
            description = domain.description,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
