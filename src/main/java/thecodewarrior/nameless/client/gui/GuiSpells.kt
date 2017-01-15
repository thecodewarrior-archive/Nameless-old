package thecodewarrior.nameless.client.gui

import com.teamwizardry.librarianlib.client.gui.GuiBase
import com.teamwizardry.librarianlib.client.gui.components.ComponentSprite
import com.teamwizardry.librarianlib.client.sprite.Sprite
import net.minecraft.util.ResourceLocation
import thecodewarrior.nameless.NamelessMod

/**
 * Created by TheCodeWarrior
 */
class GuiSpells : GuiBase(0, 0) {
    init {

        val sprite = Sprite(ResourceLocation(NamelessMod.MODID, "textures/gui/spells/fireball.png"))

        mainComponents.add(ComponentSprite(sprite, 0, 0))

    }
}
