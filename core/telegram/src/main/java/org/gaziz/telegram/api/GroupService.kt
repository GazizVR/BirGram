package org.gaziz.telegram.api

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.telegram.api.model.group.BasicGroup
import org.gaziz.telegram.api.model.group.SuperGroup

interface GroupService {
    val superGroups: StateFlow<Map<Long, SuperGroup>>
    fun updateSuperGroups(
        updFun: (Map<Long, SuperGroup>) -> Map<Long, SuperGroup>
    )
    val basicGroups: StateFlow<Map<Long, BasicGroup>>
    fun updateBasicGroups(
        updFun: (Map<Long, BasicGroup>) -> Map<Long, BasicGroup>
    )
}