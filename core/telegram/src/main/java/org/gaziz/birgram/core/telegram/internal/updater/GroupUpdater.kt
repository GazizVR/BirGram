package org.gaziz.birgram.core.telegram.internal.updater

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.GroupService
import org.gaziz.birgram.core.telegram.internal.mapper.toBasicGroup
import org.gaziz.birgram.core.telegram.internal.mapper.toSuperGroup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupUpdater @Inject constructor(
    private val groupService: GroupService
) {
    fun onBasicGroup(u: TdApi.UpdateBasicGroup){
        groupService.updateBasicGroups { old ->
            old + (u.basicGroup.id to u.basicGroup.toBasicGroup())
        }
    }
    fun onSuperGroup(u: TdApi.UpdateSupergroup) {
        groupService.updateSuperGroups { old ->
            old + (u.supergroup.id to u.supergroup.toSuperGroup())
        }
    }
}