package cn.autoforged.new_enchantments_mod_1779696334.datagen;

import cn.autoforged.new_enchantments_mod_1779696334.NewEnchantmentsMod;
import cn.autoforged.new_enchantments_mod_1779696334.init.ModEnchantments;
import cn.autoforged.new_enchantments_mod_1779696334.recipe.EnchantmentSmithingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // ===== 尖啸 Smithing Recipes =====
        // 1/2/4 Echo Shards + Book/EnchantedBook → Scream 1/2/3
        addEnchantSmithing(output, "scream_1", Items.ECHO_SHARD, 1,
            NewEnchantmentsMod.id("scream"), 1);
        addEnchantSmithing(output, "scream_2", Items.ECHO_SHARD, 2,
            NewEnchantmentsMod.id("scream"), 2);
        addEnchantSmithing(output, "scream_3", Items.ECHO_SHARD, 4,
            NewEnchantmentsMod.id("scream"), 3);

        // ===== 无谋 Smithing Recipe =====
        // Book with Smite/Bane >= 3 + Suspicious Stew → Replaces with Reckless (level-2)
        addReplaceSmithing(output, "reckless_from_smite",
            Ingredient.of(Items.SUSPICIOUS_STEW),
            1,
            NewEnchantmentsMod.id("reckless"),
            1,
            ResourceLocation.withDefaultNamespace("smite"),
            3);
        addReplaceSmithing(output, "reckless_from_bane",
            Ingredient.of(Items.SUSPICIOUS_STEW),
            1,
            NewEnchantmentsMod.id("reckless"),
            1,
            ResourceLocation.withDefaultNamespace("bane_of_arthropods"),
            3);

        // ===== 狂宴 Smithing Recipes =====
        // 32/64 Glistering Melon + Book → Feast 1/2
        addEnchantSmithing(output, "feast_1", Items.GLISTERING_MELON_SLICE, 32,
            NewEnchantmentsMod.id("feast"), 1);
        addEnchantSmithing(output, "feast_2", Items.GLISTERING_MELON_SLICE, 64,
            NewEnchantmentsMod.id("feast"), 2);

        // ===== 幽质 Smithing Recipes =====
        // 1/2/4 Ghast Tear + Book → Ethereal 1/2/3
        addEnchantSmithing(output, "ethereal_1", Items.GHAST_TEAR, 1,
            NewEnchantmentsMod.id("ethereal"), 1);
        addEnchantSmithing(output, "ethereal_2", Items.GHAST_TEAR, 2,
            NewEnchantmentsMod.id("ethereal"), 2);
        addEnchantSmithing(output, "ethereal_3", Items.GHAST_TEAR, 4,
            NewEnchantmentsMod.id("ethereal"), 3);

        // ===== 背刺 Smithing Recipes =====
        // 4 Spider Eyes + Book → Backstab 1
        // 8 Spider Eyes + Book → Backstab 2
        addEnchantSmithing(output, "backstab_1", Items.SPIDER_EYE, 4,
            NewEnchantmentsMod.id("backstab"), 1);
        addEnchantSmithing(output, "backstab_2", Items.SPIDER_EYE, 8,
            NewEnchantmentsMod.id("backstab"), 2);

        // ===== 浴血 Smithing Recipes =====
        // 8/16/32/64 Red Dye + Book → Bloodbath 1/2/3/4
        addEnchantSmithing(output, "bloodbath_1", Items.RED_DYE, 8,
            NewEnchantmentsMod.id("bloodbath"), 1);
        addEnchantSmithing(output, "bloodbath_2", Items.RED_DYE, 16,
            NewEnchantmentsMod.id("bloodbath"), 2);
        addEnchantSmithing(output, "bloodbath_3", Items.RED_DYE, 32,
            NewEnchantmentsMod.id("bloodbath"), 3);
        addEnchantSmithing(output, "bloodbath_4", Items.RED_DYE, 64,
            NewEnchantmentsMod.id("bloodbath"), 4);
    }

    private void addEnchantSmithing(RecipeOutput output, String name,
                                     net.minecraft.world.level.ItemLike addition, int additionCount,
                                     ResourceLocation enchantment, int level) {
        var recipe = new EnchantmentSmithingRecipe(
            Ingredient.of(addition), additionCount,
            enchantment, level,
            Optional.empty(), Optional.empty()
        );
        output.accept(
            NewEnchantmentsMod.id(name),
            recipe,
            null
        );
    }

    private void addReplaceSmithing(RecipeOutput output, String name,
                                     Ingredient addition, int additionCount,
                                     ResourceLocation targetEnchantment, int targetLevel,
                                     ResourceLocation sourceEnchantment, int minSourceLevel) {
        var recipe = new EnchantmentSmithingRecipe(
            addition, additionCount,
            targetEnchantment, targetLevel,
            Optional.of(sourceEnchantment), Optional.of(minSourceLevel)
        );
        output.accept(
            NewEnchantmentsMod.id(name),
            recipe,
            null
        );
    }
}
