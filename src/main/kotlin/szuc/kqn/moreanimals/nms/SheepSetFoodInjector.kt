package szuc.kqn.moreanimals.nms

import org.bukkit.entity.Sheep
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.nmsProxy

interface SheepSetFoodInjector {
    fun inject()
    fun isFood(sheep:Any,item: Any?): Boolean
    fun writeFoodToNBT(sheep: Sheep, item:ItemStack)
    companion object {
        val INSTANCE: SheepSetFoodInjector = nmsProxy(SheepSetFoodInjector::class.java, "{name}" + "Impl")
    }
}
