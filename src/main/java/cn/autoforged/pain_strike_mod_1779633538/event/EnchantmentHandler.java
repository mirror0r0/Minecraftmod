package cn.autoforged.pain_strike_mod_1779633538.event;

import cn.autoforged.pain_strike_mod_1779633538.PainStrikeMod;
import cn.autoforged.pain_strike_mod_1779633538.effect.ModEffects;
import cn.autoforged.pain_strike_mod_1779633538.init.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = PainStrikeMod.MODID)
public class EnchantmentHandler {

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        Entity attackerEntity = event.getSource().getEntity();
        if (!(attackerEntity instanceof LivingEntity attacker)) return;

        ItemStack weapon = attacker.getMainHandItem();
        if (weapon.isEmpty()) return;

        Holder<Enchantment> ruinHolder = getEnchantmentHolder(attacker, ModEnchantments.RUIN);
        Holder<Enchantment> heavyBladeHolder = getEnchantmentHolder(attacker, ModEnchantments.HEAVY_BLADE);
        Holder<Enchantment> painStrikeHolder = getEnchantmentHolder(attacker, ModEnchantments.PAIN_STRIKE);

        int ruinLevel = ruinHolder != null ? EnchantmentHelper.getItemEnchantmentLevel(ruinHolder, weapon) : 0;
        int heavyBladeLevel = heavyBladeHolder != null ? EnchantmentHelper.getItemEnchantmentLevel(heavyBladeHolder, weapon) : 0;
        int painStrikeLevel = painStrikeHolder != null ? EnchantmentHelper.getItemEnchantmentLevel(painStrikeHolder, weapon) : 0;

        float currentDamage = event.getAmount();

        if (ruinLevel > 0) {
            float bonusDamage = 1.0F + ruinLevel;
            currentDamage += bonusDamage;
        }

        if (heavyBladeLevel > 0) {
            float extra = getHeavyBladeExtraDamage(attacker);
            currentDamage += extra;
        }

        event.setAmount(currentDamage);

        if (painStrikeLevel > 0) {
            if (isCriticalHit(attacker)) {
                float chance = 0.15F * painStrikeLevel;
                if (attacker.getRandom().nextFloat() < chance) {
                    int duration = 100 + (painStrikeLevel - 1) * 20;
                    target.addEffect(new MobEffectInstance(ModEffects.VULNERABILITY, duration, 0, false, true, true));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        Entity attackerEntity = event.getSource().getEntity();
        if (!(attackerEntity instanceof LivingEntity attacker)) return;

        ItemStack weapon = attacker.getMainHandItem();
        if (weapon.isEmpty()) return;

        Holder<Enchantment> ruinHolder = getEnchantmentHolder(attacker, ModEnchantments.RUIN);
        if (ruinHolder == null) return;

        int ruinLevel = EnchantmentHelper.getItemEnchantmentLevel(ruinHolder, weapon);
        if (ruinLevel <= 0) return;

        int armorDmg = 1 + ruinLevel;
        EquipmentSlot[] armorSlots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        EquipmentSlot randomSlot = armorSlots[target.getRandom().nextInt(armorSlots.length)];
        ItemStack armorPiece = target.getItemBySlot(randomSlot);
        if (!armorPiece.isEmpty()) {
            armorPiece.hurtAndBreak(armorDmg, target, randomSlot);
        }

        weapon.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    private static float getHeavyBladeExtraDamage(LivingEntity attacker) {
        float extra = 0.0F;
        MobEffectInstance strength = attacker.getEffect(MobEffects.DAMAGE_BOOST);
        if (strength != null) {
            int amp = strength.getAmplifier();
            extra += 9.0F * (amp + 1);
        }
        MobEffectInstance weakness = attacker.getEffect(MobEffects.WEAKNESS);
        if (weakness != null) {
            int amp = weakness.getAmplifier();
            extra -= 12.0F * (amp + 1);
        }
        return extra;
    }

    private static boolean isCriticalHit(LivingEntity attacker) {
        if (!(attacker instanceof Player player)) return false;
        return player.fallDistance > 0.0F
                && !player.onGround()
                && !player.onClimbable()
                && !player.isInWater()
                && !player.hasEffect(MobEffects.BLINDNESS)
                && !player.isPassenger();
    }

    private static Holder<Enchantment> getEnchantmentHolder(LivingEntity entity, net.minecraft.resources.ResourceKey<Enchantment> key) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            return serverLevel.registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolder(key)
                    .orElse(null);
        }
        return null;
    }
}
