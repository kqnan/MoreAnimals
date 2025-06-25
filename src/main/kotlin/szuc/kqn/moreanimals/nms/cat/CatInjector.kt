package szuc.kqn.moreanimals.nms.cat

import org.bukkit.entity.Cat
import org.bukkit.entity.Cow
import org.bukkit.inventory.ItemStack
import szuc.kqn.moreanimals.nms.cow.CowInjector
import taboolib.module.nms.nmsProxy

interface CatInjector {
    fun inject()
    fun isFood(cat:Any,item: Any?): Boolean
    fun addAdditionalGift(cat: Cat, key:String,item: ItemStack,prob:Int)
    fun writeFoodToNBT(cat: Cat, item: ItemStack)
    companion object {
        val INSTANCE: CatInjector = nmsProxy(CatInjector::class.java, "{name}" + "Impl")
    }
}