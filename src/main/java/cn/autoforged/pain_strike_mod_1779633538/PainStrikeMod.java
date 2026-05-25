package cn.autoforged.pain_strike_mod_1779633538;

import cn.autoforged.pain_strike_mod_1779633538.effect.ModEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(PainStrikeMod.MODID)
public class PainStrikeMod {
    public static final String MODID = "pain_strike_mod_1779633538";

    public PainStrikeMod(IEventBus modEventBus, ModContainer modContainer) {
        ModEffects.EFFECTS.register(modEventBus);
    }
}
