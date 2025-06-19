package szuc.kqn.moreanimals.nms

import net.minecraft.world.entity.animal.EntityAnimal
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Sheep
import org.bukkit.metadata.MetadataValue
import org.bukkit.persistence.PersistentDataType
import taboolib.common.util.unsafeLazy
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.library.reflex.asm.AsmClassMethod
import taboolib.library.reflex.asm.AsmClassMethodVisitor
import taboolib.module.nms.nmsProxy
import taboolib.platform.util.bukkitPlugin
import java.util.function.Function
interface SheepAIInjector {
    //canUse，当羊走到location位置时，是否应该吃到这里的方块
    //setBlock,吃掉这里的方块后，用什么方块替换，返回替换方块的BlockData
    fun eatBlock(sheep: Sheep, canUse:Function<Location,Boolean>, setBlock: Function<Location, Unit>) :Any

    companion object {
        val INSTANCE by unsafeLazy {
            nmsProxy<SheepAIInjector>()
        }
    }
}
