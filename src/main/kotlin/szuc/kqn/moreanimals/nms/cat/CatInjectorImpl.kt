package szuc.kqn.moreanimals.nms.cat

import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.named
import net.minecraft.world.EnumHand
import net.minecraft.world.entity.animal.EntityCat
import net.minecraft.world.entity.animal.EntityCow
import net.minecraft.world.entity.player.EntityHuman
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftCat
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack
import org.bukkit.entity.Cat
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import szuc.kqn.moreanimals.nms.*
import taboolib.common.platform.function.info
import taboolib.common5.util.printed
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.ai.PathfinderExecutor
import taboolib.module.ai.PathfinderExecutorImpl
import taboolib.module.ai.PathfinderExecutorImpl17
import taboolib.module.nms.nmsProxy
import taboolib.platform.util.isAir
import taboolib.platform.util.kill
import java.beans.MethodDescriptor
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function
import java.util.function.BiFunction
import java.util.function.Consumer
import kotlin.random.Random

class CatInjectorImpl :CatInjector{
    fun setMorningGift(cat:Cat,item:ItemStack){

    }
    var init=false
    override fun inject() {
        if (!init) {
            try {
                info("自动注入EntityCat#isFood")

                val world=Bukkit.getWorlds().get(0)
                val bukkitCat=world.spawnEntity(Location(world,0.0,0.0,0.0),EntityType.CAT) as CraftCat

                val AIs=bukkitCat.handle.goalSelector.getProperty<Set<*>>("availableGoals", remap = true)!!
                var CatGiftAI:Class<*>?=null
                AIs?. toList()?.forEach {
                    val a = it!!.getProperty<Any>("goal", remap = true)!!
                    if (it!!.getProperty<Int>("priority") == 3) run {
                        CatGiftAI=a.javaClass
                    }
                }

                bukkitCat.kill()
                ByteBuddyAgent.install()
                val b = ByteBuddy()

                if(CatGiftAI==null){
                    info("自动注入失败EntityCat#giveMorningGift")
                }else {
                    info("自动注入EntityCat#giveMorningGift")
                    b.redefine(CatGiftAI).visit(Advice.to(CatMethodDelegation::class.java).on(named("giveMorningGift"))).
                    make().load(CatGiftAI!!.classLoader, ClassReloadingStrategy.fromInstalledAgent())
                }

                b.redefine(EntityCat::class.java).method(named("isFood")).
                intercept(MethodDelegation.to(CatMethodDelegationIsFood::class.java)).
                make().load(EntityCat::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent())

                b.redefine(szuc.kqn.moreanimals.nms.CatMethodDelegation::class.java).make().load(
                    EntityCat::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent()
                )
                b.redefine(szuc.kqn.moreanimals.nms.CatMethodDelegationIsFood::class.java).make().load(
                    EntityCat::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent()
                )

                val mw = Class.forName("szuc.kqn.moreanimals.nms.CatMethodDelegation", true, EntityCat::class.java.classLoader)
                mw.getField("giveMorningGift0")[null] =  Consumer<Any>{t:Any->giveMorningGift(t)}
                val mw2 = Class.forName("szuc.kqn.moreanimals.nms.CatMethodDelegationIsFood", true, EntityCat::class.java.classLoader)
                mw2.getField("isFood0")[null] = BiFunction { t:Any ,u:Any->isFood(t,u)}

            } catch (e: Exception) {
                e.printStackTrace()
            }
            init = true
        }
    }
    val random= Random(1242456346)
    val morningGift=ConcurrentHashMap<UUID,ConcurrentHashMap<String,Pair<ItemStack,Int>> >()
    fun giveMorningGift(catAI:Any){
        println("enter")

        val cat=catAI.getProperty<EntityCat>("cat", remap = true)
        if(cat !is EntityCat)return
        println("1")
        val gifts=morningGift.get(cat.uuid)?:return
        println("2")
        val bukkitEntity=Bukkit.getEntity(cat.uuid)?:return
        println("3")
        val loc=bukkitEntity.location
        println(gifts)
        for (gift in gifts) {
            println("asdasdxxxxxxx")
            if(random.nextInt(100)<gift.value.second){
                println("asdasd")
                loc.world?.dropItemNaturally(loc,gift.value.first.clone())
            }
        }
    }
    val isFood=ConcurrentHashMap<UUID,ItemStack>()
    override fun isFood(cat: Any, item: Any?): Boolean {
        val bitem=CraftItemStack.asBukkitCopy((item as net.minecraft.world.item.ItemStack))
        if(isFood.containsKey((cat as EntityCat).uuid)){
            val res=isFood.getOrDefault(cat.uuid,null)
            if (res==null|| res.isAir())isFood.remove(cat.uuid)
            return bitem.isSimilar(res)
        }else {
            return bitem.type == Material.COD || bitem.type==Material.SALMON
        }

    }
    override fun addAdditionalGift(cat: Cat, key:String,item: ItemStack,prob:Int) {
        morningGift.getOrPut(cat.uniqueId){ConcurrentHashMap<String,Pair<ItemStack,Int>>()}[key] = Pair(item.clone(),prob)
    }
    override fun writeFoodToNBT(cat: Cat, item: ItemStack) {
        isFood.set(cat.uniqueId,item.clone())
    }
}