package cc.redme.mirai.plugin.botadmin.utils

import cc.redme.mirai.plugin.botadmin.PluginMain.avatarFolder
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import java.io.File
import java.net.URL
import javax.imageio.ImageIO


object ImageUtils {
    fun getAvatar(user: User): File {
        val avatar = avatarFolder.resolve("u_${user.id}.png")
        downloadImage(user.avatarUrl, avatar)
        return avatar
    }

    fun getGroupAvatar(group: Group): File {
        val avatar = avatarFolder.resolve("g_${group.id}.png")
        downloadImage(group.avatarUrl, avatar)
        return avatar
    }

    // download image from given url and save it to given path
    private fun downloadImage(url: String, output: File) {
        if(output.exists()&&output.lastModified()>System.currentTimeMillis()-1000*60*30)
            return
        else
            output.delete()
        val image = ImageIO.read(URL(url))
        ImageIO.write(image, "png", output)
    }

}