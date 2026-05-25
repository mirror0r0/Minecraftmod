package cn.autoforged.new_enchantments_mod_1779696334.datagen;

import cn.autoforged.new_enchantments_mod_1779696334.NewEnchantmentsMod;
import cn.autoforged.new_enchantments_mod_1779696334.init.ModEnchantments;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

public class ModEnchantmentBootstrap {

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);

        // 1. Scream - 尖啸
        register(context, ModEnchantments.SCREAM,
            Enchantment.enchantment(
                Enchantment.definition(
                    items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    10, 3,
                    Enchantment.dynamicCost(15, 9),
                    Enchantment.dynamicCost(65, 9),
                    4,
                    EquipmentSlotGroup.MAINHAND
                )
            )
        );

        // 2. Reckless - 无谋
        register(context, ModEnchantments.RECKLESS,
            Enchantment.enchantment(
                Enchantment.definition(
                    items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    3, 3,
                    Enchantment.dynamicCost(10, 15),
                    Enchantment.dynamicCost(25, 15),
                    8,
                    EquipmentSlotGroup.MAINHAND
                )
            )
            .exclusiveWith(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
            .withEffect(
                EnchantmentEffectComponents.ATTRIBUTES,
                new EnchantmentAttributeEffect(
                    NewEnchantmentsMod.id("enchantment.reckless.attack_speed"),
                    Attributes.ATTACK_SPEED,
                    LevelBasedValue.perLevel(0.2F, 0.4F),
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                )
            )
        );

        // 3. Feast - 狂宴
        register(context, ModEnchantments.FEAST,
            Enchantment.enchantment(
                Enchantment.definition(
                    items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    3, 2,
                    Enchantment.dynamicCost(20, 15),
                    Enchantment.dynamicCost(65, 15),
                    8,
                    EquipmentSlotGroup.MAINHAND
                )
            )
        );

        // 4. Wild - 狂野
        register(context, ModEnchantments.WILD,
            Enchantment.enchantment(
                Enchantment.definition(
                    items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    5, 3,
                    Enchantment.dynamicCost(10, 10),
                    Enchantment.dynamicCost(25, 10),
                    4,
                    EquipmentSlotGroup.MAINHAND
                )
            )
        );

        // 5. Bloodbath - 浴血
        register(context, ModEnchantments.BLOODBATH,
            Enchantment.enchantment(
                Enchantment.definition(
                    items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    3, 4,
                    Enchantment.dynamicCost(10, 12),
                    Enchantment.dynamicCost(30, 12),
                    8,
                    EquipmentSlotGroup.MAINHAND
                )
            )
        );

        // 6. Tenacity - 坚毅: shield axe disable reduction, armor +2/3/4/5, speed -20/26/30/33%
        var shieldKey = ResourceKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("shield"));
        register(context, ModEnchantments.TENACITY,
            Enchantment.enchantment(
                Enchantment.definition(
                    HolderSet.direct(items.getOrThrow(shieldKey)),
                    5, 4,
                    Enchantment.dynamicCost(10, 8),
                    Enchantment.dynamicCost(30, 8),
                    4,
                    EquipmentSlotGroup.OFFHAND, EquipmentSlotGroup.MAINHAND
                )
            )
            .withEffect(
                EnchantmentEffectComponents.ATTRIBUTES,
                new EnchantmentAttributeEffect(
                    NewEnchantmentsMod.id("enchantment.tenacity.armor"),
                    Attributes.ARMOR,
                    LevelBasedValue.perLevel(2.0F, 1.0F),
                    AttributeModifier.Operation.ADD_VALUE
                )
            )
            .withEffect(
                EnchantmentEffectComponents.ATTRIBUTES,
                new EnchantmentAttributeEffect(
                    NewEnchantmentsMod.id("enchantment.tenacity.speed"),
                    Attributes.MOVEMENT_SPEED,
                    LevelBasedValue.perLevel(-0.20F, -0.043F),
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                )
            )
        );

        // 7. Ethereal - 幽质: armor toughness +40/60/80%, armor +1/2/3, on-hit armor reduction
        register(context, ModEnchantments.ETHEREAL,
            Enchantment.enchantment(
                Enchantment.definition(
                    items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                    1, 3,
                    Enchantment.dynamicCost(25, 20),
                    Enchantment.dynamicCost(65, 20),
                    8,
                    EquipmentSlotGroup.ARMOR
                )
            )
            .withEffect(
                EnchantmentEffectComponents.ATTRIBUTES,
                new EnchantmentAttributeEffect(
                    NewEnchantmentsMod.id("enchantment.ethereal.toughness"),
                    Attributes.ARMOR_TOUGHNESS,
                    LevelBasedValue.perLevel(0.4F, 0.2F),
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                )
            )
            .withEffect(
                EnchantmentEffectComponents.ATTRIBUTES,
                new EnchantmentAttributeEffect(
                    NewEnchantmentsMod.id("enchantment.ethereal.armor"),
                    Attributes.ARMOR,
                    LevelBasedValue.perLevel(1.0F, 1.0F),
                    AttributeModifier.Operation.ADD_VALUE
                )
            )
        );

        // 8. Backstab - 背刺
        register(context, ModEnchantments.BACKSTAB,
            Enchantment.enchantment(
                Enchantment.definition(
                    items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    3, 2,
                    Enchantment.dynamicCost(15, 12),
                    Enchantment.dynamicCost(45, 12),
                    8,
                    EquipmentSlotGroup.MAINHAND
                )
            )
        );

        // 9. Agony - 苦痛
        register(context, ModEnchantments.AGONY,
            Enchantment.enchantment(
                Enchantment.definition(
                    items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    5, 5,
                    Enchantment.dynamicCost(10, 8),
                    Enchantment.dynamicCost(20, 8),
                    4,
                    EquipmentSlotGroup.MAINHAND
                )
            )
        );

        // 10. Calamity - 灾祸
        register(context, ModEnchantments.CALAMITY,
            Enchantment.enchantment(
                Enchantment.definition(
                    items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    1, 3,
                    Enchantment.dynamicCost(25, 20),
                    Enchantment.dynamicCost(65, 20),
                    8,
                    EquipmentSlotGroup.MAINHAND
                )
            ).exclusiveWith(enchantments.getOrThrow(ModEnchantments.CALAMITY_EXCLUSIVE))
        );

        // 11. Catalysis - 催化
        register(context, ModEnchantments.CATALYSIS,
            Enchantment.enchantment(
                Enchantment.definition(
                    items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    3, 2,
                    Enchantment.dynamicCost(20, 15),
                    Enchantment.dynamicCost(65, 15),
                    8,
                    EquipmentSlotGroup.MAINHAND
                )
            )
        );
    }

    private static void register(BootstrapContext<Enchantment> context,
                                  ResourceKey<Enchantment> key,
                                  Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }
}
