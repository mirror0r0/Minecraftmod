package cn.autoforged.pain_strike_mod_1779633538.datagen;

import cn.autoforged.pain_strike_mod_1779633538.PainStrikeMod;
import cn.autoforged.pain_strike_mod_1779633538.init.ModEnchantments;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

public class ModEnchantmentProvider {

    public static void bootstrap(BootstrapContext<Enchantment> bootstrap) {
        HolderGetter<Item> items = bootstrap.lookup(net.minecraft.core.registries.Registries.ITEM);
        HolderGetter<Enchantment> enchantments = bootstrap.lookup(net.minecraft.core.registries.Registries.ENCHANTMENT);

        register(bootstrap, ModEnchantments.RUIN, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        5, 3,
                        Enchantment.dynamicCost(13, 6),
                        Enchantment.dynamicCost(25, 9),
                        4,
                        EquipmentSlotGroup.MAINHAND
                )
        ).exclusiveWith(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE)));

        register(bootstrap, ModEnchantments.HEAVY_BLADE, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        2, 1,
                        Enchantment.dynamicCost(30, 0),
                        Enchantment.dynamicCost(50, 0),
                        4,
                        EquipmentSlotGroup.MAINHAND
                )
        ).exclusiveWith(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                .withEffect(EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                ResourceLocation.fromNamespaceAndPath(PainStrikeMod.MODID, "enchantment.heavy_blade.attack_speed"),
                                Attributes.ATTACK_SPEED,
                                LevelBasedValue.constant(-0.5F),
                                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        ))
                .withEffect(EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                ResourceLocation.fromNamespaceAndPath(PainStrikeMod.MODID, "enchantment.heavy_blade.attack_damage"),
                                Attributes.ATTACK_DAMAGE,
                                LevelBasedValue.constant(4.0F),
                                AttributeModifier.Operation.ADD_VALUE
                        )));

        register(bootstrap, ModEnchantments.PAIN_STRIKE, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        5, 3,
                        Enchantment.dynamicCost(10, 8),
                        Enchantment.dynamicCost(25, 9),
                        4,
                        EquipmentSlotGroup.MAINHAND
                )
        ).exclusiveWith(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE)));
    }

    private static void register(BootstrapContext<Enchantment> bootstrap, net.minecraft.resources.ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        bootstrap.register(key, builder.build(key.location()));
    }
}
