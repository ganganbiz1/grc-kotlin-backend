package com.grc.platform.infrastructure.persistence.control.entity

import com.grc.platform.domain.control.model.Control
import com.grc.platform.domain.control.model.ControlId
import com.grc.platform.domain.control.model.ControlStatus
import com.grc.platform.domain.control.model.OwnerId
import com.grc.platform.domain.control.model.TenantId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "controls")
class ControlJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "tenant_id", nullable = false, columnDefinition = "uuid")
    val tenantId: UUID,

    @Column(name = "name", nullable = false, length = 200)
    val name: String,

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    val status: ControlStatus,

    @Column(name = "owner_id", columnDefinition = "uuid")
    val ownerId: UUID?,

    @Column(name = "note", columnDefinition = "TEXT")
    val note: String?,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "custom_fields", columnDefinition = "jsonb")
    val customFields: Map<String, Any>?,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
) {
    fun toDomain(): Control = Control(
        id = ControlId(id),
        tenantId = TenantId(tenantId),
        name = name,
        description = description,
        status = status,
        ownerId = ownerId?.let { OwnerId(it) },
        note = note,
        customFields = customFields
    )

    companion object {
        fun fromDomain(domain: Control, now: Instant): ControlJpaEntity = ControlJpaEntity(
            id = domain.id.value,
            tenantId = domain.tenantId.value,
            name = domain.name,
            description = domain.description,
            status = domain.status,
            ownerId = domain.ownerId?.value,
            note = domain.note,
            customFields = domain.customFields,
            createdAt = now,
            updatedAt = now
        )

        fun fromDomain(domain: Control, createdAt: Instant, updatedAt: Instant): ControlJpaEntity = ControlJpaEntity(
            id = domain.id.value,
            tenantId = domain.tenantId.value,
            name = domain.name,
            description = domain.description,
            status = domain.status,
            ownerId = domain.ownerId?.value,
            note = domain.note,
            customFields = domain.customFields,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
