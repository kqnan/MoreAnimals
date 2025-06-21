package szuc.kqn.moreanimals.nms.AI

import org.bukkit.Location
import org.bukkit.entity.Sheep
import java.util.function.Function

interface SheepEatAI {
    fun createPathfinderGoal(sheep: Sheep, canUse: Function<Location, Boolean>, setBlock: Function<Location,Unit>) :Any
}