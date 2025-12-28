package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7

@JvmInline
value class FrameworkId(val value: String) {
    companion object {
        fun generate(): FrameworkId = FrameworkId(UUIDv7.generate().toString())
    }
}

class Framework(
    val id: FrameworkId,
    val displayName: String,
    val shorthandName: String,
    val description: String,
    val version: String,
    val requirementCategories: List<RequirementCategory>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Framework) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "Framework(id=$id, shorthandName=$shorthandName)"
}
