package cn.autoforged.new_enchantments_mod_1779696334.init;

import cn.autoforged.new_enchantments_mod_1779696334.NewEnchantmentsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {
    // Scream - 尖啸
    public static final ResourceKey<Enchantment> SCREAM = key("scream");
    // Reckless - 无谋
    public static final ResourceKey<Enchantment> RECKLESS = key("reckless");
    // Feast - 狂宴
    public static final ResourceKey<Enchantment> FEAST = key("feast");
    // Wild - 狂野
    public static final ResourceKey<Enchantment> WILD = key("wild");
    // Bloodbath - 浴血
    public static final ResourceKey<Enchantment> BLOODBATH = key("bloodbath");
    // Tenacity - 坚毅
    public static final ResourceKey<Enchantment> TENACITY = key("tenacity");
    // Ethereal - 幽质
    public static final ResourceKey<Enchantment> ETHEREAL = key("ethereal");
    // Backstab - 背刺
    public static final ResourceKey<Enchantment> BACKSTAB = key("backstab");
    // Agony - 苦痛
    public static final ResourceKey<Enchantment> AGONY = key("agony");
    // Calamity - 灾祸
    public static final ResourceKey<Enchantment> CALAMITY = key("calamity");
    // Catalysis - 催化
    public static final ResourceKey<Enchantment> CATALYSIS = key("catalysis");

    // Tag for calamity exclusive set: conflicts with bloodbath, wild, reckless
    public static final TagKey<Enchantment> CALAMITY_EXCLUSIVE = tag("exclusive_set/calamity");

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(NewEnchantmentsMod.MOD_ID, name));
    }

    private static TagKey<Enchantment> tag(String name) {
        return TagKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(NewEnchantmentsMod.MOD_ID, name));
    }
}
