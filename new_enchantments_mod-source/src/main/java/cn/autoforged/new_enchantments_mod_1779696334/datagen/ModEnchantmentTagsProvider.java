package cn.autoforged.new_enchantments_mod_1779696334.datagen;

import cn.autoforged.new_enchantments_mod_1779696334.init.ModEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagsProvider extends EnchantmentTagsProvider {

    private final String modId;

    public ModEnchantmentTagsProvider(PackOutput output,
                                       CompletableFuture<HolderLookup.Provider> lookupProvider,
                                       String modId,
                                       ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
        this.modId = modId;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // All enchantments available in enchanting table
        tag(EnchantmentTags.IN_ENCHANTING_TABLE)
            .add(ModEnchantments.SCREAM)
            .add(ModEnchantments.RECKLESS)
            .add(ModEnchantments.FEAST)
            .add(ModEnchantments.WILD)
            .add(ModEnchantments.BLOODBATH)
            .add(ModEnchantments.TENACITY)
            .add(ModEnchantments.BACKSTAB)
            .add(ModEnchantments.AGONY)
            .add(ModEnchantments.CATALYSIS);

        // Treasure enchantments
        tag(EnchantmentTags.TREASURE)
            .add(ModEnchantments.ETHEREAL)
            .add(ModEnchantments.CALAMITY);

        // Tradeable and random loot
        tag(EnchantmentTags.TRADEABLE)
            .add(ModEnchantments.SCREAM)
            .add(ModEnchantments.RECKLESS)
            .add(ModEnchantments.FEAST)
            .add(ModEnchantments.WILD)
            .add(ModEnchantments.BLOODBATH)
            .add(ModEnchantments.TENACITY)
            .add(ModEnchantments.ETHEREAL)
            .add(ModEnchantments.BACKSTAB)
            .add(ModEnchantments.AGONY)
            .add(ModEnchantments.CALAMITY)
            .add(ModEnchantments.CATALYSIS);

        tag(EnchantmentTags.ON_RANDOM_LOOT)
            .add(ModEnchantments.SCREAM)
            .add(ModEnchantments.RECKLESS)
            .add(ModEnchantments.FEAST)
            .add(ModEnchantments.WILD)
            .add(ModEnchantments.BLOODBATH)
            .add(ModEnchantments.TENACITY)
            .add(ModEnchantments.ETHEREAL)
            .add(ModEnchantments.BACKSTAB)
            .add(ModEnchantments.AGONY)
            .add(ModEnchantments.CALAMITY)
            .add(ModEnchantments.CATALYSIS);

        // Add reckless to DAMAGE_EXCLUSIVE (conflicts with sharpness, smite, bane)
        tag(EnchantmentTags.DAMAGE_EXCLUSIVE)
            .add(ModEnchantments.RECKLESS);

        // Calamity exclusive set: conflicts with bloodbath, wild, reckless
        tag(ModEnchantments.CALAMITY_EXCLUSIVE)
            .add(ModEnchantments.BLOODBATH)
            .add(ModEnchantments.WILD)
            .add(ModEnchantments.RECKLESS);
    }
}
