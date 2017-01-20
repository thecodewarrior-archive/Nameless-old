package thecodewarrior.nameless.common.spells

import net.minecraft.util.ResourceLocation

/**
 * Created by TheCodeWarrior
 */
object SpellRegistry {
    private val map = mutableMapOf<ResourceLocation, Spell<*>>()

    fun get(loc: ResourceLocation): Spell<*>? {
        return map[loc]
    }

    fun register(spell: Spell<*>) {
        map.put(spell.name, spell)
    }

    init {
        SpellFireball
    }
}
