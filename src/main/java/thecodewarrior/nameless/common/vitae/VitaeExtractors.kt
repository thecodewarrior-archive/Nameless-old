package thecodewarrior.nameless.common.vitae

import com.teamwizardry.librarianlib.common.network.PacketHandler
import com.teamwizardry.librarianlib.common.util.minus
import com.teamwizardry.librarianlib.common.util.plus
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.NetworkRegistry
import thecodewarrior.nameless.common.network.particle.PacketParticleExtractVitae
import thecodewarrior.nameless.common.network.particle.PacketParticleExtractVitaeFinish
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by TheCodeWarrior
 */
abstract class VitaeExtractor(val unit: Float, val type: VitaeType) {

    abstract fun tryExtract(player: EntityPlayer, amount: Float): Float

    companion object {
        val extractors = mapOf(
                VitaeType.JAGGED to ::JaggedExtractor
        )

        fun createFor(type: VitaeType, unit: Float): VitaeExtractor? {
            return extractors[type]?.invoke(unit)
        }
    }
}

class JaggedExtractor(unit: Float) : VitaeExtractor(unit, VitaeType.JAGGED) {

    var pos: BlockPos? = null
    var extracted = 0f
    var stored = 0f
    var radius = Math.pow(unit.toDouble(), 1.0 / 2.0).toInt()

    override fun tryExtract(player: EntityPlayer, amount: Float): Float {
        if (player.world.isRemote) return 0f

        var ret = 0f
        var speedLeft = amount
        do {
            if (pos == null) {
                getNewPos(player)
                if (pos == null)
                    break
            }

            if (pos != null) {

                extracted += speedLeft
                PacketHandler.NETWORK.sendToAllAround(PacketParticleExtractVitae().apply { pos = this@JaggedExtractor.pos!! }, NetworkRegistry.TargetPoint(player.world.provider.dimension, pos!!.x.toDouble(), pos!!.y.toDouble(), pos!!.z.toDouble(), 32.0))
                if (extracted >= stored) {
                    replace(player.world, pos!!)
                    PacketHandler.NETWORK.sendToAllAround(PacketParticleExtractVitaeFinish().apply { pos = this@JaggedExtractor.pos!!; eID = player.entityId}, NetworkRegistry.TargetPoint(player.world.provider.dimension, player.posX, player.posY, player.posZ, 32.0))
                    pos = null
                    speedLeft -= stored
                    ret += stored
                } else
                    break
            }
        } while (pos == null && speedLeft > 0)
        return ret
    }

    fun getNewPos(player: EntityPlayer) {
        val random = ThreadLocalRandom.current()
        val playerPos = player.position
        var blockPos: BlockPos? = null
        var rad = 0
        while (blockPos == null && rad <= radius) {
            blockPos = BlockPos.getAllInBoxMutable(playerPos - BlockPos(rad, rad, rad), playerPos + BlockPos(rad, rad, rad)).find {
                random.nextFloat() < 0.1 && (
                        it.x - playerPos.x == rad || playerPos.x - it.x == rad ||
                        it.y - playerPos.y == rad || playerPos.y - it.y == rad ||
                        it.z - playerPos.z == rad || playerPos.z - it.z == rad
                        ) && getEnergyFrom(player.world, it) > 0
            }?.toImmutable()
            rad++
        }

        if(blockPos == null) {
            pos = null
            stored = 0f
            return
        }

        val blockStored = getEnergyFrom(player.world, blockPos)

        if(blockStored == 0f) {
            pos = null
            stored = 0f
            return
        }

        pos = blockPos
        stored = blockStored
    }

    private data class BlockData(val pos: BlockPos, val distSq: Double, var stored: Float = 0f)

    companion object {
        private var map = mutableMapOf(
                Blocks.GRASS to 3f,
                Blocks.TALLGRASS to 0.1f,
                Blocks.LOG to 7f,
                Blocks.LOG2 to 7f,
                Blocks.LEAVES to 4f,
                Blocks.LEAVES2 to 4f
        )

        private var replacements = mutableMapOf(
                Blocks.GRASS to Blocks.DIRT,
                Blocks.TALLGRASS to Blocks.AIR,
                Blocks.LOG to Blocks.GRAVEL,
                Blocks.LOG2 to Blocks.GRAVEL,
                Blocks.LEAVES to Blocks.AIR,
                Blocks.LEAVES2 to Blocks.AIR
        )

        fun getEnergyFrom(world: World, blockPos: BlockPos): Float {
            val state = world.getBlockState(blockPos)
            val block = state.block
            val mapEntry = map[block]
            return mapEntry ?: 0f
        }

        fun replace(world: World, blockPos: BlockPos) {
            val state = world.getBlockState(blockPos)
            val block = state.block
            val mapEntry = replacements[block] ?: return

            world.setBlockState(blockPos, mapEntry.defaultState)
        }
    }
}
