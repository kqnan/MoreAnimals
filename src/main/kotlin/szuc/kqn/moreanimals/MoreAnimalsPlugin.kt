package szuc.kqn.moreanimals

import org.bukkit.entity.Sheep
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.module.ai.getGoalAi
@RuntimeDependencies (
    RuntimeDependency(value = "net.bytebuddy:byte-buddy:1.17.5"),
    RuntimeDependency(value = "net.bytebuddy:byte-buddy-agent:1.17.5"),
    RuntimeDependency(repository = "https://repo.codemc.io/repository/maven-public/", value = "de.tr7zw:item-nbt-api-plugin:2.15.0")
)
object MoreAnimals : Plugin() {

    override fun onEnable() {
        info("Successfully running MoreAnimals!")

    }
}