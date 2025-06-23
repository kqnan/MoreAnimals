package szuc.kqn.moreanimals.utils

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Type


class ItemStackSerializer : JsonDeserializer<ItemStack?>, JsonSerializer<ItemStack?> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ItemStack {
        return ItemStack.deserialize(
            GsonBuilder().create().fromJson(json, object : TypeToken<Map<String?, Any?>?>() {}.getType())
        )
    }

    override fun serialize(src: ItemStack?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return GsonBuilder().create().toJsonTree(src?.serialize())
    }



}