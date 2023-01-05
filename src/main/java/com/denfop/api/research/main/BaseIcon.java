package com.denfop.api.research.main;

public class BaseIcon implements Icon {

    private final int x;
    private final int y;
    private final EnumTypeIcon type;

    public BaseIcon(int x, int y, EnumTypeIcon type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public EnumTypeIcon getType() {
        return this.type;
    }

}
