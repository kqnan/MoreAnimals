package szuc.kqn.moreanimals.nms.chicken

import org.bukkit.entity.Chicken
import org.bukkit.inventory.ItemStack

interface ChickenTemptAI {
    fun createPathfinderGoal(chicken: Chicken, item: ItemStack) :Any
}