package thecodewarrior.nameless.common.network.particle

import com.teamwizardry.librarianlib.client.fx.particle.ParticleBuilder
import com.teamwizardry.librarianlib.client.fx.particle.ParticleSpawner
import com.teamwizardry.librarianlib.client.fx.particle.functions.RenderFunctionBasic
import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.math.interpolate.StaticInterp
import com.teamwizardry.librarianlib.common.util.math.interpolate.position.InterpBezier3D
import com.teamwizardry.librarianlib.common.util.plus
import com.teamwizardry.librarianlib.common.util.saving.Save
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
class PacketParticleExtractVitae : PacketBase() {
    @Save var pos: BlockPos = BlockPos.ORIGIN

    override fun handle(ctx: MessageContext) {
        val builder = ParticleBuilder(20)
        val random = ThreadLocalRandom.current()
        builder.addRandomizationLambda {
            val start = Vec3d(random.nextDouble(2.0)-1, random.nextDouble(2.0)-1, random.nextDouble(2.0)-1).normalize().scale(1.5)
            val end   = Vec3d(random.nextDouble(2.0)-1, random.nextDouble(2.0)-1, random.nextDouble(2.0)-1).normalize().scale(1.5)
            it.setPositionFunction(InterpBezier3D(Vec3d.ZERO, Vec3d.ZERO, start, end))
        }
        builder.setScale(2f)
        builder.setColor(Color(1f, 1f, 1f, 0.3f))
        builder.setRenderFunction(RenderFunctionBasic(ResourceLocation(NamelessMod.MODID, "particles/sparkle"), Particles.subtractive))
        builder.enableRandom()

//        builder.setRender(ResourceLocation("nameless:particles/sparkle"))
//        Minecraft.getMinecraft().renderEngine.bindTexture(ResourceLocation("nameless:particles/sparkle"))

        ParticleSpawner.spawn(builder, Minecraft.getMinecraft().world, StaticInterp(Vec3d(pos) + vec(0.5, 0.5, 0.5)), 10)
    }
}
