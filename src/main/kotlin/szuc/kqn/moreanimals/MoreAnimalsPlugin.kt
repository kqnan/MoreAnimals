package szuc.kqn.moreanimals


import net.bytebuddy.ByteBuddy
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.minecraft.world.entity.animal.EntitySheep

import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import java.net.URL
import java.net.URLClassLoader


@RuntimeDependencies (
    RuntimeDependency(value = "net.bytebuddy:byte-buddy:1.17.5"),
    RuntimeDependency(value = "net.bytebuddy:byte-buddy-agent:1.17.5"),
    RuntimeDependency(value = "de.tr7zw:item-nbt-api-plugin:2.15.0")

)
object MoreAnimals : Plugin() {

    override fun onEnable() {
        info("Successfully running MoreAnimals!")

    }
}