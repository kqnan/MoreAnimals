package szuc.kqn.moreanimals.models

data class EntityTypeInfo(val type:String,val name:String,val production:String,
                      val product_cooldown:String,val feed:String,val drop:String,
                      val drop_amount:String,val mutations_prob:String,val common_offspring_level:String)



data class EntityInfo(val type:String,val level:Int)
