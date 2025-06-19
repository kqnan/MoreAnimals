package szuc.kqn.moreanimals

import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.entity.Sheep
import org.bukkit.inventory.ItemStack
import szuc.kqn.moreanimals.nms.*


import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.module.nms.itemTagReader
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem
import taboolib.platform.util.giveItem

@CommandHeader("moreanimals", ["ma"],permission = "moreanimals.commands")
object Command {

    @CommandBody
    val test= subCommand {
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
    val test2=subCommand {
        execute<CommandSender> {
                sender, context, argument ->
            // 获取参数的值
            if(sender is Player){
                val p= sender
                for (nearbyEntity in p.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if(nearbyEntity is Sheep){
                        (nearbyEntity as Sheep).eatBlock(
                            canEat = { loc->
                                loc.block.type==Material.DIAMOND_ORE

                            },
                            setBlock ={ loc->loc.block.setType(Material.ACACIA_WOOD)
                                loc.block.blockData
                            },
                        )

                    }
                }
            }
        }
    }
    @CommandBody
    val injectSetFood=subCommand {
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
    val setFood=subCommand {
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
}