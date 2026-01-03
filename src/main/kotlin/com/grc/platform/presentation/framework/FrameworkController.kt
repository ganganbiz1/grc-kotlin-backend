package com.grc.platform.presentation.framework

import com.grc.platform.application.framework.FrameworkCommandService
import com.grc.platform.application.framework.FrameworkQueryService
import com.grc.platform.domain.framework.model.FrameworkId
import com.grc.platform.domain.framework.model.FrameworkVersionId
import com.grc.platform.presentation.framework.api.FrameworkApi
import com.grc.platform.presentation.framework.model.CreateFrameworkRequest
import com.grc.platform.presentation.framework.model.FrameworkDetail
import com.grc.platform.presentation.framework.model.FrameworkIdResponse
import com.grc.platform.presentation.framework.model.FrameworkSummary
import com.grc.platform.presentation.framework.model.UpdateFrameworkRequest
import com.grc.platform.presentation.framework.model.CreateVersionRequest
import com.grc.platform.presentation.framework.model.VersionIdResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class FrameworkController(
    private val commandService: FrameworkCommandService,
    private val queryService: FrameworkQueryService
) : FrameworkApi {

    override fun createFramework(createFrameworkRequest: CreateFrameworkRequest): ResponseEntity<FrameworkIdResponse> {
        val frameworkId = commandService.createFramework(
            name = createFrameworkRequest.name,
            description = createFrameworkRequest.description
        )
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(FrameworkIdResponse(id = frameworkId.value))
    }

    override fun listFrameworks(): ResponseEntity<List<FrameworkSummary>> {
        val frameworks = queryService.findAll()
        return ResponseEntity.ok(frameworks)
    }

    override fun getFramework(id: UUID): ResponseEntity<FrameworkDetail> {
        val framework = queryService.findById(FrameworkId(id))
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(framework)
    }

    override fun updateFramework(
        id: UUID,
        updateFrameworkRequest: UpdateFrameworkRequest
    ): ResponseEntity<FrameworkDetail> {
        val framework = commandService.updateFramework(
            id = FrameworkId(id),
            name = updateFrameworkRequest.name,
            description = updateFrameworkRequest.description
        ) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(framework)
    }

    override fun deleteFramework(id: UUID): ResponseEntity<Unit> {
        val deleted = commandService.deleteFramework(FrameworkId(id))
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    override fun createVersion(
        id: UUID,
        createVersionRequest: CreateVersionRequest
    ): ResponseEntity<VersionIdResponse> {
        val versionId = commandService.createVersion(
            frameworkId = FrameworkId(id),
            versionNumber = createVersionRequest.versionNumber,
            effectiveDate = createVersionRequest.effectiveDate
        ) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(VersionIdResponse(id = versionId.value))
    }

    override fun activateVersion(versionId: UUID): ResponseEntity<Unit> {
        val activated = commandService.activateVersion(FrameworkVersionId(versionId))
        return if (activated) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
