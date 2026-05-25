package cn.autoforged.new_enchantments_mod_1779696334;

import cn.autoforged.new_enchantments_mod_1779696334.init.ModEnchantments;
import cn.autoforged.new_enchantments_mod_1779696334.recipe.ModRecipeSerializers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(NewEnchantmentsMod.MOD_ID)
public class NewEnchantmentsMod {
    public static final String MOD_ID = "new_enchantments_mod_1779696334";
    public static final Logger LOGGER = LoggerFactory.getLogger(NewEnchantmentsMod.class);

    public NewEnchantmentsMod(IEventBus modEventBus) {
        ModRecipeSerializers.SERIALIZERS.register(modEventBus);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
