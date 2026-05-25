package cn.autoforged.pain_strike_mod_1779633538.datagen;

import cn.autoforged.pain_strike_mod_1779633538.PainStrikeMod;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PainStrikeMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
    }
}
