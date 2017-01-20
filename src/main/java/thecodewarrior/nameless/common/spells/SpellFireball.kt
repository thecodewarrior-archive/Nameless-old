package thecodewarrior.nameless.common.spells

import com.teamwizardry.librarianlib.common.util.plus
import com.teamwizardry.librarianlib.common.util.times
import com.teamwizardry.librarianlib.common.util.vec
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityLargeFireball
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import thecodewarrior.nameless.NamelessMod
import thecodewarrior.nameless.common.vitae.VitaeBranch
import thecodewarrior.nameless.common.vitae.VitaeType

/**
 * Created by TheCodeWarrior
 */
object SpellFireball : Spell<SpellInstance>(ResourceLocation(NamelessMod.MODID, "fireball")) {
    override fun createInstance(player: EntityPlayer): SpellInstance {
        return SpellInstance(player, this)
    }

    override fun getUnitCost(i: SpellInstance, branch: VitaeBranch, skill: Float): Map<VitaeType, Float> {
        return mapOf(
                VitaeType.JAGGED to 20f
        )
    }

    override fun fire(i: SpellInstance, units: Float) {
        if(i.player.world.isRemote) return
        val look = i.player.lookVec * 0.1
        val eyes = i.player.positionVector + vec(0, i.player.eyeHeight, 0)

        val fireball = EntityLargeFireball(i.player.world, i.player, 0.0, 0.0, 0.0) //look.xCoord, look.yCoord, look.zCoord)
        fireball.accelerationX = look.xCoord
        fireball.accelerationY = look.yCoord
        fireball.accelerationZ = look.zCoord
        fireball.posX = eyes.xCoord
        fireball.posY = eyes.yCoord
        fireball.posZ = eyes.zCoord

        fireball.explosionPower = MathHelper.sqrt(units).toInt()
        i.player.world.spawnEntity(fireball)
    }

    override fun cancel(i: SpellInstance, units: Float) {
    }

    override fun tick(i: SpellInstance) {
    }
}
