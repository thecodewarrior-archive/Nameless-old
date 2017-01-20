package thecodewarrior.nameless.common.spells

import com.teamwizardry.librarianlib.common.util.saving.Savable
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import thecodewarrior.nameless.common.vitae.VitaeBranch
import thecodewarrior.nameless.common.vitae.VitaeType

/**
 * Represents a spell type, similarly to how there is a single `Block` instance per block type.
 *
 * @param I The class of the instance object, used to store per-cast data
 */
abstract class Spell<I: SpellInstance>(val name: ResourceLocation) {

    init {
        SpellRegistry.register(this)
    }
    /**
     * Creates an instance of the spell
     */
    abstract fun createInstance(player: EntityPlayer): I

    /**
     * Gets the minimum amount of the specified Vitae required for this spell to operate
     */
    abstract fun getUnitCost(i: I, branch: VitaeBranch, skill: Float): Map<VitaeType, Float>

    /**
     * Fires the spell at [units]x power. The amount of vitae charged is roughly [units]*[getUnitCost]
     */
    abstract fun fire(i: I, units: Float)

    /**
     * Cancels the spell discharging roughly [units]*[getUnitCost] vitae
     */
    abstract fun cancel(i: I, units: Float)

    /**
     * Runs each tick while the spell is being charged
     */
    abstract fun tick(i: I)
}

@Savable
open class SpellInstance(val player: EntityPlayer, spell_: Spell<*>) {
    @Suppress("UNCHECKED_CAST")
    val spell: Spell<SpellInstance> = spell_ as Spell<SpellInstance>
}
