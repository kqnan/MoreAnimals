package szuc.kqn.moreanimals.nms

import org.bukkit.Location
import org.bukkit.entity.Sheep
import java.util.function.Function

interface SheepTemptAI {
    fun createPathfinderGoal(sheep: Sheep) :Any
}