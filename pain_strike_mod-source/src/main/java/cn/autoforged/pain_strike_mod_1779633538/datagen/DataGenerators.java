package cn.autoforged.pain_strike_mod_1779633538.datagen;

import cn.autoforged.pain_strike_mod_1779633538.PainStrikeMod;
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

@EventBusSubscriber(modid = PainStrikeMod.MODID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        RegistrySetBuilder registryBuilder = new RegistrySetBuilder()
                .add(Registries.ENCHANTMENT, ModEnchantmentProvider::bootstrap);

        DatapackBuiltinEntriesProvider datapackProvider = new DatapackBuiltinEntriesProvider(
                packOutput, lookupProvider, registryBuilder, Set.of(PainStrikeMod.MODID));
        generator.addProvider(event.includeServer(), datapackProvider);

        generator.addProvider(event.includeServer(),
                new ModEnchantmentTagsProvider(packOutput, datapackProvider.getRegistryProvider(), existingFileHelper));

        generator.addProvider(event.includeClient(),
                new ModItemModelProvider(packOutput, existingFileHelper));

        generator.addProvider(event.includeServer(),
                new ModRecipeProvider(packOutput, lookupProvider));
    }
}
