package name.modid.client.mixin;

import name.modid.client.FastUseTracker;
import net.minecraft.client.MouseHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.platform.Window;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    private static final Logger log = LogManager.getLogger(MouseHandlerMixin.class);

    @Inject(at = @At("TAIL"), method = "setup")
    private void onSetup(Window window, CallbackInfo info) {
        log.info("onSetup({}, {})", window, info);
        long handle = ((WindowAccessor) (Object) window).getHandle();
        GLFWMouseButtonCallbackI previous = GLFW.glfwSetMouseButtonCallback(handle, null);
        GLFW.glfwSetMouseButtonCallback(handle, (win, button, action, mods) -> {
            if (button == 1) {
                FastUseTracker.onMouseButton(action == 1);
                log.info("onMouseButton({})", action);
            }

            if (previous != null) {
                previous.invoke(win, button, action, mods);
                log.info("invoked previous({}, {}, {}, {})", win, button, action, mods);
            }
        });
    }
}