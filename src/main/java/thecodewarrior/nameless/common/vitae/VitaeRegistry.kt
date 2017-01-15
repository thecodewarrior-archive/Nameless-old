package thecodewarrior.nameless.common.vitae

import net.minecraft.block.Block
import net.minecraft.entity.Entity

/**
 * Created by TheCodeWarrior
 */
object VitaeRegistry {
//    val tornValues = mutableListOf<TornVitaeHandler>()

}

abstract class TornVitaeHandler<T> {
    /**
     * Extract all the life force out of the specified object. Return the amount extracted
     */
    abstract fun extractLifeForce(obj: T): Float
}

abstract class TornVitaeHandlerEntity : TornVitaeHandler<Entity>()
abstract class TornVitaeHandlerBlock : TornVitaeHandler<Block>()
