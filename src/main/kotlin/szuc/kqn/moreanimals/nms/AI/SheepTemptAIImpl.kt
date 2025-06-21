package szuc.kqn.moreanimals.nms.AI

import net.minecraft.world.entity.EntityCreature
import net.minecraft.world.entity.EntityLiving
import net.minecraft.world.entity.ai.goal.PathfinderGoal
import net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition
import net.minecraft.world.entity.player.EntityHuman
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftSheep
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.entity.Sheep
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.abs


class SheepTemptAIImpl: PathfinderGoal, SheepTemptAI {

    private var px = 0.0
    private var py = 0.0
    private var pz = 0.0
    private var pRotX = 0.0
    private var pRotY = 0.0
    private lateinit var  mob:EntityCreature
    private var speedModifier: Double=1.1
    private lateinit var items: ItemStack
    private var canScare: Boolean = false
    protected var player: EntityHuman? = null

    private var calmDown = 0
    var isRunning: Boolean = false
    private lateinit var sheep:Sheep
    constructor( sheep: Sheep,mob: EntityCreature,speedModifier: Double, items: ItemStack, canScare: Boolean):super(){
        this.mob = mob
        this.speedModifier =speedModifier
        this.items = items
        this.canScare = canScare
        this.flags = EnumSet.of(Type.MOVE, Type.LOOK)
        this.sheep=sheep

    }
    constructor() :super(){
    }
    override fun canUse(): Boolean {
        if (this.calmDown > 0) {
            --this.calmDown
            return false
        } else {
            var bukkitPlayer:Player?=null
            for (nearbyEntity in sheep.getNearbyEntities(5.0, 5.0, 5.0)) {
                if(nearbyEntity is Player&&shouldFollow(nearbyEntity)){
                    bukkitPlayer=nearbyEntity
                    break
                }
            }
            if(bukkitPlayer!=null){
                this.player=(bukkitPlayer as CraftPlayer).handle
                return true
            }else return false

        }
    }

    private fun shouldFollow(var0: Player): Boolean {
      //  println(this.items)
      //  println(this.items.isSimilar(var0.inventory.itemInMainHand)||this.items.isSimilar(var0.inventory.itemInOffHand))
        return this.items.isSimilar(var0.inventory.itemInMainHand)||this.items.isSimilar(var0.inventory.itemInOffHand)
    }

    override fun canContinueToUse(): Boolean {
        if (this.canScare()) {
            if (mob.distanceToSqr(this.player) < 36.0) {
                if (player!!.distanceToSqr(this.px, this.py, this.pz) > 0.010000000000000002) {
                    return false
                }

                if (abs(player!!.xRot.toDouble() - this.pRotX) > 5.0 || abs(
                        player!!.yRot.toDouble() - this.pRotY
                    ) > 5.0
                ) {
                    return false
                }
            } else {
                this.px = player!!.x
                this.py = player!!.y
                this.pz = player!!.z
            }

            this.pRotX = player!!.xRot.toDouble()
            this.pRotY = player!!.yRot.toDouble()
        }

        return this.canUse()
    }

    protected fun canScare(): Boolean {
        return this.canScare
    }

    override fun start() {
        this.px = player!!.x
        this.py = player!!.y
        this.pz = player!!.z
        this.isRunning = true
    }

    override fun stop() {
        this.player = null
        mob.navigation.stop()
        this.calmDown = reducedTickDelay(100)
        this.isRunning = false
    }

    override fun tick() {
        mob.lookControl.setLookAt(
            this.player, (mob.maxHeadYRot + 20).toFloat(),
            mob.maxHeadXRot.toFloat()
        )
        if (mob.distanceToSqr(this.player) < 6.25) {
            mob.navigation.stop()
        } else {
            mob.navigation.moveTo(this.player, this.speedModifier)
        }
    }

    override fun createPathfinderGoal(sheep: Sheep, item: ItemStack): Any {
        return SheepTemptAIImpl(sheep,(sheep as CraftSheep).handle, 1.1, item, false)
    }


}
