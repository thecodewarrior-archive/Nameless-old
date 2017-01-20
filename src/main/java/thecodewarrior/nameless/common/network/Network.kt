package thecodewarrior.nameless.common.network

import com.teamwizardry.librarianlib.common.network.PacketHandler
import net.minecraftforge.fml.relauncher.Side
import thecodewarrior.nameless.common.network.particle.PacketParticleExtractVitae
import thecodewarrior.nameless.common.network.particle.PacketParticleExtractVitaeFinish

/**
 * Created by TheCodeWarrior
 */
object Network {
    init {
        PacketHandler.register(PacketBeginSpell::class.java, Side.SERVER)
        PacketHandler.register(PacketEndSpell::class.java, Side.SERVER)
        PacketHandler.register(PacketParticleExtractVitae::class.java, Side.CLIENT)
        PacketHandler.register(PacketParticleExtractVitaeFinish::class.java, Side.CLIENT)
    }
}
