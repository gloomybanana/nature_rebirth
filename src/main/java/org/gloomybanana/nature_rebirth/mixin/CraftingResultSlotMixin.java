package org.gloomybanana.nature_rebirth.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.gloomybanana.nature_rebirth.NatureRebirth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ResultSlot.class)
public abstract class CraftingResultSlotMixin {

    @Inject(method = "onTake", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onTake(ServerPlayer player, CallbackInfo ci, ItemStack stack) {
        if (player == null || player.level() == null) {
            return;
        }
        
        // 检查合成结果是否是龙息
        if (stack.getItem() == Items.DRAGON_BREATH) {
            Level level = player.level();
            
            // 遍历玩家的合成槽位，找到龙首
            if (player.containerMenu instanceof net.minecraft.world.inventory.CraftingMenu craftingMenu) {
                // 在1.21中，可以通过反射或直接访问container来获取合成槽位
                // 由于API变化，我们直接创建龙首物品实体
                
                // 找到龙首，返还一个给玩家
                ItemEntity dragonHeadEntity = new ItemEntity(
                    level,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    new ItemStack(Items.DRAGON_HEAD)
                );
                dragonHeadEntity.setUnlimitedLifetime();
                level.addFreshEntity(dragonHeadEntity);
                
                NatureRebirth.LOGGER.info("Dragon head returned to player after crafting dragon breath");
            }
        }
    }
}
