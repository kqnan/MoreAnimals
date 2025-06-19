package szuc.kqn.moreanimals.nms

import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.named
import net.minecraft.world.entity.EntityInsentient
import net.minecraft.world.entity.ai.goal.PathfinderGoal
import net.minecraft.world.entity.animal.EntityAnimal
import net.minecraft.world.entity.animal.EntitySheep
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Sheep
import taboolib.common.platform.function.info
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.nms.nmsProxy
import java.util.function.Function

class SheepAIInjectorImpl :SheepAIInjector{

    override fun eatBlock(sheep: Sheep, canUse:Function<Location,Boolean>, setBlock: Function<Location, Unit>){
        removeGoalAi(sheep,"PathfinderGoalEatTile")
        addGoalAi(sheep, nmsProxy(SheepEatAI::class.java,bind="{name}Impl").createPathfinderGoal(sheep,canUse,setBlock) as PathfinderGoal,5)
    }


    fun isFood(item:Any):Boolean {

        val var0=(item as ItemStack)
        return var0.`is`(Items.DIAMOND)
    }

    fun getEntityInsentient(entity: LivingEntity): Any {
        return (entity as CraftEntity).handle
    }

    fun addGoalAi(entity: LivingEntity, ai: PathfinderGoal, priority: Int) {
        (getEntityInsentient(entity) as EntityInsentient).goalSelector.addGoal(priority, ai)
    }

     fun removeGoalAi(entity: LivingEntity, name: String) {
        removeGoal(name, (getEntityInsentient(entity) as EntityInsentient).goalSelector)
    }
    private fun removeGoal(name: String, targetSelector: Any) {
        val collection = getGoal(targetSelector)
        collection.toList().forEach {
            val a = it!!.getProperty<Any>("goal", remap = true)!!
            if (a.javaClass.name.contains(name)) {

                if (collection is MutableList) {

                    collection.remove(it)
                } else if (collection is MutableSet) {

                    collection.remove(it)
                }
            }
            if (a.javaClass.simpleName == "PathfinderCreatorImpl17" && a.getProperty<Any>("simpleAI")!!.javaClass.name.contains(name)) {

                if (collection is MutableList) {

                    collection.remove(it)
                } else if (collection is MutableSet) {

                    collection.remove(it)
                }
            }
        }
    }
    private fun getGoal(targetSelector: Any): Collection<*> {
        return targetSelector.getProperty<Set<*>>("availableGoals", remap = true)!!
    }



}