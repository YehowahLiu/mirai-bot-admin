package cc.redme.mirai.plugin.botadmin

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
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}
