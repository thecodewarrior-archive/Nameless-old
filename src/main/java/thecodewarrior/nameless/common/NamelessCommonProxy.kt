package thecodewarrior.nameless.common

import com.teamwizardry.librarianlib.common.util.forCap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import thecodewarrior.nameless.NamelessMod
import thecodewarrior.nameless.common.network.Network
import thecodewarrior.nameless.common.spells.SpellRegistry
import thecodewarrior.nameless.common.vitae.VitaeCap
import thecodewarrior.nameless.common.vitae.VitaeCapProvider

/**
 * Created by TheCodeWarrior
 */
open class NamelessCommonProxy {
    open val isClient = false
    open val isDedicatedServer = true

    init {
        @Suppress("LeakingThis")
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun playerAttach(e: AttachCapabilitiesEvent.Entity) {
        if (e.entity is EntityPlayer) {
            e.addCapability(ResourceLocation(NamelessMod.MODID, "vitae"), VitaeCapProvider())
        }
    }

    @SubscribeEvent
    fun tick(e: TickEvent.PlayerTickEvent) {
        val entity = e.player
        entity.forCap(VitaeCap.CAPABILITY, null) { cap ->
            cap.tick(entity)
        }
    }

    open fun pre(e: FMLPreInitializationEvent) {
        Network
        SpellRegistry
    }
    open fun init(e: FMLInitializationEvent) {}
    open fun post(e: FMLPostInitializationEvent) {}
}
