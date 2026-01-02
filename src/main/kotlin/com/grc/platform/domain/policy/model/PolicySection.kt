package com.grc.platform.domain.policy.model

import com.grc.platform.domain.shared.UUIDv7
import java.util.UUID

@JvmInline
value class PolicySectionId(val value: UUID) {
    companion object {
        fun generate(): PolicySectionId = PolicySectionId(UUIDv7.generate())
        fun fromString(value: String): PolicySectionId = PolicySectionId(UUID.fromString(value))
    }
}

/**
 * 章・条・項
 */
class PolicySection(
    val id: PolicySectionId,
    val policyRevisionId: PolicyRevisionId,
    val parentId: PolicySectionId?,
    val sectionNumber: String,
    val title: String,
    val content: String?,
    val displayOrder: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PolicySection) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "PolicySection(id=$id, sectionNumber=$sectionNumber)"
}
