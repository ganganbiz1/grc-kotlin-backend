package com.grc.platform.application.framework

import com.grc.platform.domain.framework.model.FrameworkId
import com.grc.platform.presentation.framework.model.FrameworkDetail
import com.grc.platform.presentation.framework.model.FrameworkSummary

/**
 * Framework参照系サービスのインターフェース
 */
interface FrameworkQueryService {
    /**
     * 全ての規格を取得する
     */
    fun findAll(): List<FrameworkSummary>

    /**
     * 指定したIDの規格を取得する
     */
    fun findById(id: FrameworkId): FrameworkDetail?
}
