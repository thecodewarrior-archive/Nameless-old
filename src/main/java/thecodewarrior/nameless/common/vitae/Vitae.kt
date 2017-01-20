package thecodewarrior.nameless.common.vitae

import com.teamwizardry.librarianlib.common.util.saving.AbstractSaveHandler
import com.teamwizardry.librarianlib.common.util.saving.Save
import com.teamwizardry.librarianlib.common.util.saving.SaveInPlace
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.util.INBTSerializable
import thecodewarrior.nameless.common.spells.SpellInstance
import thecodewarrior.nameless.common.spells.SpellRegistry
import java.util.*

/**
 * Created by TheCodeWarrior
 */
enum class VitaeBranch {
    SYNERGIZER,
    DEFILER
}

enum class VitaeType(val branch: VitaeBranch) {
    SOFT(VitaeBranch.SYNERGIZER),
    SYNERGISTIC(VitaeBranch.SYNERGIZER),
    EPIC(VitaeBranch.SYNERGIZER),
    ASCENDANT(VitaeBranch.SYNERGIZER),
    DIVINE(VitaeBranch.SYNERGIZER),

    JAGGED(VitaeBranch.DEFILER),
    DESTRUCTIVE(VitaeBranch.DEFILER),
    ANNIHILATIVE(VitaeBranch.DEFILER),
    DIABOLIC(VitaeBranch.DEFILER),
    INFERNAL(VitaeBranch.DEFILER)
}
/*
enum class VitaeTypeRequirement(val obj: Any?) {
    SOFT(VitaeType.SOFT),
    SYNERGISTIC(VitaeType.SYNERGISTIC),
    EPIC(VitaeType.EPIC),
    ASCENDANT(VitaeType.ASCENDANT),
    DIVINE(VitaeType.DIVINE),

    JAGGED(VitaeType.JAGGED),
    DESTRUCTIVE(VitaeType.DESTRUCTIVE),
    ANNIHILATIVE(VitaeType.ANNIHILATIVE),
    DIABOLIC(VitaeType.DIABOLIC),
    INFERNAL(VitaeType.INFERNAL),

    ANY_SYNERGIZER(VitaeBranch.SYNERGIZER),
    ANY_DEFILER(VitaeBranch.DEFILER),

    ANY(null);

    val vitaeTypes: Set<VitaeType>
        get() = toType.get(this)!!

    companion object {
        protected val toType = mutableMapOf(
                SOFT to setOf(VitaeType.SOFT),
                SYNERGISTIC to setOf(VitaeType.SYNERGISTIC),
                EPIC to setOf(VitaeType.EPIC),
                ASCENDANT to setOf(VitaeType.ASCENDANT),
                DIVINE to setOf(VitaeType.DIVINE),

                JAGGED to setOf(VitaeType.JAGGED),
                DESTRUCTIVE to setOf(VitaeType.DESTRUCTIVE),
                ANNIHILATIVE to setOf(VitaeType.ANNIHILATIVE),
                DIABOLIC to setOf(VitaeType.DIABOLIC),
                INFERNAL to setOf(VitaeType.INFERNAL),

                ANY_SYNERGIZER to setOf(VitaeType.SOFT, VitaeType.SYNERGISTIC, VitaeType.EPIC, VitaeType.ASCENDANT, VitaeType.DIVINE),
                ANY_DEFILER to setOf(VitaeType.JAGGED, VitaeType.DESTRUCTIVE, VitaeType.ANNIHILATIVE, VitaeType.DIABOLIC, VitaeType.INFERNAL),

                ANY to setOf(*VitaeType.values())
        )
    }
}
*/

@SaveInPlace
class VitaeCap {
    inner class SpellCastData(val instance: SpellInstance, skill: Float, branch: VitaeBranch) {
        var uuid = UUID.randomUUID()

        var unit = instance.spell.getUnitCost(instance, branch, skill)

        var stored = mutableMapOf<VitaeType, Float>()

        var extractors = unit.keys.associate { it to VitaeExtractor.createFor(it, unit[it] ?: 0f) }
    }

    val casting = mutableMapOf<UUID, SpellCastData>() // Data that shouldn't be saved. There is a `NoSync` annotation, but no `NoSave` annotation.
    @Save val tolerance: LinkedHashMap<VitaeType, Float> = mutableMapOf<VitaeType, Float>() as LinkedHashMap<VitaeType, Float>
    var branch = VitaeBranch.DEFILER

    fun tick(player: EntityPlayer) {
        if(player.world.isRemote) return
        casting.forEach { cast ->
            cast.value.extractors.forEach extractorLoop@{
                val unit = cast.value.unit.get(it.key) ?: return@extractorLoop
                val existing = cast.value.stored.get(it.key) ?: 0f
                val extractor = it.value ?: return@extractorLoop
                val extracted = extractor.tryExtract(player, unit)
                cast.value.stored.put(it.key, existing + extracted)
            }
//            player.sendStatusMessage(TextComponentString("- ${cast.value.stored.get(VitaeType.JAGGED)}"))
        }
    }

    fun beginSpell(name: ResourceLocation, player: EntityPlayer) {
        SpellRegistry.get(name)?.createInstance(player)?.let {
            val data = SpellCastData(it, 1f, branch)
            casting.put(data.uuid, data)
        }
    }

    // uuid is unused until I get syncing functional
    fun endSpell(uuid_: UUID, player: EntityPlayer) {
        val first = casting.toList().firstOrNull() ?: return
        val uuid = first.second.uuid
        // ^^ until I get uuid syncing working

        val data = casting.get(uuid) ?: return
        casting.remove(uuid)
        val instance = data.instance

        var units = Float.POSITIVE_INFINITY

        data.unit.forEach {
            units = Math.min(units, (data.stored[it.key] ?: 0f)/it.value)
        }

        player.sendStatusMessage(TextComponentString("pwr: $units"))

        @Suppress("UNCHECKED_CAST")
        instance.spell.fire(instance, units)
    }

    fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        compound.setTag("auto", AbstractSaveHandler.writeAutoNBT(this, false))
        return compound
    }

    fun readFromNBT(compound: NBTTagCompound) {
        AbstractSaveHandler.readAutoNBT(this, compound.getTag("auto"), false)
    }

    companion object {
        init {
            CapabilityManager.INSTANCE.register(VitaeCap::class.java, VitaeCapStorage(), { VitaeCap() });
        }

        @CapabilityInject(VitaeCap::class)
        lateinit var CAPABILITY: Capability<VitaeCap>

    }
}

class VitaeCapStorage : Capability.IStorage<VitaeCap> {
    override fun readNBT(capability: Capability<VitaeCap>, instance: VitaeCap, side: EnumFacing?, nbt: NBTBase) {
        instance.readFromNBT(nbt as NBTTagCompound)
    }

    override fun writeNBT(capability: Capability<VitaeCap>, instance: VitaeCap, side: EnumFacing?): NBTBase {
        return instance.writeToNBT(NBTTagCompound())
    }
}


class VitaeCapProvider : ICapabilityProvider, INBTSerializable<NBTTagCompound> {
    override fun serializeNBT(): NBTTagCompound {
        return cap.writeToNBT(NBTTagCompound())
    }

    override fun deserializeNBT(nbt: NBTTagCompound) {
        cap.readFromNBT(nbt)
    }

    val cap = VitaeCap()

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return capability == VitaeCap.CAPABILITY
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (capability == VitaeCap.CAPABILITY)
            return cap as T
        return null
    }
}
