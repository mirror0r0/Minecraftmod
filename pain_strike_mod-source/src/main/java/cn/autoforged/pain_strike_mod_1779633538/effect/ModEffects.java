package cn.autoforged.pain_strike_mod_1779633538.effect;

import cn.autoforged.pain_strike_mod_1779633538.PainStrikeMod;
import cn.autoforged.pain_strike_mod_1779633538.effect.custom.VulnerabilityEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, PainStrikeMod.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> VULNERABILITY = EFFECTS.register("vulnerability",
            () -> new VulnerabilityEffect(MobEffectCategory.HARMFUL, 0xAA00FF));
}
