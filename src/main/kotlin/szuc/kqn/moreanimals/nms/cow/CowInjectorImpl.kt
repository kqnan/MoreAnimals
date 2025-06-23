package szuc.kqn.moreanimals.nms.cow

import net.minecraft.sounds.SoundEffects
import net.minecraft.world.EnumHand
import net.minecraft.world.EnumInteractionResult
import net.minecraft.world.entity.animal.EntityCow
import net.minecraft.world.entity.player.EntityHuman
import net.minecraft.world.item.ItemLiquidUtil
import net.minecraft.world.item.Items
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity
import org.bukkit.entity.Cow
import org.bukkit.persistence.PersistentDataType.BYTE_ARRAY
import taboolib.platform.util.deserializeToItemStack

class CowInjectorImpl:CowInjector {









    fun checkCooldown(cow:Cow):Boolean{
        return true
    }
    //利用Advice.OnMethodEnter注入到原方法进入时,返回值代表是否跳过原方法
    fun setProduct(cow: EntityCow, var0: EntityHuman, var1: EnumHand):Any?{
        val bukkitCow=Bukkit.getEntity(cow.uuid) as Cow
        val bukkitPlayer=Bukkit.getPlayer(var0.uuid)?:return null
        val item= bukkitCow.persistentDataContainer.get(NamespacedKey.fromString("moreanimals:product")!!, BYTE_ARRAY)?.deserializeToItemStack(false)?:return null
        if(bukkitCow.isAdult&&var0.getItemInHand(var1).`is`(Items.BUCKET)){
            if(checkCooldown(bukkitCow)){
                var0.playSound(SoundEffects.COW_MILK, 1.0f, 1.0f)
                bukkitPlayer.inventory.setItemInMainHand(item)
                return EnumInteractionResult.sidedSuccess(cow.level().isClientSide)
            }
        }
        return null
//        val var2 = var0.getItemInHand(var1)
//        if (var2.`is`(Items.BUCKET) && !cow.isBaby()) {
//            var0.playSound(SoundEffects.COW_MILK, 1.0f, 1.0f)
//            val var3 = ItemLiquidUtil.createFilledResult(var2, var0, Items.MILK_BUCKET.defaultInstance)
//            var0.setItemInHand(var1, var3)
//            return EnumInteractionResult.sidedSuccess(cow.level().isClientSide)
//        } else {
//            return super.mobInteract(var0, var1)
//        }
    }
}