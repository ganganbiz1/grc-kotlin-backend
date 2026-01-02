package com.grc.platform.domain.control.model

import com.grc.platform.domain.shared.UUIDv7
import java.util.UUID

@JvmInline
value class ControlTestMappingId(val value: UUID) {
    companion object {
        fun generate(): ControlTestMappingId = ControlTestMappingId(UUIDv7.generate())
        fun fromString(value: String): ControlTestMappingId = ControlTestMappingId(UUID.fromString(value))
    }
}

@JvmInline
value class TestId(val value: UUID) {
    companion object {
        fun generate(): TestId = TestId(UUIDv7.generate())
        fun fromString(value: String): TestId = TestId(UUID.fromString(value))
    }
}

/**
 * Control ↔ Test（多:多）
 * ※ testsテーブルは将来実装
 */
class ControlTestMapping(
    val id: ControlTestMappingId,
    val tenantId: TenantId,
    val controlId: ControlId,
    val testId: TestId
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ControlTestMapping) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "ControlTestMapping(id=$id, controlId=$controlId, testId=$testId)"
}
