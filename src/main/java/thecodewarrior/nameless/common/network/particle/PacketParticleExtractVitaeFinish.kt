package thecodewarrior.nameless.common.network.particle

import com.teamwizardry.librarianlib.client.fx.particle.ParticleBase
import com.teamwizardry.librarianlib.client.fx.particle.ParticleBuilder
import com.teamwizardry.librarianlib.client.fx.particle.ParticleSpawner
import com.teamwizardry.librarianlib.client.fx.particle.functions.RenderFunctionBasic
import com.teamwizardry.librarianlib.client.fx.particle.functions.TickFunction
import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.math.interpolate.InterpFunction
import com.teamwizardry.librarianlib.common.util.minus
import com.teamwizardry.librarianlib.common.util.plus
import com.teamwizardry.librarianlib.common.util.saving.Save
import com.teamwizardry.librarianlib.common.util.times
import com.teamwizardry.librarianlib.common.util.vec
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import thecodewarrior.nameless.NamelessMod
import thecodewarrior.nameless.client.particle.Particles
import java.awt.Color
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by TheCodeWarrior
 */
class PacketParticleExtractVitaeFinish : PacketBase() {
    @Save var eID: Int = 0
    @Save var pos: BlockPos = BlockPos.ORIGIN

    override fun handle(ctx: MessageContext) {
        val entity = Minecraft.getMinecraft().world.getEntityByID(eID) ?: return
        val builder = ParticleBuilder(20*5)
        val random = ThreadLocalRandom.current()

        val particlePos = Vec3d(pos) + vec(0.5, 0.5, 0.5)
        val particleMotion = vec(0, random.nextDouble(2.0), 0) * if(particlePos.yCoord > entity.positionVector.yCoord + entity.eyeHeight/2) -1 else 1
        builder.addRandomizationLambda {
            builder.setMotion(particleMotion + vec(random.nextDouble(0.1), random.nextDouble(0.1), random.nextDouble(0.1)))
        }

        builder.tickFunction = object : TickFunction {
            override fun tick(particle: ParticleBase) {
                val relPos = (entity.positionVector + vec(0, entity.eyeHeight/2, 0)) - particle.pos
                val len = relPos.lengthVector()
                if(len*len < particle.velocity.lengthSquared()) {
                    particle.setExpired()
                }

                particle.velocity = relPos.scale(Math.max(0.5/len, len/30))

            }

        }
        builder.setScale(2f)
        builder.setColor(Color(1f, 1f, 1f, 0.3f))
        builder.setRenderFunction(RenderFunctionBasic(ResourceLocation(NamelessMod.MODID, "particles/sparkle"), Particles.subtractive))
        builder.enableRandom()

//        builder.setRender(ResourceLocation("nameless:particles/sparkle"))
//        Minecraft.getMinecraft().renderEngine.bindTexture(ResourceLocation("nameless:particles/sparkle"))

        ParticleSpawner.spawn(builder, Minecraft.getMinecraft().world, object : InterpFunction<Vec3d> {
            override fun get(i: Float): Vec3d {
                return particlePos + vec(random.nextDouble(1.0)-0.5, random.nextDouble(1.0)-0.5, random.nextDouble(1.0)-0.5)
            }
        }, 10)
    }
}
