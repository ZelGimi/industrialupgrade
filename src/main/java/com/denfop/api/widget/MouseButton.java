package com.denfop.api.widget;

public enum MouseButton {
    left(0),
    right(1);

    private static final MouseButton[] map = createMap();
    public final int id;

    MouseButton(int id) {
        this.id = id;
    }

    public static MouseButton get(int id) {
        return id >= 0 && id < map.length ? map[id] : null;
    }

    private static MouseButton[] createMap() {
        MouseButton[] values = values();
        int max = -1;
        MouseButton[] ret = values;
        int var3 = values.length;

        int var4;
        for (var4 = 0; var4 < var3; ++var4) {
            MouseButton button = ret[var4];
            if (button.id > max) {
                max = button.id;
            }
        }

        if (max < 0) {
            return new MouseButton[0];
        } else {
            ret = new MouseButton[max + 1];
            var4 = values.length;

            for (int var8 = 0; var8 < var4; ++var8) {
                MouseButton button = values[var8];
                ret[button.id] = button;
            }

            return ret;
        }
    }
}
