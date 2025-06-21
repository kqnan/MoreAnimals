package szuc.kqn.moreanimals.nms.AI

import net.minecraft.world.entity.EntityInsentient
import net.minecraft.world.entity.ai.goal.PathfinderGoal
import net.minecraft.world.entity.animal.EntitySheep
import net.minecraft.world.level.World
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_20_R3.block.data.CraftBlockData
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftSheep
import org.bukkit.entity.Sheep
import org.bukkit.persistence.PersistentDataType
import taboolib.platform.util.bukkitPlugin
import taboolib.platform.util.groundBlock
import java.util.*
import java.util.function.Function
import kotlin.math.max
import kotlin.random.Random

class SheepEatAIImpl:PathfinderGoal, SheepEatAI {

    val EAT_ANIMATION_TICKS: Int = 40
    private var canUse: Function<Location, Boolean>? = null
    private var setBlock:Function<Location,Unit>? = null
    private var mob: EntityInsentient? = null
    private var level: World? = null
    private var eatAnimationTick = 0
    private var sheep: Sheep? = null
    constructor():super(){

    }
    constructor (sheep: Sheep,canUse:Function<Location,Boolean>,setBlock:Function<Location,Unit> ):super(){
        this.sheep=sheep
        val var0: EntityInsentient = (sheep as CraftSheep).handle
        this.mob = var0
        this.canUse=canUse
        this.setBlock=setBlock
        this.sheep = sheep
        this.level = var0.level()
        this.flags = EnumSet.of(Type.MOVE, Type.LOOK, Type.JUMP)
    }

    val rand=Random
    override fun canUse(): Boolean {
//        if (rand.nextInt(if(sheep!!.isAdult)1000 else 50 )!=0) {
//            return false
//        } else {
//
//            return canUse!!.apply(sheep!!.groundBlock.location)
//        }
        return canUse!!.apply(sheep!!.groundBlock.location)
    }

    override fun start() {
        this.eatAnimationTick = this.adjustedTickDelay(40)
        level!!.broadcastEntityEvent(this.mob, 10.toByte())
        mob!!.navigation.stop()
    }

    override fun stop() {
        this.eatAnimationTick = 0
    }

    override fun canContinueToUse(): Boolean {
        return this.eatAnimationTick > 0
    }

    fun getEatAnimationTick(): Int {
        return this.eatAnimationTick
    }
    fun ate(){
        //mob!!.gameEvent(GameEvent.EAT)
        if (mob!!.isBaby()) {
            (mob as EntitySheep).ageUp(60)
        }
        if(sheep!!.isSheared){
            val maxfreq=sheep!!.persistentDataContainer.get(NamespacedKey(bukkitPlugin,"woolGrowFrequency"), PersistentDataType.INTEGER)?:0//需要吃
            val cur=sheep!!.persistentDataContainer.get(NamespacedKey(bukkitPlugin,"eatenTimes"), PersistentDataType.INTEGER)?:0//已吃次数
            sheep!!.persistentDataContainer.set(NamespacedKey(bukkitPlugin,"eatenTimes"), PersistentDataType.INTEGER,(cur+1)%maxfreq)
            if(maxfreq<=cur+1){
                sheep!!.isSheared=false
            }
        }
    }
    override fun tick() {
        this.eatAnimationTick = max(0.0, (this.eatAnimationTick - 1).toDouble()).toInt()
        if (this.eatAnimationTick == this.adjustedTickDelay(4)) {
                val var0 = mob!!.blockPosition()
                val var1 = var0.below()
                if (canUse().apply { sheep!!.groundBlock.location }) {
                       // level.levelEvent(2001, var1, Block.getId(Blocks.DIAMOND_ORE.defaultBlockState()))//方块粒子
                        level!!.levelEvent(2001,var1,Block.getId(((sheep!!.groundBlock.blockData as CraftBlockData).state)))
                        this.setBlock!!.apply(sheep!!.groundBlock.location)
                        ate()
                }

        }
    }
    override fun createPathfinderGoal(sheep: Sheep,canUse:Function<Location,Boolean>,setBlock:Function<Location,Unit>): Any {
        return SheepEatAIImpl(sheep, canUse, setBlock)
    }
}