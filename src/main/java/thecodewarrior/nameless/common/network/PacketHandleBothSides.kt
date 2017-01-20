package thecodewarrior.nameless.common.network

import com.teamwizardry.librarianlib.common.network.PacketBase
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

/**
 * Created by TheCodeWarrior
 */
abstract class PacketHandleBothSides : PacketBase() {

    override fun handle(ctx: MessageContext) {
        if(ctx.side == Side.SERVER) {
            handle(ctx.serverHandler.playerEntity)
        }
        if(ctx.side == Side.CLIENT) {
            handle(Minecraft.getMinecraft().player)
        }
    }

    abstract fun handle(player: EntityPlayer)
}
