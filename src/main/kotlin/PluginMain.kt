package cc.redme.mirai.plugin.botadmin

import cc.redme.mirai.plugin.botadmin.command.MemberCommand
import cc.redme.mirai.plugin.botadmin.command.GroupCommand
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "cc.redme.mirai.bot-admin",
        name = "BotAdmin",
        version = "0.1.0"
    ) {
        author("YehowahLiu")
        info(
            """
            BotAdmin
            简单方便地操控机器人行为
        """.trimIndent()
        )
    }
) {

    private val commands = listOf(MemberCommand, GroupCommand)
    val avatarFolder = dataFolder.resolve("avatar")
    override fun onEnable() {
        logger.info { "Plugin loaded" }
        if(avatarFolder.exists().not()) {
            avatarFolder.mkdirs()
        }
        commands.forEach (CommandManager::registerCommand)
    }

    override fun onDisable() {
        logger.info { "Plugin unloaded" }
        commands.forEach(CommandManager.INSTANCE::unregisterCommand)
    }
}
