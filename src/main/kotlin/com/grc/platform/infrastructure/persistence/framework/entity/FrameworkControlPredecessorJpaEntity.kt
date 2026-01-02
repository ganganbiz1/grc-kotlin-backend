package com.grc.platform.infrastructure.persistence.framework.entity

import com.grc.platform.domain.framework.model.FrameworkControlId
import com.grc.platform.domain.framework.model.FrameworkControlPredecessor
import com.grc.platform.domain.framework.model.FrameworkControlPredecessorId
import com.grc.platform.domain.framework.model.PredecessorStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "framework_control_predecessors")
class FrameworkControlPredecessorJpaEntity(
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    val id: UUID,

    @Column(name = "framework_control_id", nullable = false, columnDefinition = "uuid")
    val frameworkControlId: UUID,

    @Column(name = "predecessor_id", nullable = false, columnDefinition = "uuid")
    val predecessorId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    val status: PredecessorStatus,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant
) {
    fun toDomain(): FrameworkControlPredecessor = FrameworkControlPredecessor(
        id = FrameworkControlPredecessorId(id),
        frameworkControlId = FrameworkControlId(frameworkControlId),
        predecessorId = FrameworkControlId(predecessorId),
        status = status
    )

    companion object {
        fun fromDomain(domain: FrameworkControlPredecessor, now: Instant): FrameworkControlPredecessorJpaEntity = FrameworkControlPredecessorJpaEntity(
            id = domain.id.value,
            frameworkControlId = domain.frameworkControlId.value,
            predecessorId = domain.predecessorId.value,
            status = domain.status,
            createdAt = now
        )
    }
}
