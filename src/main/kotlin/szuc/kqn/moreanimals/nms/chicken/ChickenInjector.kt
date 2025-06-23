package szuc.kqn.moreanimals.nms.chicken

import org.bukkit.entity.Chicken
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.nmsProxy

interface ChickenInjector {
    fun inject()
    fun setProduct(chick:Chicken,item: ItemStack)
    fun writeTemptFoodToNBT(sheep: Chicken, item: org.bukkit.inventory.ItemStack)
    fun isFood(chicken:Any,item: Any?):Boolean
    fun writeFoodToNBT(chicken:Chicken, item: ItemStack)
    companion object {

        val INSTANCE: ChickenInjector = nmsProxy(ChickenInjector::class.java, "{name}" + "Impl")
    }
}