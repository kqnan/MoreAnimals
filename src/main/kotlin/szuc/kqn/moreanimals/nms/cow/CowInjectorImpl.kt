package szuc.kqn.moreanimals.nms.cow

import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.named
import net.minecraft.sounds.SoundEffects
import net.minecraft.world.EnumHand
import net.minecraft.world.EnumInteractionResult
import net.minecraft.world.entity.animal.EntityAnimal
import net.minecraft.world.entity.animal.EntityChicken
import net.minecraft.world.entity.animal.EntityCow
import net.minecraft.world.entity.player.EntityHuman
import net.minecraft.world.item.Items
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftChicken
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftCow
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack
import org.bukkit.entity.Chicken
import org.bukkit.entity.Cow
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType.BYTE_ARRAY
import szuc.kqn.moreanimals.nms.CowMethodDelegationIsFood
import taboolib.platform.util.deserializeToItemStack
import taboolib.platform.util.serializeToByteArray
import java.util.function.BiFunction
import java.util.function.Function

class CowInjectorImpl: CowInjector {
    fun checkCooldown(cow:Cow):Boolean{
        return true
    }
    val field=EnumInteractionResult::class.java.getDeclaredField("SUCCESS")
    //利用Advice.OnMethodEnter注入到原方法进入时,返回值代表是否跳过原方法
    fun setProduct(cow: EntityCow, var0: EntityHuman, var1: EnumHand):Any?{
        val bukkitCow=Bukkit.getEntity(cow.uuid) as Cow
     //   println(Bukkit.getPlayer(var0.uuid))

        val bukkitPlayer=Bukkit.getPlayer(var0.uuid)?:return null
       // println(bukkitCow.persistentDataContainer.get(NamespacedKey.fromString("moreanimals:product")!!, BYTE_ARRAY)?.deserializeToItemStack(false))
        val item= bukkitCow.persistentDataContainer.get(NamespacedKey.fromString("moreanimals:product")!!, BYTE_ARRAY)?.deserializeToItemStack(false)?:return null
       // println(item)
        //println(bukkitCow.isAdult&&var0.getItemInHand(var1).`is`(Items.BUCKET))
        if(bukkitCow.isAdult&&var0.getItemInHand(var1).`is`(Items.BUCKET)){
            if(checkCooldown(bukkitCow)){
                var0.playSound(SoundEffects.COW_MILK, 1.0f, 1.0f)
                var0.getItemInHand(var1).shrink(1)
                bukkitPlayer.inventory.addItem(item)
                return field.get(null)
            }
        }
        return null
    }
    var init=false
    override fun inject() {
        if (!init) {
            try {
                ByteBuddyAgent.install()
                val b = ByteBuddy()
                b.redefine(EntityCow::class.java).visit(Advice.to(szuc.kqn.moreanimals.nms.CowMethodDelegation::class.java).on(named("mobInteract"))).
                method(named("isFood")).
                intercept(MethodDelegation.to(CowMethodDelegationIsFood::class.java)).
                make().load(EntityCow::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent())

                b.redefine(szuc.kqn.moreanimals.nms.CowMethodDelegation::class.java).make().load(
                    EntityCow::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent()
                )
                b.redefine(szuc.kqn.moreanimals.nms.CowMethodDelegationIsFood::class.java).make().load(
                    EntityCow::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent()
                )
                val mw = Class.forName("szuc.kqn.moreanimals.nms.CowMethodDelegation", true, EntityCow::class.java.classLoader)
                mw.getField("mobInteract0")[null] =  BiFunction<Any,Any, Function<Any, Any?>>  { t:Any, u:Any ->
                   return@BiFunction java.util.function.Function{v:Any->
                       setProduct(t as EntityCow,u as EntityHuman,v as EnumHand)
                   }
                }
                val mw2 = Class.forName("szuc.kqn.moreanimals.nms.CowMethodDelegationIsFood", true, EntityCow::class.java.classLoader)
                mw2.getField("isFood0")[null] = BiFunction { t:Any ,u:Any->isFood(t,u)}

            } catch (e: Exception) {
                e.printStackTrace()
            }
            init = true
        }
    }
    var debug=false
    override fun isFood(cow: Any, item: Any?): Boolean {
        if (item == null) return false// java.util.Map.entry(false,false)
        if(!((cow is EntityAnimal)&& (item is net.minecraft.world.item.ItemStack)))return false // java.util.Map.entry(false,false)
        val bukkititem= CraftItemStack.asBukkitCopy(item).clone()
        bukkititem.amount=1
        val itemHash=bukkititem.toString().hashCode()//转换成Bukkit ItemStack再取哈希
        val nbtData=cow.tags.firstOrNull() { it.startsWith("moreanimals:isFood=") }?.removePrefix("moreanimals:isFood=")?.toIntOrNull()
        if(debug)println(itemHash==nbtData)
        if(debug)println(nbtData==null||nbtData==0)
        if(nbtData==null||nbtData==0){
            //Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD
            return item.`is`(Items.WHEAT)
        }
        return itemHash==nbtData//java.util.Map.entry(false,itemHash==nbtData)
    }
    override fun writeFoodToNBT(cow: Cow, item: org.bukkit.inventory.ItemStack) {
        var item=item.clone()
        item.amount=1
        item= CraftItemStack.asBukkitCopy(CraftItemStack.asNMSCopy(item))

        (cow as CraftCow).handle.tags.removeIf { it.startsWith("moreanimals:isFood=") }
        (cow).handle.addTag("moreanimals:isFood="+item.toString().hashCode())
    }
    override fun writeProductionToNBT(cow: Cow, item: ItemStack) {
        val item=item.clone()
        item.amount=1
        cow.persistentDataContainer.set(NamespacedKey.fromString("moreanimals:product")!!, BYTE_ARRAY,item.serializeToByteArray(false))
    }
}