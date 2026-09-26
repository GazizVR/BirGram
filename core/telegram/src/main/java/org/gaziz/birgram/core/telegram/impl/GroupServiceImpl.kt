package org.gaziz.birgram.core.telegram.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gaziz.birgram.core.telegram.api.GroupService
import org.gaziz.birgram.core.telegram.api.model.group.BasicGroup
import org.gaziz.birgram.core.telegram.api.model.group.SuperGroup
import javax.inject.Inject

class GroupServiceImpl @Inject constructor(): GroupService {
    private val _superGroups = MutableStateFlow<Map<Long, SuperGroup>>(emptyMap())
    override val superGroups: StateFlow<Map<Long, SuperGroup>> = _superGroups.asStateFlow()

    override fun updateSuperGroups(
        updFun: (Map<Long, SuperGroup>) -> Map<Long, SuperGroup>
    ) {
        _superGroups.update(updFun)
    }

    private val _basicGroups = MutableStateFlow<Map<Long, BasicGroup>>(emptyMap())
    override val basicGroups: StateFlow<Map<Long, BasicGroup>> = _basicGroups.asStateFlow()

    override fun updateBasicGroups(
        updFun: (Map<Long, BasicGroup>) -> Map<Long, BasicGroup>
    ) {
        _basicGroups.update(updFun)
    }

}