package name.modid.client.mixin;

import name.modid.client.FastUseTracker;
import name.modid.client.FasterProjectilesClient;
import name.modid.client.ProjectileUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    private static final Logger log = LogManager.getLogger(GameRendererMixin.class);
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(at = @At("HEAD"), method = "render")
    private void render(DeltaTracker deltaTracker, boolean tick, CallbackInfo info) {
        if (FasterProjectilesClient.shouldLog) {
            log.info("render({}, {}, {})", deltaTracker, tick, info);
        }
        if (this.minecraft.player != null && this.minecraft.level != null) {
            Player player = this.minecraft.player;
            if (FastUseTracker.rightMousePressed) {
                ItemStack held = player.getMainHandItem();
                if (ProjectileUtils.isProjectileItem(held)) {
                    if (!player.isUsingItem()) {
                        assert this.minecraft.gameMode != null;
                        this.minecraft.gameMode.useItem(player, InteractionHand.OFF_HAND);
                        if (FasterProjectilesClient.shouldLog) {
                            log.info("useItem({}, {})", player, InteractionHand.OFF_HAND);
                        }
                    }

                    FastUseTracker.consumePress();
                }
            }

            if (FastUseTracker.rightMouseJustReleased) {
                if (player.isUsingItem()) {
                    ItemStack active = player.getActiveItem();
                    if (ProjectileUtils.isProjectileItem(active)) {
                        assert this.minecraft.gameMode != null;
                        this.minecraft.gameMode.releaseUsingItem(player);
                        if (FasterProjectilesClient.shouldLog) {
                            log.info("releaseUsingItem({})", player);
                        }
                    }
                }

                FastUseTracker.consumeRelease();
            }
        }
    }
}