package com.grc.platform.infrastructure.persistence.framework.adapter

import com.grc.platform.domain.framework.model.Framework
import com.grc.platform.domain.framework.model.FrameworkControl
import com.grc.platform.domain.framework.model.FrameworkId
import com.grc.platform.domain.framework.model.FrameworkVersion
import com.grc.platform.domain.framework.model.Requirement
import com.grc.platform.domain.framework.model.RequirementCategory
import com.grc.platform.domain.framework.repository.FrameworkRepository
import com.grc.platform.infrastructure.persistence.framework.entity.FrameworkControlJpaEntity
import com.grc.platform.infrastructure.persistence.framework.entity.FrameworkJpaEntity
import com.grc.platform.infrastructure.persistence.framework.entity.FrameworkVersionJpaEntity
import com.grc.platform.infrastructure.persistence.framework.entity.RequirementCategoryJpaEntity
import com.grc.platform.infrastructure.persistence.framework.entity.RequirementJpaEntity
import com.grc.platform.infrastructure.persistence.framework.repository.FrameworkControlJpaRepository
import com.grc.platform.infrastructure.persistence.framework.repository.FrameworkJpaRepository
import com.grc.platform.infrastructure.persistence.framework.repository.FrameworkVersionJpaRepository
import com.grc.platform.infrastructure.persistence.framework.repository.RequirementCategoryJpaRepository
import com.grc.platform.infrastructure.persistence.framework.repository.RequirementJpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.Instant

@Repository
class FrameworkRepositoryImpl(
    private val frameworkJpaRepository: FrameworkJpaRepository,
    private val frameworkVersionJpaRepository: FrameworkVersionJpaRepository,
    private val requirementCategoryJpaRepository: RequirementCategoryJpaRepository,
    private val requirementJpaRepository: RequirementJpaRepository,
    private val frameworkControlJpaRepository: FrameworkControlJpaRepository,
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

        // Save versions, categories, requirements, and framework controls
        framework.versions.forEach { version ->
            saveVersion(version, now)
        }

        return findById(framework.id)!!
    }

    @Transactional
    override fun delete(id: FrameworkId) {
        val versions = frameworkVersionJpaRepository.findByFrameworkIdOrderByCreatedAtDesc(id.value)

        versions.forEach { version ->
            val categories = requirementCategoryJpaRepository.findByFrameworkVersionIdOrderByDisplayOrder(version.id)
            val categoryIds = categories.map { it.id }
            val requirements = requirementJpaRepository.findByCategoryIdInOrderByDisplayOrder(categoryIds)
            val requirementIds = requirements.map { it.id }

            // Delete framework controls
            val frameworkControls = frameworkControlJpaRepository.findByRequirementIdInOrderByDisplayOrder(requirementIds)
            frameworkControls.forEach { frameworkControlJpaRepository.deleteById(it.id) }

            // Delete requirements
            requirements.forEach { requirementJpaRepository.deleteById(it.id) }

            // Delete categories
            categories.forEach { requirementCategoryJpaRepository.deleteById(it.id) }

            // Delete version
            frameworkVersionJpaRepository.deleteById(version.id)
        }

        frameworkJpaRepository.deleteById(id.value)
    }

    private fun saveVersion(version: FrameworkVersion, now: Instant) {
        val existingVersion = frameworkVersionJpaRepository.findById(version.id.value).orElse(null)
        val versionEntity = if (existingVersion != null) {
            FrameworkVersionJpaEntity.fromDomain(version, existingVersion.createdAt, now)
        } else {
            FrameworkVersionJpaEntity.fromDomain(version, now)
        }
        frameworkVersionJpaRepository.save(versionEntity)

        version.categories.forEach { category ->
            saveCategory(category, now)
        }
    }

    private fun saveCategory(category: RequirementCategory, now: Instant) {
        val existingCategory = requirementCategoryJpaRepository.findById(category.id.value).orElse(null)
        val categoryEntity = if (existingCategory != null) {
            RequirementCategoryJpaEntity.fromDomain(category, existingCategory.createdAt, now)
        } else {
            RequirementCategoryJpaEntity.fromDomain(category, now)
        }
        requirementCategoryJpaRepository.save(categoryEntity)

        category.requirements.forEach { requirement ->
            saveRequirement(requirement, now)
        }
    }

    private fun saveRequirement(requirement: Requirement, now: Instant) {
        val existingRequirement = requirementJpaRepository.findById(requirement.id.value).orElse(null)
        val requirementEntity = if (existingRequirement != null) {
            RequirementJpaEntity.fromDomain(requirement, existingRequirement.createdAt, now)
        } else {
            RequirementJpaEntity.fromDomain(requirement, now)
        }
        requirementJpaRepository.save(requirementEntity)

        requirement.frameworkControls.forEach { frameworkControl ->
            saveFrameworkControl(frameworkControl, now)
        }
    }

    private fun saveFrameworkControl(frameworkControl: FrameworkControl, now: Instant) {
        val existingControl = frameworkControlJpaRepository.findById(frameworkControl.id.value).orElse(null)
        val controlEntity = if (existingControl != null) {
            FrameworkControlJpaEntity.fromDomain(frameworkControl, existingControl.createdAt, now)
        } else {
            FrameworkControlJpaEntity.fromDomain(frameworkControl, now)
        }
        frameworkControlJpaRepository.save(controlEntity)
    }

    private fun assembleFramework(frameworkEntity: FrameworkJpaEntity): Framework {
        val versionEntities = frameworkVersionJpaRepository.findByFrameworkIdOrderByCreatedAtDesc(frameworkEntity.id)

        val versions = versionEntities.map { versionEntity ->
            assembleVersion(versionEntity)
        }

        return frameworkEntity.toDomain(versions)
    }

    private fun assembleVersion(versionEntity: FrameworkVersionJpaEntity): FrameworkVersion {
        val categoryEntities = requirementCategoryJpaRepository
            .findByFrameworkVersionIdOrderByDisplayOrder(versionEntity.id)

        val categoryIds = categoryEntities.map { it.id }
        val requirementEntities = requirementJpaRepository.findByCategoryIdInOrderByDisplayOrder(categoryIds)
        val requirementIds = requirementEntities.map { it.id }

        val frameworkControlEntities = frameworkControlJpaRepository.findByRequirementIdInOrderByDisplayOrder(requirementIds)
        val controlsByRequirementId = frameworkControlEntities.groupBy { it.requirementId }

        val requirementsByCategoryId = requirementEntities.groupBy { it.categoryId }

        val categories = categoryEntities.map { categoryEntity ->
            val requirements = (requirementsByCategoryId[categoryEntity.id] ?: emptyList()).map { reqEntity ->
                val frameworkControls = (controlsByRequirementId[reqEntity.id] ?: emptyList())
                    .map { it.toDomain() }
                reqEntity.toDomain(frameworkControls)
            }
            categoryEntity.toDomain(requirements)
        }

        return versionEntity.toDomain(categories)
    }
}
