package thecodewarrior.nameless.client

import com.teamwizardry.librarianlib.common.util.saving.Save
import com.teamwizardry.librarianlib.common.util.saving.SaveInPlace
import thecodewarrior.nameless.common.vitae.VitaeType
import java.util.*

/**
 * Created by TheCodeWarrior
 */
@SaveInPlace
class VitaeClientData {
    @Save
    var castings: ArrayList<VitaeClientCastingData> = arrayListOf()

    companion object {
        val CLIENT_INSTANCE = VitaeClientData()
    }
}

data class VitaeClientCastingData(var unit: HashMap<VitaeType, Float>, var units: Float)
