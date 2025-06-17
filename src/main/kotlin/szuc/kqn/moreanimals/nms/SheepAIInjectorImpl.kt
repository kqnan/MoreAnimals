package szuc.kqn.moreanimals.nms

import net.minecraft.world.entity.EntityInsentient
import net.minecraft.world.entity.ai.control.ControllerJump
import net.minecraft.world.entity.ai.goal.PathfinderGoal
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector
import net.minecraft.world.entity.ai.navigation.NavigationAbstract
import net.minecraft.world.level.pathfinder.PathEntity
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Sheep
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.ai.PathfinderCreator
import taboolib.module.ai.PathfinderExecutor
import taboolib.module.ai.SimpleAi
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsProxy
import java.lang.reflect.Field
import java.util.function.Function
class SheepAIInjectorImpl :SheepAIInjector{
    override fun eatBlock(sheep: Sheep, canUse:Function<Location,Boolean>, setBlock: Function<Location, Unit>){
        removeGoalAi(sheep,"PathfinderGoalEatTile")
        addGoalAi(sheep, nmsProxy(SheepEatAI::class.java,bind="{name}Impl").createPathfinderGoal(sheep,canUse,setBlock) as PathfinderGoal,5)
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