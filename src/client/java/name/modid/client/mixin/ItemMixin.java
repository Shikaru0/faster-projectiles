package name.modid.client.mixin;

import name.modid.client.FastUseTracker;
import name.modid.client.ProjectileUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    private static final Logger log = LogManager.getLogger(ItemMixin.class);

    @Inject(at = @At("HEAD"), method = "releaseUsing")
    private void release(ItemStack stack, Level level, LivingEntity livingEntity, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
        log.info("released({}, {}, {}, {}, {})", stack, level, livingEntity, remainingUseTicks, cir);

        if (ProjectileUtils.isProjectileItem(stack)) {
            FastUseTracker.consumeRelease();
            stack.setDamageValue(stack.getDamageValue());
        }
    }

    @Inject(at = @At("HEAD"), method = "onUseTick")
    private void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
        log.info("onUseTick({}, {}, {}, {}, {})", level, livingEntity, stack, remainingUseTicks, ci);
        Item item = stack.getItem();
        if (item != Items.EXPERIENCE_BOTTLE) {
            if (item instanceof BowItem || item instanceof CrossbowItem || item instanceof TridentItem) {
                stack.setDamageValue(stack.getDamageValue());
            }
        }
    }
}