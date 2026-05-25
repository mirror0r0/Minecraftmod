package cn.autoforged.pain_strike_mod_1779633538.event;

import cn.autoforged.pain_strike_mod_1779633538.PainStrikeMod;
import cn.autoforged.pain_strike_mod_1779633538.init.ModEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = PainStrikeMod.MODID)
public class EnchantmentTooltipHandler {

    private static final ResourceKey<Enchantment>[] CUSTOM_ENCHANTMENTS = new ResourceKey[]{
            ModEnchantments.RUIN,
            ModEnchantments.HEAVY_BLADE,
            ModEnchantments.PAIN_STRIKE
    };

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEnchanted() && stack.getItem() != Items.ENCHANTED_BOOK) return;

        Player player = event.getEntity();
        if (player == null) return;
        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);

        for (var entry : enchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            for (ResourceKey<Enchantment> key : CUSTOM_ENCHANTMENTS) {
                if (holder.is(key)) {
                    Component desc = Component.translatable(key.location().toLanguageKey("enchantment") + ".desc")
                            .withStyle(ChatFormatting.GRAY);
                    event.getToolTip().add(desc);
                    break;
                }
            }
        }
    }
}
