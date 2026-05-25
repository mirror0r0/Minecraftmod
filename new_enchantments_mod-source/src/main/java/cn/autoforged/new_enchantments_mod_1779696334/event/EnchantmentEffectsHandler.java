package cn.autoforged.new_enchantments_mod_1779696334.event;

import cn.autoforged.new_enchantments_mod_1779696334.NewEnchantmentsMod;
import cn.autoforged.new_enchantments_mod_1779696334.init.ModEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = NewEnchantmentsMod.MOD_ID)
public class EnchantmentEffectsHandler {
    private static boolean handlingWildSelfDamage = false;
    private static final java.util.Map<java.util.UUID, Long> catalysisCooldowns = new java.util.HashMap<>();

    // ==================== 1. Scream - 尖啸 ====================
    @SubscribeEvent
    public static void onScreamHorn(PlayerInteractEvent.RightClickItem event) {
        if (event.getLevel().isClientSide()) return;
        var stack = event.getItemStack();
        if (!stack.is(Items.GOAT_HORN)) return;

        Player player = event.getEntity();
        int level = getEnchantLevel(player, ModEnchantments.SCREAM);
        if (level <= 0) return;

        var level_ = player.level();
        double radius = 16.0;
        var box = player.getBoundingBox().inflate(radius);
        for (var entity : level_.getEntitiesOfClass(LivingEntity.class, box,
                e -> e instanceof Mob mob && mob.isAggressive() && e != player)) {
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0, false, true));
        }
    }

    // ==================== 2. Reckless - 无谋 ====================
    @SubscribeEvent
    public static void onRecklessHit(LivingDamageEvent.Post event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide()) return;

        var source = event.getSource();
        var attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity la)) return;

        int level = getEnchantLevel(la, ModEnchantments.RECKLESS);
        if (level <= 0) return;

        int slowness = switch (level) { case 1 -> 1; case 2 -> 2; default -> 4; };
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, slowness, false, true));
        entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0, false, true));
    }

    // ==================== 3. Feast - 狂宴 ====================
    @SubscribeEvent
    public static void onFeastKill(LivingDeathEvent event) {
        var deceased = event.getEntity();
        if (deceased.level().isClientSide()) return;

        var source = event.getSource();
        var killer = source.getEntity();
        if (!(killer instanceof Player player)) return;

        int level = getEnchantLevel(player, ModEnchantments.FEAST);
        if (level <= 0) return;

        int baseAmplifier = level - 1;
        int saturation = level == 1 ? 6 : 8;

        var existing = player.getEffect(MobEffects.HEALTH_BOOST);
        int amp = (existing != null && existing.getDuration() > 0)
            ? Math.min(existing.getAmplifier() + 1, 4)
            : baseAmplifier;

        player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 400, amp, false, true));
        FoodData fd = player.getFoodData();
        fd.eat(saturation, saturation);
    }

    // ==================== 4. Wild - 狂野 ====================
    @SubscribeEvent
    public static void onWildDamage(LivingIncomingDamageEvent event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide()) return;

        var source = event.getSource();
        var attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity la)) return;

        int level = getEnchantLevel(la, ModEnchantments.WILD);
        if (level <= 0) return;

        float multiplier = switch (level) { case 1 -> 1.4F; case 2 -> 1.66F; default -> 1.8F; };
        event.setAmount(event.getAmount() * multiplier);
    }

    @SubscribeEvent
    public static void onWildSelfDamage(LivingDamageEvent.Post event) {
        if (handlingWildSelfDamage) return;
        handlingWildSelfDamage = true;
        try {
            var entity = event.getEntity();
            if (entity.level().isClientSide()) return;

            var source = event.getSource();
            var attacker = source.getEntity();
            if (!(attacker instanceof LivingEntity la)) return;

            int level = getEnchantLevel(la, ModEnchantments.WILD);
            if (level <= 0) return;

            la.hurt(la.damageSources().magic(), level);
        } finally {
            handlingWildSelfDamage = false;
        }
    }

    // ==================== 5. Bloodbath - 浴血 ====================
    @SubscribeEvent
    public static void onBloodbathHurt(LivingIncomingDamageEvent event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (!(entity instanceof Player player)) return;

        int level = getEnchantLevel(player, ModEnchantments.BLOODBATH);
        if (level <= 0) return;

        player.setAbsorptionAmount(player.getAbsorptionAmount() + level * 2.0F);
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, level - 1, false, true));
    }

    // ==================== 8. Backstab - 背刺 ====================
    @SubscribeEvent
    public static void onBackstabDamage(LivingIncomingDamageEvent event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide()) return;

        var source = event.getSource();
        var attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity la)) return;
        if (!(entity instanceof Mob mob)) return;

        int level = getEnchantLevel(la, ModEnchantments.BACKSTAB);
        if (level <= 0) return;

        if (mob.getTarget() != null && mob.getTarget() == la) return;

        float multiplier = level == 1 ? 2.0F : 2.5F;
        event.setAmount(event.getAmount() * multiplier);

        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, level - 1, false, true));
    }

    // ==================== 9. Agony - 苦痛 ====================
    @SubscribeEvent
    public static void onAgonyHit(LivingDamageEvent.Post event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide()) return;

        var source = event.getSource();
        var attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity la)) return;

        int level = getEnchantLevel(la, ModEnchantments.AGONY);
        if (level <= 0) return;

        int duration = 20 + level * 20;
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, Math.min(level - 1, 3), false, true));
    }

    // ==================== 10. Calamity - 灾祸 ====================
    @SubscribeEvent
    public static void onCalamityDamage(LivingIncomingDamageEvent event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide()) return;

        var source = event.getSource();
        var attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity la)) return;
        if (!(entity instanceof LivingEntity target)) return;

        int level = getEnchantLevel(la, ModEnchantments.CALAMITY);
        if (level <= 0) return;

        var effects = target.getActiveEffects();
        long harmful = effects.stream()
            .filter(e -> !e.getEffect().value().isBeneficial())
            .count();
        if (harmful <= 0) return;

        float base = switch (level) { case 1 -> 1.3F; case 2 -> 1.4F; default -> 1.5F; };
        float per = switch (level) { case 1 -> 1.0F; case 2 -> 1.5F; default -> 2.0F; };
        event.setAmount(event.getAmount() * (base + per * (harmful - 1)));
    }

    // ==================== 11. Catalysis - 催化 ====================
    @SubscribeEvent
    public static void onCatalysisHit(LivingDamageEvent.Post event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (!(entity instanceof LivingEntity target)) return;

        var source = event.getSource();
        var attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity la)) return;

        int level = getEnchantLevel(la, ModEnchantments.CATALYSIS);
        if (level <= 0) return;

        long gameTime = la.level().getGameTime();
        Long lastActive = catalysisCooldowns.get(la.getUUID());
        if (lastActive != null && gameTime - lastActive < 100) return;
        catalysisCooldowns.put(la.getUUID(), gameTime);

        var poison = target.getEffect(MobEffects.POISON);
        if (poison == null) return;

        int oldDur = poison.getDuration();
        int divisor = level == 1 ? 3 : 2;
        int newDur = oldDur - oldDur / divisor;
        int reduced = oldDur - newDur;

        if (reduced > 0) {
            target.hurt(target.damageSources().magic(), reduced / 20.0F);
        }

        target.addEffect(new MobEffectInstance(MobEffects.POISON, newDur, Math.min(poison.getAmplifier() + 1, 4), false, true));
    }

    // ==================== 6. Tenacity - 坚毅 ====================
    // Shield disable cooldown reduction: 100 ticks -> 40/60/72/80
    @SubscribeEvent
    public static void onTenacityShieldBlock(LivingDamageEvent.Post event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (!(entity instanceof Player player)) return;

        if (!player.getCooldowns().isOnCooldown(Items.SHIELD)) return;

        var offhand = player.getOffhandItem();
        var mainhand = player.getMainHandItem();
        int level = 0;
        if (offhand.is(Items.SHIELD)) level = getEnchantLevelInSlot(player, EquipmentSlot.OFFHAND, ModEnchantments.TENACITY);
        else if (mainhand.is(Items.SHIELD)) level = getEnchantLevelInSlot(player, EquipmentSlot.MAINHAND, ModEnchantments.TENACITY);
        if (level <= 0) return;

        int newCooldown = switch (level) { case 1 -> 40; case 2 -> 60; case 3 -> 72; default -> 80; };
        var cdManager = player.getCooldowns();
        cdManager.removeCooldown(Items.SHIELD);
        cdManager.addCooldown(Items.SHIELD, newCooldown);
    }

    // ==================== 7. Ethereal - 幽质 ====================
    // On hit: reduce wearer's armor by 40/70/90% for 20/27/32 ticks
    @SubscribeEvent
    public static void onEtherealHit(LivingIncomingDamageEvent event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (!(entity instanceof LivingEntity target)) return;

        int level = 0;
        for (var slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            int l = getEnchantLevelInSlot(target, slot, ModEnchantments.ETHEREAL);
            if (l > level) level = l;
        }
        if (level <= 0) return;

        int duration = switch (level) { case 1 -> 20; case 2 -> 27; default -> 32; };
        float reduction = switch (level) { case 1 -> 0.4F; case 2 -> 0.7F; default -> 0.9F; };

        if (target.hasEffect(MobEffects.UNLUCK) && target.getEffect(MobEffects.UNLUCK).getAmplifier() >= level) {
            return;
        }

        target.addEffect(new MobEffectInstance(MobEffects.UNLUCK, duration, level, false, true));

        float armor = target.getArmorValue();
        float reducedArmor = armor * (1.0F - reduction);
        float originalReduction = armor * 0.04F;
        float newReduction = reducedArmor * 0.04F;
        float extraDamage = event.getAmount() * (originalReduction - newReduction) / (1.0F - originalReduction);
        if (extraDamage > 0) {
            event.setAmount(event.getAmount() + extraDamage);
        }
    }

    // ==================== Helper ====================
    private static int getEnchantLevel(LivingEntity entity, ResourceKey<Enchantment> key) {
        int max = 0;
        for (var slot : EquipmentSlot.values()) {
            int l = getEnchantLevelInSlot(entity, slot, key);
            if (l > max) max = l;
        }
        return max;
    }

    private static int getEnchantLevelInSlot(LivingEntity entity, EquipmentSlot slot, ResourceKey<Enchantment> key) {
        if (!(entity.level() instanceof ServerLevel sl)) return 0;
        var holder = sl.registryAccess()
            .registryOrThrow(Registries.ENCHANTMENT)
            .getHolder(key).orElse(null);
        if (holder == null) return 0;
        return EnchantmentHelper.getItemEnchantmentLevel(holder, entity.getItemBySlot(slot));
    }
}
