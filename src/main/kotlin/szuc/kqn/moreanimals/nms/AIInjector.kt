package szuc.kqn.moreanimals.nms

import org.bukkit.Location
import org.bukkit.entity.Chicken
import org.bukkit.entity.Cow
import org.bukkit.entity.Sheep
import org.bukkit.inventory.ItemStack
import taboolib.common.util.unsafeLazy
import taboolib.module.nms.nmsProxy
import java.util.function.Function
interface AIInjector {
    //canUse，当羊走到location位置时，是否应该吃到这里的方块
    //setBlock,吃掉这里的方块后，用什么方块替换，返回替换方块的BlockData
    fun SheepeatBlock(sheep: Sheep, canUse:Function<Location,Boolean>, setBlock: Function<Location, Unit>) :Any

    fun SheepTemptItem(sheep:Sheep, item:ItemStack)

    fun ChickenTemptItem(chicken: Chicken,item: ItemStack)
    fun CowTemptItem(cow:Cow,item: ItemStack)
    companion object {
        val INSTANCE by unsafeLazy {
            nmsProxy<AIInjector>()
        }
    }
}
