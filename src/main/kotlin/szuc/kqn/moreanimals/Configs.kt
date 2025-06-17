package szuc.kqn.moreanimals

import taboolib.common.LifeCycle
import taboolib.common.io.newFolder
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.Configuration

object Configs {
    @Config("config.yml")
    lateinit var config:ConfigFile

    // 读取一个文件夹里的所有yaml然后加载
    lateinit var entity:HashMap<String,String>
    lateinit var tree:HashMap<String,String>

    @Awake(LifeCycle.ENABLE)
    fun load() {

        releaseResourceFile("bee/entity.yml",replace = false)
        releaseResourceFile("bee/tree.yml",replace = false)
        releaseResourceFile("cat/entity.yml",replace = false)
        releaseResourceFile("cat/tree.yml",replace = false)
        releaseResourceFile("chicken/entity.yml",replace = false)
        releaseResourceFile("chicken/tree.yml",replace = false)
        releaseResourceFile("cow/entity.yml",replace = false)
        releaseResourceFile("cow/tree.yml",replace = false)
        val file = newFolder(getDataFolder(), "/", create = false)
        file.walk()
            .filter { it.isFile }
            .filter {it.name=="tree.yml" || it.name == "entity.yml" }
            .forEach {
                // TODO: 注入内存
                if(it.name=="tree.yml"){
                    val cfg=Configuration.loadFromFile(it)

                }else if (it.name == "entity.yml"){

                }

            }
    }
}