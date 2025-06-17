package szuc.kqn.moreanimals

import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.entity.Sheep
import szuc.kqn.moreanimals.nms.SheepAIInjector
import szuc.kqn.moreanimals.nms.eatBlock

import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.module.nms.nmsProxy

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
}