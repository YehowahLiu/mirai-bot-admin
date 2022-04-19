package cc.redme.mirai.plugin.botadmin.command

import cc.redme.mirai.plugin.botadmin.PluginMain
import cc.redme.mirai.plugin.botadmin.utils.ImageUtils.getGroupAvatar
import kotlinx.coroutines.delay
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.contact.PermissionDeniedException
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

object GroupCommand: CompositeCommand(
    PluginMain, "group", "群组",
    description = "群组相关指令"
){
    @SubCommand("setName", "设置群名")
    suspend fun CommandSender.setName(name: String, group: Group?=getGroupOrNull()){
        try {
            if (group != null) {
                group.name = name
            }else{
                sendMessage("请先选择一个群")
            }
        }catch (e: PermissionDeniedException){
            sendMessage("无权限")
        }
    }

    @SubCommand("muteAll", "全体禁言")
    suspend fun CommandSender.muteAll(bool: Boolean?, group: Group?=getGroupOrNull()){
        try {
            if (group != null) {
                group.settings.isMuteAll = bool ?: !group.settings.isMuteAll
                sendMessage("全体禁言${if (group.settings.isMuteAll) "开启" else "关闭"}")
            }else{
                sendMessage("请先选择一个群")
            }
        }catch (e: PermissionDeniedException){
            sendMessage("无权限")
        }
    }

    @SubCommand("listMember", "lm", "群员列表")
    suspend fun CommandSender.listMember(page: Int=1, group: Group?=getGroupOrNull()){
        if(group == null){
            sendMessage("请先选择一个群")
            return
        }
        val members = group.allMember()
        val pageSize = 10
        val pageCount = members.size / pageSize + if (members.size % pageSize == 0) 0 else 1
        if (page < 1 || page > pageCount) {
            sendMessage("页码超出范围(1-$pageCount)")
            return
        }
        val start = (page - 1) * pageSize
        val end = if (page == pageCount) members.size else page * pageSize
        buildMessageChain {
            for (i in start until end) {
                +"${i + 1}. ${members[i].nick}\n"
            }
            +"第${page}页，共${pageCount}页, 共${members.size}人"
        }.also { sendMessage(it) }
    }

    @SubCommand("anonymous", "匿名聊天")
    suspend fun CommandSender.setAnonymous(bool: Boolean?, group: Group?=getGroupOrNull()){
        try {
            if (group != null) {
                group.settings.isAnonymousChatEnabled = bool ?: !group.settings.isAnonymousChatEnabled
                delay(500)
                sendMessage("匿名聊天${if (group.settings.isAnonymousChatEnabled) "开启" else "关闭"}")
            }else{
                sendMessage("请先选择一个群")
            }
        }catch (e: PermissionDeniedException){
            sendMessage("无权限")
        }
    }

    @SubCommand("status", "info", "状态", "信息")
    suspend fun CommandSender.status(group: Group?=getGroupOrNull()){
        if(group == null){
            sendMessage("请先选择一个群")
            return
        }
        buildMessageChain {
            if(subject!=null) {
                +subject!!.uploadImage(getGroupAvatar(group).toExternalResource())
            }
            +" ${group.name} @ ${group.id}\n"
            +"人数: ${group.allMember().size}\n"
            +"群主: ${group.allMember().find { it.permission.level == 2 }?.nameCardOrNick}\n"
            +"我的权限: ${
                when(group.botAsMember.permission.level){
                    0 -> "群员"
                    1 -> "管理"
                    2 -> "群主"
                    else -> "未知"
                }
            }\n \n"
            +"----群聊设置----\n"
            +"匿名聊天: ${group.settings.isAnonymousChatEnabled}\n"
            +"全体禁言: ${group.settings.isMuteAll}\n"
            +"群员邀人: ${group.settings.isAllowMemberInvite}\n"
        }.also { sendMessage(it) }
    }

    @SubCommand("setAllowInvite", "setAllowMemberInvite", "设置允许邀请")
    suspend fun CommandSender.setAllowInvite(bool: Boolean?, group: Group?=getGroupOrNull()){
        try {
            if (group != null) {
                group.settings.isAllowMemberInvite = bool ?: !group.settings.isAllowMemberInvite
                sendMessage("群员邀请${if (group.settings.isAllowMemberInvite) "开启" else "关闭"}")
            }else{
                sendMessage("请先选择一个群")
            }
        }catch (e: PermissionDeniedException){
            sendMessage("无权限")
        }
    }

    private fun Group.allMember(): MutableList<NormalMember> {
        val payload = members.toMutableList()
        payload.add(botAsMember)
        return payload
    }
}