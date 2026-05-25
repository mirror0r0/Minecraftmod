package cn.autoforged.new_enchantments_mod_1779696334.recipe;

import cn.autoforged.new_enchantments_mod_1779696334.NewEnchantmentsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, NewEnchantmentsMod.MOD_ID);

    public static final Supplier<RecipeSerializer<EnchantmentSmithingRecipe>> ENCHANTMENT_SMITHING =
        SERIALIZERS.register("enchantment_smithing", EnchantmentSmithingRecipe.Serializer::new);
}
