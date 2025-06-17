package szuc.kqn.moreanimals.nms

import org.bukkit.Location
import org.bukkit.entity.Sheep
import taboolib.common.util.unsafeLazy
import taboolib.module.nms.nmsProxy
import java.util.function.Function
interface SheepAIInjector {
    //canUse，当羊走到location位置时，是否应该吃到这里的方块
    //setBlock,吃掉这里的方块后，用什么方块替换，返回替换方块的BlockData
    fun eatBlock(sheep: Sheep, canUse:Function<Location,Boolean>, setBlock: Function<Location, Unit>) :Any
    companion object {
        val INSTANCE by unsafeLazy {
            nmsProxy<SheepAIInjector>()
        }
    }
}
/**
 *
 * canEat:羊脚下位置的方块是否能吃
 *
 * setBlock:羊脚下方块被吃后用什么方块替换
 * */
fun Sheep.eatBlock(canEat: Function<Location, Boolean>,setBlock: Function<Location, Unit>){
    SheepAIInjector.INSTANCE.eatBlock(this,canEat,setBlock)
}
