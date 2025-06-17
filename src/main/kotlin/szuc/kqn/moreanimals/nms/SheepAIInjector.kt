package szuc.kqn.moreanimals.nms

import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Sheep
import org.bukkit.metadata.MetadataValue
import org.bukkit.persistence.PersistentDataType
import taboolib.common.util.unsafeLazy
import taboolib.module.nms.nmsProxy
import taboolib.platform.util.bukkitPlugin
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
 * 原版的羊会自己吃草方块，这个函数修改了羊会吃的方块。不可持久化，每次重启都要注入一次
 * canEat:羊脚下位置的方块是否能吃
 *
 * setBlock:羊脚下方块被吃后用什么方块替换
 * */
fun Sheep.eatBlock(canEat: Function<Location, Boolean>,setBlock: Function<Location, Unit>){
    SheepAIInjector.INSTANCE.eatBlock(this,canEat,setBlock)
}
/**
 * 原版的羊吃一次方块就会长毛，这个函数修改了这个次数。使用可持久化容器存储
 * freq:吃几次方块才长毛
 *
 *
 */
fun Sheep.woolGrowFrequency(freq:Int){
    this.persistentDataContainer.set(NamespacedKey(bukkitPlugin,"woolGrowFrequency"), PersistentDataType.INTEGER,freq)//需要吃
    this.persistentDataContainer.set(NamespacedKey(bukkitPlugin,"eatenTimes"), PersistentDataType.INTEGER,0)//已吃次数
    
}