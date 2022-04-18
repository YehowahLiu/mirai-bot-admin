package cc.redme.mirai.plugin.botadmin.command

import cc.redme.mirai.plugin.botadmin.PluginMain
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.message.data.buildMessageChain

object MemberCommand: CompositeCommand(
    PluginMain, "member", "群员",
    description = "群员相关指令",
){
    @SubCommand("mute", "禁言")
    suspend fun CommandSender.mute(member: NormalMember, time: Int){
        try {
            member.mute(time)
        }catch (e: PermissionDeniedException){
            sendMessage("无权限")
        }
    }

    @SubCommand("unmute", "解除禁言")
    suspend fun CommandSender.unmute(member: NormalMember){
        try {
            member.unmute()
        }catch (e: PermissionDeniedException){
            sendMessage("无权限")
        }
    }

    @SubCommand("status", "info", "状态", "信息")
    suspend fun CommandSender.status(member: Member){
        buildMessageChain {
            // TODO: 首行显示头像
            if (member is NormalMember) {
                +(member.asFriendOrNull()?: member.asStranger()).nick
                +" @ ${member.id}\n"
                +"名片: ${member.nameCard} @ ${member.group.name}\n"
                +"头衔: ${member.specialTitle}\n"
                +"权限: ${
                    when (member.permission.level) {
                        0 -> "普通成员"
                        1 -> "管理员"
                        2 -> "群主"
                        else -> "未知"
                    }
                }\n"
                +"禁言状态:"
                +if (member.isMuted) {
                    "${member.muteTimeRemaining}秒"
                } else {
                    "未禁言"
                }
            } else {
                +"匿名成员"
            }
        }.also { sendMessage(it) }
    }

    @SubCommand("kick", "remove", "rm", "踢出", "移除")
    suspend fun CommandSender.kick(member: NormalMember, message: String = "您已被移出群聊"){
        try {
            member.kick(message)
        }catch (e: PermissionDeniedException){
            sendMessage("无权限")
        }
    }

    @SubCommand("setNameCard", "设置群名片")
    suspend fun CommandSender.setNameCard(member: Member, nameCard: String){
        try {
            (member as NormalMember).nameCard = nameCard
        }catch (e: PermissionDeniedException){
            sendMessage("无权限")
        }
    }

    @SubCommand("setSpecialTitle", "设置群头衔")
    suspend fun CommandSender.setSpecialTitle(member: Member, specialTitle: String){
        try {
            (member as NormalMember).specialTitle = specialTitle
        }catch (e: PermissionDeniedException){
            sendMessage("无权限")
        }
    }

    @SubCommand("setAdmin", "addAdmin", "设置管理员")
    suspend fun CommandSender.setAdmin(member: NormalMember){
        try {
            member.modifyAdmin(true)
        }catch (e: PermissionDeniedException){
            sendMessage("无权限")
        }
    }

    @SubCommand("unsetAdmin", "rmAdmin", "取消管理员")
    suspend fun CommandSender.unsetAdmin(member: NormalMember){
        try {
            member.modifyAdmin(false)
        }catch (e: PermissionDeniedException){
            sendMessage("无权限")
        }
    }
}