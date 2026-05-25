package cn.autoforged.new_enchantments_mod_1779696334.datagen;

import cn.autoforged.new_enchantments_mod_1779696334.NewEnchantmentsMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = NewEnchantmentsMod.MOD_ID)
public class ModDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        RegistrySetBuilder builder = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, ModEnchantmentBootstrap::bootstrap);

        DatapackBuiltinEntriesProvider datapackProvider = new DatapackBuiltinEntriesProvider(
            output, lookupProvider, builder, Set.of(NewEnchantmentsMod.MOD_ID));
        generator.addProvider(event.includeServer(), datapackProvider);

        generator.addProvider(event.includeServer(),
            new ModEnchantmentTagsProvider(output, datapackProvider.getRegistryProvider(),
                NewEnchantmentsMod.MOD_ID, existingFileHelper));

        generator.addProvider(event.includeServer(),
            new ModRecipeProvider(output, lookupProvider));
    }
}
