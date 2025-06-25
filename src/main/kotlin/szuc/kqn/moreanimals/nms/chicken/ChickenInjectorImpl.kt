package szuc.kqn.moreanimals.nms.chicken


import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.matcher.ElementMatchers.named
import net.minecraft.sounds.SoundEffects
import net.minecraft.world.entity.animal.EntityAnimal
import net.minecraft.world.entity.animal.EntityChicken
import net.minecraft.world.item.Items
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftChicken
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack
import org.bukkit.entity.Chicken
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType.BYTE_ARRAY

import szuc.kqn.moreanimals.nms.ChickenMethodDelegation
import szuc.kqn.moreanimals.nms.ChickenMethodDelegationIsFood


import taboolib.platform.util.deserializeToItemStack
import taboolib.platform.util.serializeToByteArray
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiFunction
import java.util.function.Consumer

class ChickenInjectorImpl : ChickenInjector {
    var init: Boolean = false
    val sharedData= ConcurrentHashMap<Any,Any>()
    /**
     *
     *  修改nms鸡的AIStep方法，每次重启要重新注入
     */
    override fun inject() {
        if (!init) {
            try {
                ByteBuddyAgent.install()
                val b = ByteBuddy()

                b.redefine(EntityChicken::class.java).method(ElementMatchers.named("aiStep")).intercept(
                    Advice.to(ChickenMethodDelegation::class.java)
                ).method(named("isFood")).
                intercept(MethodDelegation.to(ChickenMethodDelegationIsFood::class.java)).make().load(EntityChicken::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent())

                b.redefine(szuc.kqn.moreanimals.nms.ChickenMethodDelegation::class.java).make().load(
                    EntityChicken::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent()
                )
                b.redefine(szuc.kqn.moreanimals.nms.ChickenMethodDelegationIsFood::class.java).make().load(
                    EntityChicken::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent()
                )
                val mw = Class.forName("szuc.kqn.moreanimals.nms.ChickenMethodDelegation", true, EntityChicken::class.java.classLoader)
                mw.getField("aiStep0")[null] = Consumer { t:Any ->aiStep(t)}

                val mw2 = Class.forName("szuc.kqn.moreanimals.nms.ChickenMethodDelegationIsFood", true, EntityChicken::class.java.classLoader)
                mw2.getField("isFood0")[null] = BiFunction { t:Any ,u:Any->isFood(t,u)}


            } catch (e: Exception) {
                e.printStackTrace()
            }
            init = true
        }
    }
    fun  spawnItem(chicken: EntityChicken):Boolean{
        val buukitChick=Bukkit.getEntity(chicken.uuid)
        val item=buukitChick?.persistentDataContainer?.get(NamespacedKey.fromString("moreanimals:product")!!, BYTE_ARRAY)?.deserializeToItemStack(false)
        if(buukitChick==null||item==null)return false
        buukitChick.world.dropItemNaturally(buukitChick.location,item)
        if(debug)println("nms :    $item")
        return true
    }
    /**
     * 在EntityChicken#aiStep进入前执行
     */
    var debug=false;
    fun aiStep(chicken:Any){
        if(chicken !is EntityChicken)return
        if(debug)spawnItem(chicken)
        if (!chicken.level().isClientSide && chicken.isAlive() && !chicken.isBaby() && !chicken.isChickenJockey() && --chicken.eggTime <= 0) {
            if(!spawnItem(chicken))return
            //正常下蛋：
            chicken.playSound(
                SoundEffects.CHICKEN_EGG,
                1.0f,
                (chicken.random.nextFloat() - chicken.random.nextFloat()) * 0.2f + 1.0f
            )

            chicken.eggTime = chicken.random.nextInt(6000) + 6000
        }
    }

    override fun setProduct(chick: Chicken, item: ItemStack) {

    }
    override fun writeTemptFoodToNBT(chick:Chicken, item: org.bukkit.inventory.ItemStack) {
        val item=item.clone()
        item.amount=1
        chick.persistentDataContainer.set(NamespacedKey.fromString("moreanimals:product")!!, BYTE_ARRAY,item.serializeToByteArray(false))

    }
    override fun isFood(chicken:Any, item: Any?): Boolean {
        if (item == null) return false// java.util.Map.entry(false,false)
        if(!((chicken is EntityAnimal)&& (item is net.minecraft.world.item.ItemStack)))return false // java.util.Map.entry(false,false)
        val bukkititem= CraftItemStack.asBukkitCopy(item).clone()
        bukkititem.amount=1
        val itemHash=bukkititem.toString().hashCode()//转换成Bukkit ItemStack再取哈希
        val nbtData=chicken.tags.firstOrNull() { it.startsWith("moreanimals:isFood=") }?.removePrefix("moreanimals:isFood=")?.toIntOrNull()
        if(debug)println(itemHash==nbtData)
        if(debug)println(nbtData==null||nbtData==0)
        if(nbtData==null||nbtData==0){
            //Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD
            return item.`is`(Items.WHEAT_SEEDS)||item.`is`(Items.MELON_SEEDS)||item.`is`(Items.PUMPKIN_SEEDS)||item.`is`(Items.BEETROOT_SEEDS
            )||item.`is`(Items.TORCHFLOWER_SEEDS)||item.`is`(Items.PITCHER_POD)
        }
        return itemHash==nbtData//java.util.Map.entry(false,itemHash==nbtData)
    }

    override fun writeFoodToNBT(chicken: Chicken, item: org.bukkit.inventory.ItemStack) {
        var item=item.clone()
        item.amount=1
        item= CraftItemStack.asBukkitCopy(CraftItemStack.asNMSCopy(item))

        (chicken as CraftChicken).handle.tags.removeIf { it.startsWith("moreanimals:isFood=") }
        (chicken).handle.addTag("moreanimals:isFood="+item.toString().hashCode())
    }

}