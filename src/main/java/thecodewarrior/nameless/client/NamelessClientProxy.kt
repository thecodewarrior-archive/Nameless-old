package thecodewarrior.nameless.client

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import thecodewarrior.nameless.client.particle.Particles
import thecodewarrior.nameless.common.NamelessCommonProxy

/**
 * Created by TheCodeWarrior
 */
class NamelessClientProxy : NamelessCommonProxy() {
    override val isClient = true
    override val isDedicatedServer = false

    override fun pre(e: FMLPreInitializationEvent) {
        super.pre(e)

        Keybinds
        Particles
    }
}
