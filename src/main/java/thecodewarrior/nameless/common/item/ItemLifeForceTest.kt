package thecodewarrior.nameless.common.item

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Created by TheCodeWarrior
 */
class ItemLifeForceTest : ItemMod("lifeForceTest") {
    override fun onItemUse(stack: ItemStack, playerIn: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {


        return EnumActionResult.SUCCESS
    }
}
