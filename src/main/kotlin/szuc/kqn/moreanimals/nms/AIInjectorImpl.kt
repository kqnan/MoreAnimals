package szuc.kqn.moreanimals.nms

import net.minecraft.world.entity.EntityInsentient
import net.minecraft.world.entity.ai.goal.PathfinderGoal
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity
import org.bukkit.entity.Chicken
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Sheep
import szuc.kqn.moreanimals.nms.chicken.ChickenTemptAI
import szuc.kqn.moreanimals.nms.sheep.SheepEatAI
import szuc.kqn.moreanimals.nms.sheep.SheepTemptAI
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.nms.nmsProxy
import java.util.function.Function

class AIInjectorImpl : AIInjector {

    override fun SheepeatBlock(sheep: Sheep, canUse:Function<Location,Boolean>, setBlock: Function<Location, Unit>){
        removeGoalAi(sheep,"Eat")
        removeGoalAi(sheep,"SheepEatAIImpl")
        addGoalAi(sheep, nmsProxy(SheepEatAI::class.java,bind="{name}Impl").createPathfinderGoal(sheep,canUse,setBlock) as PathfinderGoal,5)
    }

    override fun SheepTemptItem(sheep: Sheep, item: org.bukkit.inventory.ItemStack) {
        removeGoalAi(sheep,"Tempt")
        removeGoalAi(sheep,"SheepAIInjectorImpl")
        addGoalAi(sheep,nmsProxy(SheepTemptAI::class.java,bind="{name}Impl").createPathfinderGoal(sheep,item) as PathfinderGoal,3)

    }

    override fun ChickenTemptItem(chicken: Chicken, item: org.bukkit.inventory.ItemStack) {
        removeGoalAi(chicken,"Tempt")
        removeGoalAi(chicken,"ChickenTemptAIImpl")
        addGoalAi(chicken,nmsProxy(ChickenTemptAI::class.java,bind="{name}Impl").createPathfinderGoal(chicken,item) as PathfinderGoal,3)

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