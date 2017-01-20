package thecodewarrior.nameless.client

import com.teamwizardry.librarianlib.common.network.PacketHandler
import com.teamwizardry.librarianlib.common.util.MethodHandleHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.settings.KeyBindingMap
import net.minecraftforge.client.settings.KeyConflictContext
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import thecodewarrior.nameless.NamelessMod
import thecodewarrior.nameless.common.network.PacketBeginSpell
import thecodewarrior.nameless.common.network.PacketEndSpell

/**
 * Created by TheCodeWarrior
 */
object Keybinds {
    val prefix = KeyBinding("key.nameless.prefix", KeyConflictContext.IN_GAME, Keyboard.KEY_X, "key.categories.gameplay")
    val spellKeys = mutableMapOf< ResourceLocation, Int>(ResourceLocation(NamelessMod.MODID, "fireball") to Keyboard.KEY_F)

    var prefixPressed = false
    var prefixMode = false
    var castingMode = false
    var casting: Pair<ResourceLocation, Int>? = null

    init {
        MinecraftForge.EVENT_BUS.register(this)
        ClientRegistry.registerKeyBinding(prefix)
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    fun keyEvent(e: InputEvent) {
        if(prefix.isKeyDown && !prefixPressed) {
            prefixMode = true
        }
        prefixPressed = prefix.isKeyDown

        if(prefixMode) {
            // prevent any of the keybinds for bound keys from activating
            spellKeys.values.forEach {
                KeyBinding.setKeyBindState(it, false)
                keyhash().lookupAll(it).forEach {
                    it.pressed = false
                    it.pressTime = 0
                }
            }

            var pressed: Pair<ResourceLocation, Int>? = null
            spellKeys.forEach {
                if(pressed != null) return@forEach
                if(Keyboard.isKeyDown(it.value))
                    pressed = it.key to it.value
            }

            val loc = pressed?.first

            if(loc != null) {
                prefixMode = false
                castingMode = true
                casting = pressed
                PacketHandler.NETWORK.sendToServer(PacketBeginSpell().apply {
                    name = loc
                    handle(Minecraft.getMinecraft().player)
                })
            }
        }

        val cast = casting
        if(castingMode && cast != null) {
            // prevent keybind for currently pressed cast key from firing
            KeyBinding.setKeyBindState(cast.second, false)
            keyhash().lookupAll(cast.second).forEach {
                it.pressed = false
                it.pressTime = 0
            }
            if(!Keyboard.isKeyDown(cast.second)) {
                castingMode = false
                casting = null
                PacketHandler.NETWORK.sendToServer(PacketEndSpell().apply {
                    handle(Minecraft.getMinecraft().player)
                })
            }

        }
//        if(prefix.isPressed) {
//            Particles.spawn(Minecraft.getMinecraft().player)
//            Minecraft.getMinecraft().displayGuiScreen(GuiSpells())
//        }
    }

    @Suppress("UNCHECKED_CAST")
    val keyhash = MethodHandleHelper.wrapperForStaticGetter(KeyBinding::class.java, "HASH", "field_74514_b") as () -> KeyBindingMap

    @SubscribeEvent
    fun tick(e: TickEvent.ClientTickEvent) {
        if(e.phase != TickEvent.Phase.END) return


    }
}

var KeyBinding.pressed by MethodHandleHelper.delegateForReadWrite<KeyBinding, Boolean>(KeyBinding::class.java, "pressed", "field_74513_e")
var KeyBinding.pressTime by MethodHandleHelper.delegateForReadWrite<KeyBinding, Int>(KeyBinding::class.java, "pressTime", "field_151474_i")

