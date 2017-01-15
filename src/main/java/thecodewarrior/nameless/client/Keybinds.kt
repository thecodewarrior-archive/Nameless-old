package thecodewarrior.nameless.client

import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.lwjgl.input.Keyboard
import thecodewarrior.nameless.client.particle.Particles

/**
 * Created by TheCodeWarrior
 */
object Keybinds {
    val prefix = KeyBinding("nameless.key.prefix", Keyboard.KEY_X, "key.categories.gameplay")
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun keyEvent(e: InputEvent) {
        if(prefix.isPressed) {
            Particles.spawn(Minecraft.getMinecraft().player)
            //Minecraft.getMinecraft().displayGuiScreen(GuiSpells())
        }
    }
}
