package szuc.kqn.moreanimals.nms.cow

import org.bukkit.entity.Chicken
import org.bukkit.entity.Cow
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.nmsProxy

interface CowInjector {
    fun inject()
    fun isFood(cow:Any,item: Any?): Boolean
    fun writeProductionToNBT(cow: Cow, item: ItemStack)
    fun writeFoodToNBT(cow: Cow, item: ItemStack)
    companion object {
        val INSTANCE: CowInjector = nmsProxy(CowInjector::class.java, "{name}" + "Impl")
    }
}
