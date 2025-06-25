package szuc.kqn.moreanimals

import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import szuc.kqn.moreanimals.nms.*


import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.module.nms.itemTagReader
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem
import taboolib.platform.util.giveItem
import java.util.UUID

@CommandHeader("moreanimals", ["ma"],permission = "moreanimals.commands")
object Command {

    @CommandBody
    val SheepwoolGrowFrequency= subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender
                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Sheep){
                        (nearbyEntity as Sheep).woolGrowFrequency(2)
                    }
                }
            }
        }
    }
    @CommandBody
    val SheepeatBlock=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender
                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Sheep){
                        val main=sender.inventory.itemInMainHand.clone()
                        val off=sender.inventory.itemInOffHand.clone()
                        (nearbyEntity as Sheep).eatBlock(
                            canEat = { loc->
                                loc.block.type==main.type
                            },
                            setBlock ={
                                loc->
                                loc.block.type = off.type
                                loc.block.blockData
                            },
                        )

                    }
                }
            }
        }
    }
    @CommandBody
    val SheepinjectSetFood=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender

                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Sheep){
                        injectSheep()
                    }
                }

            }
        }
    }

    @CommandBody
    val SheepsetFood=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender

                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Sheep){

                        p.inventory.itemInMainHand.let { nearbyEntity.setFood(it) }
                    }
                }

            }
        }
    }
    @CommandBody
    val SheepsetTemptItem=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender

                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Sheep){

                        p.inventory.itemInMainHand.let { nearbyEntity.setTemptItem(item = it
                        ) }
                    }
                }

            }
        }
    }
    @CommandBody
    val chickenSetProduct=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender

                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Chicken){

                        p.inventory.itemInMainHand.let { nearbyEntity.setProduction(item = it
                        )

                        }
                    }
                }

            }
        }
    }
    @CommandBody
    val chickenTemptItem=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender

                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Chicken){
                        nearbyEntity.setTemptItem(p.inventory.itemInMainHand)
                    }
                }

            }
        }
    }
    @CommandBody
    val chickenSetFood=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender

                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Chicken){
                        nearbyEntity.setFood(p.inventory.itemInMainHand)
                    }
                }

            }
        }
    }
    @CommandBody
    val cowSetProduction=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender

                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Cow){
                        nearbyEntity.setProduction(p.inventory.itemInMainHand)
                    }
                }

            }
        }
    }
    @CommandBody
    val cowSetTemptFood=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender

                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Cow){
                        nearbyEntity.setTemptItem(p.inventory.itemInMainHand)
                    }
                }

            }
        }
    }
    @CommandBody
    val cowSetFood=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender

                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Cow){
                        nearbyEntity.setFood(p.inventory.itemInMainHand)
                    }
                }

            }
        }
    }
    @CommandBody
    val catSetFood=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender

                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Cat){
                        nearbyEntity.setFood(p.inventory.itemInMainHand)
                    }
                }

            }
        }
    }
    @CommandBody
    val catAdditionalGift=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender
                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Cat){
                        nearbyEntity.addAdditionalGift(UUID.randomUUID().toString(),p.inventory.itemInMainHand,100)
                    }
                }

            }
        }
    }
    @CommandBody
    val testItem=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender
                val item=buildItem(Material.COOKED_BEEF){
                    name="炎神战斧"
                    lore.addAll(arrayOf("熔岩核心锻造的神器","右键点燃敌人"))
                    flags.addAll(arrayOf(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_UNBREAKABLE))
                    enchants.put(Enchantment.SILK_TOUCH,1)
                }
                p.giveItem(item)
            }
        }
    }
}