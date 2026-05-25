package cn.autoforged.pain_strike_mod_1779633538.init;

import cn.autoforged.pain_strike_mod_1779633538.PainStrikeMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> RUIN = key("ruin");
    public static final ResourceKey<Enchantment> HEAVY_BLADE = key("heavy_blade");
    public static final ResourceKey<Enchantment> PAIN_STRIKE = key("pain_strike");

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(PainStrikeMod.MODID, name));
    }
}
