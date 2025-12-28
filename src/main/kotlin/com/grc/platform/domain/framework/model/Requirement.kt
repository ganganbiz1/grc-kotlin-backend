package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7

@JvmInline
value class RequirementId(val value: String) {
    companion object {
        fun generate(): RequirementId = RequirementId(UUIDv7.generate().toString())
    }
}

class Requirement(
    val id: RequirementId,
    val name: String,
    val shorthand: String,
    val description: String,
    val controls: List<Control>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Requirement) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "Requirement(id=$id, shorthand=$shorthand)"
}
