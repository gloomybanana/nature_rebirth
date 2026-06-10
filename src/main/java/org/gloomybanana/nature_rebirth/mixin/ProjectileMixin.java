package org.gloomybanana.nature_rebirth.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class ProjectileMixin {

    @Inject(method = "onHit", at = @At("HEAD"))
    private void onHit(HitResult result, CallbackInfo ci) {
        if (!org.gloomybanana.nature_rebirth.Config.EGG_SPAWN_EGG.get()) {
            return;
        }

        Projectile projectile = (Projectile) (Object) this;
        if (projectile.getType() != EntityType.EGG) {
            return;
        }

        if (result.getType() != HitResult.Type.ENTITY) {
            return;
        }

        Entity entity = ((EntityHitResult) result).getEntity();
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        Level level = projectile.level();
        if (level.isClientSide()) {
            return;
        }

        // 计算击杀概率：血量为1时100%，每超过1点降低10%
        float health = livingEntity.getHealth();
        if (health <= 0) return;

        // 基础100%概率，每点生命值减少10%
        double killChance = 1.0 - (health - 1) * 0.1;
        killChance = Math.max(0.0, killChance);

        // 随机判定
        if (level.getRandom().nextDouble() >= killChance) {
            return;
        }

        // 获取对应生物的刷怪蛋
        ItemStack spawnEgg = getSpawnEgg(livingEntity.getType());
        if (spawnEgg.isEmpty()) {
            return;
        }

        try {
            ServerLevel serverLevel = (ServerLevel) level;
            BlockPos pos = livingEntity.blockPosition();

            // 先保存位置
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;

            // 消灭生物
            livingEntity.discard();

            // 生成刷怪蛋物品
            ItemEntity itemEntity = new ItemEntity(serverLevel, x, y, z, spawnEgg);
            serverLevel.addFreshEntity(itemEntity);

            // 播放音效
            serverLevel.playSound(null, BlockPos.containing(x, y, z), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.NEUTRAL, 1.0f, 1.0f);
        } catch (Exception e) {
            // 忽略任何异常，防止崩溃
        }
    }

    // 获取生物对应的刷怪蛋
    private ItemStack getSpawnEgg(EntityType<?> type) {
        if (type == EntityType.ZOMBIE) return new ItemStack(Items.ZOMBIE_SPAWN_EGG);
        if (type == EntityType.SKELETON) return new ItemStack(Items.SKELETON_SPAWN_EGG);
        if (type == EntityType.CREEPER) return new ItemStack(Items.CREEPER_SPAWN_EGG);
        if (type == EntityType.SPIDER) return new ItemStack(Items.SPIDER_SPAWN_EGG);
        if (type == EntityType.CAVE_SPIDER) return new ItemStack(Items.CAVE_SPIDER_SPAWN_EGG);
        if (type == EntityType.ENDERMAN) return new ItemStack(Items.ENDERMAN_SPAWN_EGG);
        if (type == EntityType.WITHER_SKELETON) return new ItemStack(Items.WITHER_SKELETON_SPAWN_EGG);
        if (type == EntityType.STRAY) return new ItemStack(Items.STRAY_SPAWN_EGG);
        if (type == EntityType.PIGLIN) return new ItemStack(Items.PIGLIN_SPAWN_EGG);
        if (type == EntityType.PIGLIN_BRUTE) return new ItemStack(Items.PIGLIN_BRUTE_SPAWN_EGG);
        if (type == EntityType.HOGLIN) return new ItemStack(Items.HOGLIN_SPAWN_EGG);
        if (type == EntityType.ZOGLIN) return new ItemStack(Items.ZOGLIN_SPAWN_EGG);
        if (type == EntityType.PIG) return new ItemStack(Items.PIG_SPAWN_EGG);
        if (type == EntityType.SHEEP) return new ItemStack(Items.SHEEP_SPAWN_EGG);
        if (type == EntityType.COW) return new ItemStack(Items.COW_SPAWN_EGG);
        if (type == EntityType.CHICKEN) return new ItemStack(Items.CHICKEN_SPAWN_EGG);
        if (type == EntityType.SQUID) return new ItemStack(Items.SQUID_SPAWN_EGG);
        if (type == EntityType.GLOW_SQUID) return new ItemStack(Items.GLOW_SQUID_SPAWN_EGG);
        if (type == EntityType.WOLF) return new ItemStack(Items.WOLF_SPAWN_EGG);
        if (type == EntityType.CAT) return new ItemStack(Items.CAT_SPAWN_EGG);
        if (type == EntityType.OCELOT) return new ItemStack(Items.OCELOT_SPAWN_EGG);
        if (type == EntityType.HORSE) return new ItemStack(Items.HORSE_SPAWN_EGG);
        if (type == EntityType.DONKEY) return new ItemStack(Items.DONKEY_SPAWN_EGG);
        if (type == EntityType.MULE) return new ItemStack(Items.MULE_SPAWN_EGG);
        if (type == EntityType.LLAMA) return new ItemStack(Items.LLAMA_SPAWN_EGG);
        if (type == EntityType.RABBIT) return new ItemStack(Items.RABBIT_SPAWN_EGG);
        if (type == EntityType.FOX) return new ItemStack(Items.FOX_SPAWN_EGG);
        if (type == EntityType.BEE) return new ItemStack(Items.BEE_SPAWN_EGG);
        if (type == EntityType.GOAT) return new ItemStack(Items.GOAT_SPAWN_EGG);
        if (type == EntityType.VILLAGER) return new ItemStack(Items.VILLAGER_SPAWN_EGG);
        if (type == EntityType.WANDERING_TRADER) return new ItemStack(Items.WANDERING_TRADER_SPAWN_EGG);
        if (type == EntityType.IRON_GOLEM) return new ItemStack(Items.IRON_GOLEM_SPAWN_EGG);
        if (type == EntityType.SNOW_GOLEM) return new ItemStack(Items.SNOW_GOLEM_SPAWN_EGG);
        if (type == EntityType.SHULKER) return new ItemStack(Items.SHULKER_SPAWN_EGG);
        if (type == EntityType.ENDERMITE) return new ItemStack(Items.ENDERMITE_SPAWN_EGG);
        if (type == EntityType.GUARDIAN) return new ItemStack(Items.GUARDIAN_SPAWN_EGG);
        if (type == EntityType.ELDER_GUARDIAN) return new ItemStack(Items.ELDER_GUARDIAN_SPAWN_EGG);
        if (type == EntityType.BLAZE) return new ItemStack(Items.BLAZE_SPAWN_EGG);
        if (type == EntityType.GHAST) return new ItemStack(Items.GHAST_SPAWN_EGG);
        if (type == EntityType.MAGMA_CUBE) return new ItemStack(Items.MAGMA_CUBE_SPAWN_EGG);
        if (type == EntityType.SLIME) return new ItemStack(Items.SLIME_SPAWN_EGG);
        if (type == EntityType.VEX) return new ItemStack(Items.VEX_SPAWN_EGG);
        if (type == EntityType.VINDICATOR) return new ItemStack(Items.VINDICATOR_SPAWN_EGG);
        if (type == EntityType.EVOKER) return new ItemStack(Items.EVOKER_SPAWN_EGG);
        if (type == EntityType.WITCH) return new ItemStack(Items.WITCH_SPAWN_EGG);
        if (type == EntityType.PHANTOM) return new ItemStack(Items.PHANTOM_SPAWN_EGG);
        if (type == EntityType.DROWNED) return new ItemStack(Items.DROWNED_SPAWN_EGG);
        if (type == EntityType.HUSK) return new ItemStack(Items.HUSK_SPAWN_EGG);
        if (type == EntityType.ZOMBIE_VILLAGER) return new ItemStack(Items.ZOMBIE_VILLAGER_SPAWN_EGG);
        if (type == EntityType.POLAR_BEAR) return new ItemStack(Items.POLAR_BEAR_SPAWN_EGG);
        if (type == EntityType.PANDA) return new ItemStack(Items.PANDA_SPAWN_EGG);
        if (type == EntityType.PARROT) return new ItemStack(Items.PARROT_SPAWN_EGG);
        if (type == EntityType.DOLPHIN) return new ItemStack(Items.DOLPHIN_SPAWN_EGG);
        if (type == EntityType.TURTLE) return new ItemStack(Items.TURTLE_SPAWN_EGG);
        if (type == EntityType.COD) return new ItemStack(Items.COD_SPAWN_EGG);
        if (type == EntityType.SALMON) return new ItemStack(Items.SALMON_SPAWN_EGG);
        if (type == EntityType.PUFFERFISH) return new ItemStack(Items.PUFFERFISH_SPAWN_EGG);
        if (type == EntityType.TROPICAL_FISH) return new ItemStack(Items.TROPICAL_FISH_SPAWN_EGG);
        if (type == EntityType.AXOLOTL) return new ItemStack(Items.AXOLOTL_SPAWN_EGG);
        if (type == EntityType.ALLAY) return new ItemStack(Items.ALLAY_SPAWN_EGG);
        if (type == EntityType.FROG) return new ItemStack(Items.FROG_SPAWN_EGG);
        if (type == EntityType.TADPOLE) return new ItemStack(Items.TADPOLE_SPAWN_EGG);
        if (type == EntityType.WARDEN) return new ItemStack(Items.WARDEN_SPAWN_EGG);
        if (type == EntityType.SNIFFER) return new ItemStack(Items.SNIFFER_SPAWN_EGG);
        if (type == EntityType.CAMEL) return new ItemStack(Items.CAMEL_SPAWN_EGG);

        return ItemStack.EMPTY;
    }
}
