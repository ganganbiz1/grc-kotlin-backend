package com.grc.platform.infrastructure.persistence.control.entity

import com.grc.platform.domain.control.model.ControlId
import com.grc.platform.domain.control.model.ControlPolicyMapping
import com.grc.platform.domain.control.model.ControlPolicyMappingId
import com.grc.platform.domain.control.model.TenantId
import com.grc.platform.domain.policy.model.PolicyId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "control_policy_mappings")
class ControlPolicyMappingJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "tenant_id", nullable = false, columnDefinition = "uuid")
    val tenantId: UUID,

    @Column(name = "control_id", nullable = false, columnDefinition = "uuid")
    val controlId: UUID,

    @Column(name = "policy_id", nullable = false, columnDefinition = "uuid")
    val policyId: UUID,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant
) {
    fun toDomain(): ControlPolicyMapping = ControlPolicyMapping(
        id = ControlPolicyMappingId(id),
        tenantId = TenantId(tenantId),
        controlId = ControlId(controlId),
        policyId = PolicyId(policyId)
    )

    companion object {
        fun fromDomain(domain: ControlPolicyMapping, now: Instant): ControlPolicyMappingJpaEntity = ControlPolicyMappingJpaEntity(
            id = domain.id.value,
            tenantId = domain.tenantId.value,
            controlId = domain.controlId.value,
            policyId = domain.policyId.value,
            createdAt = now
        )
    }
}
