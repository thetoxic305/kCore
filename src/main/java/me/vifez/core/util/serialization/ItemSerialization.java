package me.vifez.core.util.serialization;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import me.vifez.core.kCoreConstant;
import me.vifez.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemSerialization {

    private static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {
    }.getType();
    private static final Type MAP_STRING_INTEGER_TYPE = new TypeToken<Map<String, Integer>>() {
    }.getType();

    public static JsonObject serialize(ItemStack item) {
        if (item == null) {
            return serialize(new ItemStack(Material.AIR));
        }

        JsonObject object = new JsonObject();

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (meta.getDisplayName() != null) {
                object.addProperty("displayName", meta.getDisplayName());
            }

            if (meta.getLore() != null && !meta.getLore().isEmpty()) {
                object.addProperty("lore", kCoreConstant.GSON.toJson(meta.getLore()));
            }
        }

        object.addProperty("type", item.getType().toString());
        object.addProperty("amount", item.getAmount());
        object.addProperty("durability", item.getDurability());

        Map<String, Integer> enchantments = new HashMap<>();
        item.getEnchantments().forEach((enchantment, level) -> enchantments.put(enchantment.getName(), level));

        object.addProperty("enchants", kCoreConstant.GSON.toJson(enchantments));

        return object;
    }

    public static ItemStack deserialize(JsonObject object) {
        if (object == null || object.isJsonNull()) {
            return null;
        }

        Material type = Material.valueOf(object.get("type").getAsString());

        ItemBuilder builder = new ItemBuilder(type);
        if (object.has("displayName")) {
            builder.setName(object.get("displayName").getAsString());
        }

        if (object.has("lore")) {
            List<String> lore = kCoreConstant.GSON.fromJson(object.get("lore").getAsString(), LIST_STRING_TYPE);
            builder.setLore(lore);
        }

        builder.setAmount(object.get("amount").getAsInt());
        builder.setDurability(object.get("durability").getAsShort());

        Map<String, Integer> enchantments = kCoreConstant.GSON.fromJson(object.get("enchants").getAsString(), MAP_STRING_INTEGER_TYPE);
        enchantments.forEach((enchantmentName, level) -> {
            Enchantment enchantment = Enchantment.getByName(enchantmentName);
            builder.addEnchantment(enchantment, level);
        });

        return builder.build();
    }

}