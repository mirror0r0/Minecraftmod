package cn.autoforged.pain_strike_mod_1779633538.event;

import cn.autoforged.pain_strike_mod_1779633538.PainStrikeMod;
import cn.autoforged.pain_strike_mod_1779633538.init.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

@EventBusSubscriber(modid = PainStrikeMod.MODID)
public class AnvilRecipeHandler {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (handleRuinRecipe(event, left, right)) return;
        if (handleHeavyBladeRecipe(event, left, right)) return;
        if (handlePainStrikeRecipe(event, left, right)) return;
    }

    private static boolean handleRuinRecipe(AnvilUpdateEvent event, ItemStack left, ItemStack right) {
        if (left.getItem() != Items.ENCHANTED_BOOK) return false;
        if (right.getItem() != Items.BLAZE_POWDER || right.getCount() < 8) return false;

        ServerLevel serverLevel = getServerLevel(event);
        if (serverLevel == null) return false;

        Holder<Enchantment> sharpnessHolder = getEnchantmentHolder(serverLevel, net.minecraft.world.item.enchantment.Enchantments.SHARPNESS);
        if (sharpnessHolder == null) return false;

        ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(left);
        int sharpnessLevel = enchantments.getLevel(sharpnessHolder);
        if (sharpnessLevel < 3) return false;

        int ruinLevel = sharpnessLevel - 2;
        if (ruinLevel < 1) return false;

        Holder<Enchantment> ruinHolder = getEnchantmentHolder(serverLevel, ModEnchantments.RUIN);
        if (ruinHolder == null) return false;

        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(enchantments);
        mutable.set(sharpnessHolder, 0);
        mutable.set(ruinHolder, ruinLevel);

        ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(output, mutable.toImmutable());

        event.setOutput(output);
        event.setCost(12 + sharpnessLevel * 3);
        event.setMaterialCost(8);
        return true;
    }

    private static boolean handleHeavyBladeRecipe(AnvilUpdateEvent event, ItemStack left, ItemStack right) {
        if (left.getItem() != Items.ENCHANTED_BOOK) return false;
        if (right.getItem() != Items.HEAVY_CORE || right.getCount() < 1) return false;

        ServerLevel serverLevel = getServerLevel(event);
        if (serverLevel == null) return false;

        Holder<Enchantment> sharpnessHolder = getEnchantmentHolder(serverLevel, net.minecraft.world.item.enchantment.Enchantments.SHARPNESS);
        if (sharpnessHolder == null) return false;

        ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(left);
        int sharpnessLevel = enchantments.getLevel(sharpnessHolder);
        if (sharpnessLevel < 5) return false;

        Holder<Enchantment> heavyBladeHolder = getEnchantmentHolder(serverLevel, ModEnchantments.HEAVY_BLADE);
        if (heavyBladeHolder == null) return false;

        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(enchantments);
        mutable.set(sharpnessHolder, 0);
        mutable.set(heavyBladeHolder, 1);

        ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(output, mutable.toImmutable());

        event.setOutput(output);
        event.setCost(20);
        event.setMaterialCost(1);
        return true;
    }

    private static boolean handlePainStrikeRecipe(AnvilUpdateEvent event, ItemStack left, ItemStack right) {
        boolean isBook = left.getItem() == Items.BOOK;
        boolean isEnchantedBook = left.getItem() == Items.ENCHANTED_BOOK;
        if (!isBook && !isEnchantedBook) return false;
        if (right.getItem() != Items.FERMENTED_SPIDER_EYE || right.getCount() < 8) return false;

        ServerLevel serverLevel = getServerLevel(event);
        if (serverLevel == null) return false;

        Holder<Enchantment> painStrikeHolder = getEnchantmentHolder(serverLevel, ModEnchantments.PAIN_STRIKE);
        if (painStrikeHolder == null) return false;

        ItemEnchantments.Mutable mutable;
        if (isEnchantedBook) {
            ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(left);
            mutable = new ItemEnchantments.Mutable(enchantments);
        } else {
            mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        }
        mutable.set(painStrikeHolder, 1);

        ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(output, mutable.toImmutable());

        event.setOutput(output);
        event.setCost(8);
        event.setMaterialCost(8);
        return true;
    }

    private static ServerLevel getServerLevel(AnvilUpdateEvent event) {
        if (event.getPlayer().level() instanceof ServerLevel serverLevel) {
            return serverLevel;
        }
        return null;
    }

    private static Holder<Enchantment> getEnchantmentHolder(ServerLevel level, net.minecraft.resources.ResourceKey<Enchantment> key) {
        return level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolder(key).orElse(null);
    }
}
