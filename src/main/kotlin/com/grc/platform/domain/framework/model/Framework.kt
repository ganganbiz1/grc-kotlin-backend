package com.grc.platform.domain.framework.model

import com.grc.platform.domain.shared.UUIDv7
import java.util.UUID

@JvmInline
value class FrameworkId(val value: UUID) {
    companion object {
        fun generate(): FrameworkId = FrameworkId(UUIDv7.generate())
        fun fromString(value: String): FrameworkId = FrameworkId(UUID.fromString(value))
    }
}

/**
 * 規格マスタ（SOC2、ISO27001、ISMAP等）
 * テナント非依存の共通マスタ
 */
class Framework(
    val id: FrameworkId,
    val name: String,
    val description: String?,
    val versions: List<FrameworkVersion> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Framework) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "Framework(id=$id, name=$name)"
}
