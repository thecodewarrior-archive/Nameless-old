package thecodewarrior.nameless.client.gui

import com.teamwizardry.librarianlib.client.gui.GuiComponent
import com.teamwizardry.librarianlib.client.gui.GuiOverlay
import com.teamwizardry.librarianlib.client.gui.components.ComponentSprite
import com.teamwizardry.librarianlib.client.sprite.Texture
import net.minecraft.util.ResourceLocation
import thecodewarrior.nameless.NamelessMod
import java.util.function.BooleanSupplier
import java.util.function.Consumer

/**
 * Created by TheCodeWarrior
 */
object CastingOverlay {
    init {
        GuiOverlay.getOverlayComponent(BooleanSupplier {
            true //VitaeClientData.CLIENT_INSTANCE.castings.size > 0
        }, Consumer { rebuild(it) })
    }
    val texture = Texture(ResourceLocation(NamelessMod.MODID, "textures/gui/casting.png"))
    val glass = texture.getSprite("glass_back", 73, 11)
    val glass_front = texture.getSprite("glass_front", 73, 11)
    val filling = texture.getSprite("filling", 65, 11)
    val sideways_bar = texture.getSprite("sideways_bar", 128, 2)
    val ring = texture.getSprite("ring", 2, 13)

    fun rebuild(main: GuiComponent<*>) {
        main.add(ComponentSprite(glass, 10, 10))
    }
}
