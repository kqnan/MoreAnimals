package szuc.kqn.moreanimals.models

object Tree {
    lateinit var map:HashMap <String,String>
    init {
        // TODO: 读入繁殖树，初始化map
    }
    fun mutations(father:String,mother:String ):String? {
        return map[father + mother] ?: map[mother + father]
    }
}