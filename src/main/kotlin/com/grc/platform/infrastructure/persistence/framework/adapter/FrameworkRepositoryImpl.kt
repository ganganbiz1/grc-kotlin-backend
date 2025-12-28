package com.grc.platform.infrastructure.persistence.framework.adapter

import com.grc.platform.domain.framework.model.Control
import com.grc.platform.domain.framework.model.Framework
import com.grc.platform.domain.framework.model.FrameworkId
import com.grc.platform.domain.framework.model.Requirement
import com.grc.platform.domain.framework.model.RequirementCategory
import com.grc.platform.domain.framework.repository.FrameworkRepository
import com.grc.platform.domain.shared.UUIDv7
import com.grc.platform.infrastructure.persistence.framework.entity.FrameworkJpaEntity
import com.grc.platform.infrastructure.persistence.framework.entity.RequirementCategoryJpaEntity
import com.grc.platform.infrastructure.persistence.framework.entity.RequirementControlJpaEntity
import com.grc.platform.infrastructure.persistence.framework.entity.RequirementJpaEntity
import com.grc.platform.infrastructure.persistence.framework.repository.ControlJpaRepository
import com.grc.platform.infrastructure.persistence.framework.repository.FrameworkJpaRepository
import com.grc.platform.infrastructure.persistence.framework.repository.RequirementCategoryJpaRepository
import com.grc.platform.infrastructure.persistence.framework.repository.RequirementControlJpaRepository
import com.grc.platform.infrastructure.persistence.framework.repository.RequirementJpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.Instant

@Repository
class FrameworkRepositoryImpl(
    private val frameworkJpaRepository: FrameworkJpaRepository,
    private val requirementCategoryJpaRepository: RequirementCategoryJpaRepository,
    private val requirementJpaRepository: RequirementJpaRepository,
    private val requirementControlJpaRepository: RequirementControlJpaRepository,
    private val controlJpaRepository: ControlJpaRepository,
    private val clock: Clock
) : FrameworkRepository {

    override fun findById(id: FrameworkId): Framework? {
        val frameworkEntity = frameworkJpaRepository.findById(id.value).orElse(null) ?: return null
        return assembleFramework(frameworkEntity)
    }

    override fun findAll(): List<Framework> {
        return frameworkJpaRepository.findAll().map { assembleFramework(it) }
    }

    @Transactional
    override fun save(framework: Framework): Framework {
        val now = Instant.now(clock)

        // Save framework
        val existingFramework = frameworkJpaRepository.findById(framework.id.value).orElse(null)
        val frameworkEntity = if (existingFramework != null) {
            FrameworkJpaEntity.fromDomain(framework, existingFramework.createdAt, now)
        } else {
            FrameworkJpaEntity.fromDomain(framework, now)
        }
        frameworkJpaRepository.save(frameworkEntity)

        // Save categories, requirements, and requirement-control mappings
        framework.requirementCategories.forEach { category ->
            saveCategory(category, framework.id.value, now)
        }

        return findById(framework.id)!!
    }

    @Transactional
    override fun delete(id: FrameworkId) {
        val categories = requirementCategoryJpaRepository.findByFrameworkIdOrderByDisplayOrder(id.value)
        val categoryIds = categories.map { it.id }
        val requirements = requirementJpaRepository.findByCategoryIdInOrderByDisplayOrder(categoryIds)
        val requirementIds = requirements.map { it.id }

        // Delete in reverse order of dependencies
        requirementIds.forEach { requirementControlJpaRepository.deleteByRequirementId(it) }
        requirements.forEach { requirementJpaRepository.deleteById(it.id) }
        categories.forEach { requirementCategoryJpaRepository.deleteById(it.id) }
        frameworkJpaRepository.deleteById(id.value)
    }

    private fun saveCategory(category: RequirementCategory, frameworkId: String, now: Instant) {
        val existingCategory = requirementCategoryJpaRepository.findById(category.id.value).orElse(null)
        val categoryEntity = if (existingCategory != null) {
            RequirementCategoryJpaEntity.fromDomain(category, frameworkId, existingCategory.createdAt, now)
        } else {
            RequirementCategoryJpaEntity.fromDomain(category, frameworkId, now)
        }
        requirementCategoryJpaRepository.save(categoryEntity)

        category.requirements.forEach { requirement ->
            saveRequirement(requirement, category.id.value, now)
        }
    }

    private fun saveRequirement(requirement: Requirement, categoryId: String, now: Instant) {
        val existingRequirement = requirementJpaRepository.findById(requirement.id.value).orElse(null)
        val requirementEntity = if (existingRequirement != null) {
            RequirementJpaEntity.fromDomain(requirement, categoryId, existingRequirement.createdAt, now)
        } else {
            RequirementJpaEntity.fromDomain(requirement, categoryId, now)
        }
        requirementJpaRepository.save(requirementEntity)

        // Update requirement-control mappings
        val existingMappings = requirementControlJpaRepository.findByRequirementId(requirement.id.value)
        val existingControlIds = existingMappings.map { it.controlId }.toSet()
        val newControlIds = requirement.controls.map { it.id.value }.toSet()

        // Delete removed mappings
        existingMappings.filter { it.controlId !in newControlIds }
            .forEach { requirementControlJpaRepository.deleteById(it.id) }

        // Add new mappings
        newControlIds.filter { it !in existingControlIds }
            .forEach { controlId ->
                val mapping = RequirementControlJpaEntity.create(
                    id = UUIDv7.generate().toString(),
                    requirementId = requirement.id.value,
                    controlId = controlId,
                    now = now
                )
                requirementControlJpaRepository.save(mapping)
            }
    }

    private fun assembleFramework(frameworkEntity: FrameworkJpaEntity): Framework {
        val categoryEntities = requirementCategoryJpaRepository
            .findByFrameworkIdOrderByDisplayOrder(frameworkEntity.id)

        val categoryIds = categoryEntities.map { it.id }
        val requirementEntities = requirementJpaRepository.findByCategoryIdInOrderByDisplayOrder(categoryIds)
        val requirementIds = requirementEntities.map { it.id }

        val mappings = requirementControlJpaRepository.findByRequirementIdIn(requirementIds)
        val controlIds = mappings.map { it.controlId }.distinct()
        val controls = controlJpaRepository.findByIdIn(controlIds).map { it.toDomain() }
        val controlMap = controls.associateBy { it.id.value }

        val requirementsByCategoryId = requirementEntities.groupBy { it.categoryId }
        val mappingsByRequirementId = mappings.groupBy { it.requirementId }

        val categories = categoryEntities.map { categoryEntity ->
            val requirements = (requirementsByCategoryId[categoryEntity.id] ?: emptyList()).map { reqEntity ->
                val reqControls = (mappingsByRequirementId[reqEntity.id] ?: emptyList())
                    .mapNotNull { controlMap[it.controlId] }
                reqEntity.toDomain(reqControls)
            }
            categoryEntity.toDomain(requirements)
        }

        return frameworkEntity.toDomain(categories)
    }
}
