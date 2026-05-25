package cn.autoforged.new_enchantments_mod_1779696334.recipe;

import cn.autoforged.new_enchantments_mod_1779696334.NewEnchantmentsMod;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class EnchantmentSmithingRecipe implements SmithingRecipe {
    private final Ingredient addition;
    private final int additionCount;
    private final ResourceLocation enchantmentId;
    private final int level;
    private final Optional<ResourceLocation> replaceSourceId;
    private final Optional<Integer> minSourceLevel;

    public EnchantmentSmithingRecipe(Ingredient addition, int additionCount,
                                     ResourceLocation enchantmentId, int level,
                                     Optional<ResourceLocation> replaceSourceId,
                                     Optional<Integer> minSourceLevel) {
        this.addition = addition;
        this.additionCount = additionCount;
        this.enchantmentId = enchantmentId;
        this.level = level;
        this.replaceSourceId = replaceSourceId;
        this.minSourceLevel = minSourceLevel;
    }

    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        if (!isBook(input.base())) return false;
        if (!this.addition.test(input.addition())) return false;
        if (input.addition().getCount() < this.additionCount) return false;

        if (replaceSourceId.isPresent() && minSourceLevel.isPresent()) {
            var reg = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
            var holder = reg.getHolder(ResourceKey.create(Registries.ENCHANTMENT, replaceSourceId.get()));
            if (holder.isEmpty()) return false;
            return EnchantmentHelper.getItemEnchantmentLevel(holder.get(), input.base()) >= minSourceLevel.get();
        }

        return true;
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) {
        ItemStack output = input.base().copy();
        output.setCount(1);

        if (output.is(Items.BOOK)) {
            output = new ItemStack(Items.ENCHANTED_BOOK);
        }

        var registry = registries.lookupOrThrow(Registries.ENCHANTMENT);

        if (replaceSourceId.isPresent()) {
            var sourceHolder = registry.get(ResourceKey.create(Registries.ENCHANTMENT, replaceSourceId.get()));
            if (sourceHolder.isPresent()) {
                var existingEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(output);
                var mutable = new ItemEnchantments.Mutable(existingEnchantments);
                mutable.removeIf(holder -> holder.is(sourceHolder.get().key()));
                EnchantmentHelper.setEnchantments(output, mutable.toImmutable());
            }
        }

        var enchantmentHolder = registry.get(ResourceKey.create(Registries.ENCHANTMENT, enchantmentId));
        if (enchantmentHolder.isPresent()) {
            output.enchant(enchantmentHolder.get(), level);
        }

        return output;
    }

    public int getAdditionCount() {
        return additionCount;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
        registries.lookupOrThrow(Registries.ENCHANTMENT)
            .get(ResourceKey.create(Registries.ENCHANTMENT, enchantmentId))
            .ifPresent(holder -> stack.enchant(holder, level));
        return stack;
    }

    private static boolean isBook(ItemStack stack) {
        return stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK);
    }

    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return isBook(stack);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return this.addition.test(stack);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ENCHANTMENT_SMITHING.get();
    }

    public static class Serializer implements RecipeSerializer<EnchantmentSmithingRecipe> {
        private static final MapCodec<EnchantmentSmithingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                Ingredient.CODEC.fieldOf("addition").forGetter(r -> r.addition),
                net.minecraft.util.ExtraCodecs.POSITIVE_INT.fieldOf("addition_count").forGetter(r -> r.additionCount),
                ResourceLocation.CODEC.fieldOf("enchantment").forGetter(r -> r.enchantmentId),
                net.minecraft.util.ExtraCodecs.intRange(1, 5).fieldOf("level").forGetter(r -> r.level),
                ResourceLocation.CODEC.optionalFieldOf("replace_source").forGetter(r -> r.replaceSourceId),
                net.minecraft.util.ExtraCodecs.intRange(0, 5).optionalFieldOf("min_source_level").forGetter(r -> r.minSourceLevel)
            ).apply(instance, EnchantmentSmithingRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, EnchantmentSmithingRecipe> STREAM_CODEC =
            StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<EnchantmentSmithingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EnchantmentSmithingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static EnchantmentSmithingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            Ingredient addition = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            int additionCount = buf.readVarInt();
            ResourceLocation enchantmentId = buf.readResourceLocation();
            int level = buf.readVarInt();
            Optional<ResourceLocation> replaceSource = buf.readOptional(FriendlyByteBuf::readResourceLocation);
            Optional<Integer> minSourceLevel = buf.readOptional(FriendlyByteBuf::readVarInt);
            return new EnchantmentSmithingRecipe(addition, additionCount, enchantmentId, level, replaceSource, minSourceLevel);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, EnchantmentSmithingRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.addition);
            buf.writeVarInt(recipe.additionCount);
            buf.writeResourceLocation(recipe.enchantmentId);
            buf.writeVarInt(recipe.level);
            buf.writeOptional(recipe.replaceSourceId, FriendlyByteBuf::writeResourceLocation);
            buf.writeOptional(recipe.minSourceLevel, FriendlyByteBuf::writeVarInt);
        }
    }
}
