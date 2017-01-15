package thecodewarrior.nameless

import com.teamwizardry.librarianlib.common.core.LoggerBase
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import thecodewarrior.nameless.common.NamelessCommonProxy

/**
 * Created by TheCodeWarrior
 */
@Mod(modid = NamelessMod.MODID, version = NamelessMod.VERSION, name = NamelessMod.MODNAME)
class NamelessMod {

    @Mod.EventHandler
    fun preInit(e: FMLPreInitializationEvent) {
        PROXY.pre(e)
    }

    @Mod.EventHandler
    fun init(e: FMLInitializationEvent) {
        PROXY.init(e)
    }

    @Mod.EventHandler
    fun postInit(e: FMLPostInitializationEvent) {
        PROXY.post(e)
    }

    companion object {

        const val MODID = "nameless"
        const val MODNAME = "Nameless"
        const val VERSION = "0.0"
        const val CLIENT = "thecodewarrior.nameless.client.NamelessClientProxy"
        const val SERVER = "thecodewarrior.nameless.common.NamelessCommonProxy"

        @JvmStatic
        @SidedProxy(clientSide = CLIENT, serverSide = SERVER)
        lateinit var PROXY: NamelessCommonProxy

        @JvmStatic
        lateinit var INSTANCE: NamelessMod

        @JvmField
        val DEV_ENVIRONMENT = Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean

        val isClient: Boolean
            get() = PROXY.isClient
        val isDedicatedServer: Boolean
            get() = PROXY.isDedicatedServer
    }

}

object NLog : LoggerBase("Nameless")
