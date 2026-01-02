package com.grc.platform.infrastructure.persistence.control.entity

import com.grc.platform.domain.control.model.ControlId
import com.grc.platform.domain.control.model.ControlTestMapping
import com.grc.platform.domain.control.model.ControlTestMappingId
import com.grc.platform.domain.control.model.TenantId
import com.grc.platform.domain.control.model.TestId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "control_test_mappings")
class ControlTestMappingJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "tenant_id", nullable = false, columnDefinition = "uuid")
    val tenantId: UUID,

    @Column(name = "control_id", nullable = false, columnDefinition = "uuid")
    val controlId: UUID,

    @Column(name = "test_id", nullable = false, columnDefinition = "uuid")
    val testId: UUID,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant
) {
    fun toDomain(): ControlTestMapping = ControlTestMapping(
        id = ControlTestMappingId(id),
        tenantId = TenantId(tenantId),
        controlId = ControlId(controlId),
        testId = TestId(testId)
    )

    companion object {
        fun fromDomain(domain: ControlTestMapping, now: Instant): ControlTestMappingJpaEntity = ControlTestMappingJpaEntity(
            id = domain.id.value,
            tenantId = domain.tenantId.value,
            controlId = domain.controlId.value,
            testId = domain.testId.value,
            createdAt = now
        )
    }
}
