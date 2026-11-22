package name.modid.client;

public class FastUseTracker {
    public static volatile boolean rightMousePressed = false;
    public static volatile boolean rightMouseJustReleased = false;

    public static void onMouseButton(boolean pressed) {
        if (pressed) {
            rightMousePressed = true;
            rightMouseJustReleased = false;
        } else {
            rightMousePressed = false;
            rightMouseJustReleased = true;
        }
    }

    public static void consumePress() { rightMousePressed = false; }
    public static void consumeRelease() { rightMouseJustReleased = false; }
}
