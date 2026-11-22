package name.modid.client;

import net.minecraft.world.item.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProjectileUtils {
    private static final Logger log = LogManager.getLogger(ProjectileUtils.class);

    public static boolean isProjectileItem(ItemStack stack) {
        if (stack.isEmpty()) {
            log.info("isProjectileItem returned false: stack {} was empty", stack);
            return false;
        } else {
            Item item = stack.getItem();
            return item != Items.EXPERIENCE_BOTTLE && (item instanceof BowItem
                    || item instanceof CrossbowItem
                    || item instanceof TridentItem
                    || item instanceof SnowballItem
                    || item instanceof EggItem
                    || item instanceof PotionItem
                    || item instanceof EnderpearlItem
                    || item instanceof FireworkRocketItem
                    || item instanceof WindChargeItem);
        }
    }
}
