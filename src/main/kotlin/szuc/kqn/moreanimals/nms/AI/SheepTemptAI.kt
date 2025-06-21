package szuc.kqn.moreanimals.nms.AI

import org.bukkit.entity.Sheep
import org.bukkit.inventory.ItemStack

interface SheepTemptAI {
    fun createPathfinderGoal(sheep: Sheep,item:ItemStack) :Any
}