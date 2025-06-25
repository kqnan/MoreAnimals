package szuc.kqn.moreanimals.nms.cow

import org.bukkit.entity.Chicken
import org.bukkit.entity.Cow
import org.bukkit.inventory.ItemStack

interface CowTemptAI {
    fun createPathfinderGoal(cow:Cow, item: ItemStack) :Any
}