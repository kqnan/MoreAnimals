package szuc.kqn.moreanimals.nms.sheep

import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers
import net.minecraft.world.entity.animal.EntityAnimal
import net.minecraft.world.entity.animal.EntitySheep
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftSheep
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack
import org.bukkit.entity.Sheep
import java.util.function.BiFunction


class SheepSetFoodInjectorImpl : SheepSetFoodInjector {
    var init: Boolean = false
    /**
     *
     *  修改nms羊的isFood方法，每次重启要重新注入
     */
    override fun inject() {
        if (!init) {
            try {
                ByteBuddyAgent.install()
                val b = ByteBuddy()
                b.redefine(EntitySheep::class.java).method(ElementMatchers.named("isFood")).intercept(MethodDelegation.to(
                    szuc.kqn.moreanimals.nms.MethodDelegation::class.java)
                ).make().load(EntitySheep::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent())

                b.redefine(szuc.kqn.moreanimals.nms.MethodDelegation::class.java).make().load(
                    EntitySheep::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent()
                )
                val mw = Class.forName("szuc.kqn.moreanimals.nms.MethodDelegation", true, EntitySheep::class.java.classLoader)
                mw.getField("isFood0")[null] = BiFunction { t:Any, u:Any ->isFood(t,u)}
            } catch (e: Exception) {
                e.printStackTrace()
            }
            init = true
        }
    }


    override fun isFood(sheep:Any,item: Any?): Boolean {
        if (item == null) return false
        if(!((sheep is EntityAnimal )&& (item is ItemStack)))return false
        val bukkititem=CraftItemStack.asBukkitCopy(item).clone()
        bukkititem.amount=1
        val itemHash=bukkititem.toString().hashCode()//转换成Bukkit ItemStack再取哈希
        val nbtData=sheep.tags.firstOrNull() { it.startsWith("moreanimals:isFood=") }?.removePrefix("moreanimals:isFood=")?.toIntOrNull()
        if(nbtData==null||nbtData==0){
            return item.`is`(Items.WHEAT)
        }
        return itemHash==nbtData
    }

    override fun writeFoodToNBT(sheep: Sheep, item: org.bukkit.inventory.ItemStack) {
        var item=item.clone()
        item.amount=1
        item=CraftItemStack.asBukkitCopy(CraftItemStack.asNMSCopy(item))

        (sheep as CraftSheep).handle.tags.removeIf { it.startsWith("moreanimals:isFood=") }
        (sheep).handle.addTag("moreanimals:isFood="+item.toString().hashCode())
    }
}

