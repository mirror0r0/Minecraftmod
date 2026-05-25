package cn.autoforged.pain_strike_mod_1779633538.event;

import cn.autoforged.pain_strike_mod_1779633538.PainStrikeMod;
import cn.autoforged.pain_strike_mod_1779633538.init.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

// 注意：如果这个类是在 mod 主类所在的 bus 上监听，可能需要修改 bus 类型，
// 但 BuildCreativeModeTabContentsEvent 通常在 NeoForge 事件总线上，这样写是正确的。
@EventBusSubscriber(modid = PainStrikeMod.MODID)
public class CreativeTabEvents {

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        // 在 1.21 中，附魔书通常被自动归类到 INGREDIENTS 标签页
        if (event.getTabKey() != CreativeModeTabs.INGREDIENTS) return;

        // 手动注入附魔书（如果游戏没有自动注入的话）
        addEnchantedBook(event, ModEnchantments.RUIN);
        addEnchantedBook(event, ModEnchantments.HEAVY_BLADE);
        addEnchantedBook(event, ModEnchantments.PAIN_STRIKE);
    }

    /**
     * 安全地向外创造模式物品栏注入附魔书 (1.21 写法)
     */
    private static void addEnchantedBook(BuildCreativeModeTabContentsEvent event, ResourceKey<Enchantment> key) {
        // 1.21 中，我们需要通过 Registry 获取 Enchantment 的 Holder 引用
        event.getRegistryAccess().registry(Registry.ENCHANTMENT_REGISTRY)
                .ifPresent(registry -> {
                    // 尝试获取附魔的 Holder
                    registry.getHolder(key).ifPresent(enchantmentHolder -> {
                        // 遍历从 1 到最大等级，生成所有等级的附魔书
                        for (int level = enchantmentHolder.value().getMinLevel(); level <= enchantmentHolder.value().getMaxLevel(); level++) {
                            // 使用 1.21 提供的 EnchantmentManager 动态创建附魔书 ItemStack
                            ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                            // 将附魔信息写入物品栈 NBT
                            net.minecraft.world.item.enchantment.EnchantmentHelper.setEnchantments(enchantedBook, java.util.List.of(
                                    new net.minecraft.world.item.enchantment.EnchantmentInstance(enchantmentHolder, level)
                            ));

                            // 将生成的附魔书添加到创造模式标签页
                            event.accept(enchantedBook);
                        }
                    });
                });
    }
}
