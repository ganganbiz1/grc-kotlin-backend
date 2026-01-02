package com.grc.platform.infrastructure.persistence.control.entity

import com.grc.platform.domain.control.model.ControlFrameworkMapping
import com.grc.platform.domain.control.model.ControlFrameworkMappingId
import com.grc.platform.domain.control.model.ControlId
import com.grc.platform.domain.control.model.TenantId
import com.grc.platform.domain.framework.model.FrameworkControlId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "control_framework_mappings")
class ControlFrameworkMappingJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "tenant_id", nullable = false, columnDefinition = "uuid")
    val tenantId: UUID,

    @Column(name = "control_id", nullable = false, columnDefinition = "uuid")
    val controlId: UUID,

    @Column(name = "framework_control_id", nullable = false, columnDefinition = "uuid")
    val frameworkControlId: UUID,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant
) {
    fun toDomain(): ControlFrameworkMapping = ControlFrameworkMapping(
        id = ControlFrameworkMappingId(id),
        tenantId = TenantId(tenantId),
        controlId = ControlId(controlId),
        frameworkControlId = FrameworkControlId(frameworkControlId)
    )

    companion object {
        fun fromDomain(domain: ControlFrameworkMapping, now: Instant): ControlFrameworkMappingJpaEntity = ControlFrameworkMappingJpaEntity(
            id = domain.id.value,
            tenantId = domain.tenantId.value,
            controlId = domain.controlId.value,
            frameworkControlId = domain.frameworkControlId.value,
            createdAt = now
        )
    }
}
