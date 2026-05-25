package cn.autoforged.pain_strike_mod_1779633538.datagen;

import cn.autoforged.pain_strike_mod_1779633538.PainStrikeMod;
import cn.autoforged.pain_strike_mod_1779633538.init.ModEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagsProvider extends EnchantmentTagsProvider {

    public ModEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PainStrikeMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EnchantmentTags.DAMAGE_EXCLUSIVE)
                .add(ModEnchantments.RUIN)
                .add(ModEnchantments.HEAVY_BLADE)
                .add(ModEnchantments.PAIN_STRIKE);

        this.tag(EnchantmentTags.IN_ENCHANTING_TABLE)
                .add(ModEnchantments.RUIN)
                .add(ModEnchantments.HEAVY_BLADE)
                .add(ModEnchantments.PAIN_STRIKE);

        this.tag(EnchantmentTags.TRADEABLE)
                .add(ModEnchantments.RUIN)
                .add(ModEnchantments.PAIN_STRIKE);

        this.tag(EnchantmentTags.ON_RANDOM_LOOT)
                .add(ModEnchantments.RUIN)
                .add(ModEnchantments.PAIN_STRIKE);

        this.tag(EnchantmentTags.TREASURE)
                .add(ModEnchantments.HEAVY_BLADE);
    }
}
