package thecodewarrior.nameless.common.network

import com.teamwizardry.librarianlib.common.util.forCap
import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraft.entity.player.EntityPlayer
import thecodewarrior.nameless.common.vitae.VitaeCap
import java.util.*

/**
 * Created by TheCodeWarrior
 */
class PacketEndSpell : PacketHandleBothSides() {
    @Save var uuid = UUID(0,0)

    override fun handle(player: EntityPlayer) {
        player.forCap(VitaeCap.CAPABILITY, null) { cap ->
            cap.endSpell(uuid, player)
        }
    }
}
