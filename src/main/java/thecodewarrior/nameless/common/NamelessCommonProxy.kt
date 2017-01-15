package thecodewarrior.nameless.common

import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

/**
 * Created by TheCodeWarrior
 */
open class NamelessCommonProxy {
    open val isClient = false
    open val isDedicatedServer = true

    open fun pre(e: FMLPreInitializationEvent) {}
    open fun init(e: FMLInitializationEvent) {}
    open fun post(e: FMLPostInitializationEvent) {}
}
