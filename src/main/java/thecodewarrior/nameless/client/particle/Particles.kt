package thecodewarrior.nameless.client.particle

import com.teamwizardry.librarianlib.client.fx.particle.ParticleBuilder
import com.teamwizardry.librarianlib.client.fx.particle.ParticleRenderLayer
import com.teamwizardry.librarianlib.client.fx.particle.ParticleRenderManager
import com.teamwizardry.librarianlib.client.fx.particle.ParticleSpawner
import com.teamwizardry.librarianlib.client.fx.particle.functions.RenderFunctionBasic
import com.teamwizardry.librarianlib.client.util.CustomBlockMapSprites
import com.teamwizardry.librarianlib.common.util.math.interpolate.StaticInterp
import com.teamwizardry.librarianlib.common.util.math.interpolate.float.InterpLinearFloat
import com.teamwizardry.librarianlib.common.util.math.interpolate.position.InterpBezier3D
import com.teamwizardry.librarianlib.common.util.times
import com.teamwizardry.librarianlib.common.util.vec
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.ARBImaging
import org.lwjgl.opengl.GL11
import thecodewarrior.nameless.NamelessMod
import java.awt.Color
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by TheCodeWarrior
 */
object Particles {

    val subtractive: ParticleRenderLayer = object : ParticleRenderLayer("subtractive", true) {
            override fun setup() {
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)

                GlStateManager.pushAttrib()
                GL11.glPushAttrib(GL11.GL_LIGHTING_BIT)
//            GlStateManager.depthMask(false);
                GlStateManager.enableBlend()
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE)
                GlStateManager.glBlendEquation(ARBImaging.GL_FUNC_REVERSE_SUBTRACT)
                GlStateManager.alphaFunc(GL11.GL_GREATER, 1/256f)
                GlStateManager.disableLighting()
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)

                val tessellator = Tessellator.getInstance()
                val vertexbuffer = tessellator.buffer
                vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP)
            }

            override fun teardown() {
                val tessellator = Tessellator.getInstance()
                tessellator.draw()

                GlStateManager.glBlendEquation(ARBImaging.GL_FUNC_ADD)
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F)
                GlStateManager.disableBlend()
//            GlStateManager.depthMask(true);
                GL11.glPopAttrib()
                GlStateManager.popAttrib()
            }

        }

    init {
        ParticleRenderManager.register(subtractive)

        CustomBlockMapSprites.register(ResourceLocation(NamelessMod.MODID, "particles/sparkle"))
    }

    fun spawn(e: EntityPlayer) {
        val builder = ParticleBuilder(20)
        val random = ThreadLocalRandom.current()
        builder.addRandomizationLambda {
            val a = random.nextFloat() * 2 * Math.PI
            val n = vec(Math.sin(a), 0, Math.cos(a))
            it.setPositionFunction(InterpBezier3D(n*random.nextDouble(0.0, 3.0), vec(0,0.75,0), vec(0, 2, 0), n))
        }
        builder.setScaleFunction(InterpLinearFloat(3f, 1f))
        builder.setColor(Color(1f, 1f, 1f, 0.3f))
        builder.setRenderFunction(RenderFunctionBasic(ResourceLocation(NamelessMod.MODID, "particles/sparkle"), subtractive))
        builder.enableRandom()
//        builder.setRender(ResourceLocation("nameless:particles/sparkle"))
//        Minecraft.getMinecraft().renderEngine.bindTexture(ResourceLocation("nameless:particles/sparkle"))

        ParticleSpawner.spawn(builder, e.world, StaticInterp(e.positionVector), 200, 20)
    }
}
