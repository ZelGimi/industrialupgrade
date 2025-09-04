package com.denfop.api.widget;

public enum EnumTypeComponent {
    ENERGY(103, 0, 116, 1, 12, 16, EnumTypeRender.HEIGHT, 0, 1, true),
    AMPERE(84, 175, 84, 194, 162, 20, EnumTypeRender.HEIGHT, 0, 0, true, false, true),
    ENERGY_WEIGHT_2(114, 136, 142, 136, 142 - 114, 10, EnumTypeRender.WIDTH, 0, 0, true, false, true),
    SPACE_PROGRESS(89, 221, 117, 221, 117 - 89, 33, EnumTypeRender.WIDTH, 0, 0, true, false, true),
    ENERGY_WEIGHT(170, 0, 213, 0, 43, 16, EnumTypeRender.WIDTH, 0, 0, true),
    ADVANCED(36, 0, 220, 0, 18, 18, EnumTypeRender.WIDTH, 0, 0, false),
    IMPROVED(18, 0, 202, 0, 18, 18, EnumTypeRender.WIDTH, 0, 0, false),
    PERFECT(0, 0, 184, 0, 18, 18, EnumTypeRender.WIDTH, 0, 0, false),
    PHOTONIC(184, 20, 184, 0, 18, 18, EnumTypeRender.WIDTH, 0, 0, false, true),
    NEXT(189, 197, 238, 0, 208 - 188, 217 - 207, EnumTypeRender.WIDTH, 0, 0, false, true, false, false),
    PREV(189, 208, 238, 0, 208 - 188, 218 - 207, EnumTypeRender.WIDTH, 0, 0, true, true, false, false),

    DEFAULT(54, 0, 238, 0, 18, 18, EnumTypeRender.WIDTH, 0, 0, false),
    STEAM_DEFAULT(1, 1, 238, 0, 18, 18, EnumTypeRender.WIDTH, 0, 0, false, false, true),
    STEAM_FLUID(1, 20, 238, 0, 18, 18, EnumTypeRender.WIDTH, 0, 0, true, false, true),
    BIO_DEFAULT(1, 1, 238, 0, 18, 18, EnumTypeRender.WIDTH, 0, 0, false, false, false, true),
    BIO_FLUID(1, 20, 238, 0, 18, 18, EnumTypeRender.WIDTH, 0, 0, true, false, false, true),
    SPACE_DEFAULT(1, 1, 238, 0, 18, 18, EnumTypeRender.WIDTH, 0, 0, false, false, false, false, true),
    SPACE_FLUID(1, 20, 238, 0, 18, 18, EnumTypeRender.WIDTH, 0, 0, true, false, false, false, true),
    SPACE_MULTI_PROCESS(48, 16, 66, 16, 16, 24, EnumTypeRender.WIDTH, 0, 0, true, false, false, false, true),


    MULTI_PROCESS(102, 54, 23, 50, 16, 24, EnumTypeRender.WIDTH, 0, 0, true),
    STEAM_MULTI_PROCESS(48, 16, 66, 16, 16, 24, EnumTypeRender.WIDTH, 0, 0, true, false, true),
    BIO_MULTI_PROCESS(48, 16, 66, 16, 16, 24, EnumTypeRender.WIDTH, 0, 0, true, false, false, true),
    COLD(132, 0, 137, 0, 4, 16, EnumTypeRender.HEIGHT, 0, 0, true),
    HEAT(170, 34, 213, 34, 43, 16, EnumTypeRender.WIDTH, 0, 0, true),
    PROGRESS3(170, 17, 213, 17, 43, 16, EnumTypeRender.WIDTH, 0, 0, true),
    PROGRESS4(179, 101, 179, 85, 15, 16, EnumTypeRender.WIDTH, 0, 0, true),
    CIRCLE_BAR(190, 118, 221, 118, 32, 32, EnumTypeRender.UNIQUE, 0, 0, false, true),
    EXP(132, 0, 147, 0, 4, 16, EnumTypeRender.HEIGHT, 0, 0, true),
    FLUID_PART(132, 24, 132, 24, 154 - 132, 17, EnumTypeRender.WIDTH, 0, 0, false),
    FLUID_PART1(143, 46, 132, 24, 18, 13, EnumTypeRender.WIDTH, 0, 0, false),
    FLUID_PART2(142, 61, 132, 24, 25, 13, EnumTypeRender.WIDTH, 0, 0, false),
    FLUID_PART3(25, 102, 132, 24, 36, 13, EnumTypeRender.WIDTH, 0, 0, false),
    FLUID_PART4(58, 118, 132, 24, 13, 36, EnumTypeRender.WIDTH, 0, 0, false),
    FLUID_PART5(80, 114, 132, 24, 13, 135 - 113, EnumTypeRender.WIDTH, 0, 0, false),
    FLUID_PART6(87, 141, 132, 24, 13, 17, EnumTypeRender.WIDTH, 0, 0, false),
    FLUID_PART7(0, 163, 132, 24, 0, 3, EnumTypeRender.WIDTH, 0, 0, false),
    FLUID_PART9(122, 193, 132, 24, 3, 0, EnumTypeRender.HEIGHT, 0, 0, false),
    FLUID_PART8(218, 148, 132, 24, 36, 14, EnumTypeRender.WIDTH, 0, 0, false),

    EMPTY(2, 103, 132, 24, 16, 16, EnumTypeRender.WIDTH, 0, 0, false),
    HYDRO(2, 122, 132, 24, 16, 16, EnumTypeRender.WIDTH, 0, 0, false),
    WIND(2, 141, 132, 24, 19, 16, EnumTypeRender.WIDTH, 0, 0, false),
    CHANGEMODE(146, 79, 132, 24, 163 - 145, 96 - 78, EnumTypeRender.WIDTH, 0, 0, false),


    WATER(132, 0, 142, 0, 4, 16, EnumTypeRender.HEIGHT, 0, 0, true),
    SOLARIUM_ENERGY_WEIGHT(170, 51, 213, 51, 43, 16, EnumTypeRender.WIDTH, 0, 0, true),
    NIGHT_ENERGY_WEIGHT(112, 114, 155, 114, 43, 17, EnumTypeRender.WIDTH, 0, 0, true, false, true),

    QUANTUM_ENERGY_WEIGHT(170, 68, 213, 68, 43, 16, EnumTypeRender.WIDTH, 0, 0, true),
    COOL_ENERGY_WEIGHT(170, 68, 213, 68, 43, 16, EnumTypeRender.WIDTH, 0, 0, true),
    SOUND_BUTTON(48, 58, 64, 58, 15, 15, EnumTypeRender.WIDTH, 0, 0, true),
    WORK_BUTTON(0, 79, 0, 57, 21, 21, EnumTypeRender.WIDTH, 0, 0, true),
    PLUS_BUTTON(22, 61, 0, 57, 12, 12, EnumTypeRender.WIDTH, 0, 0, true),
    MINUS_BUTTON(35, 61, 0, 57, 12, 12, EnumTypeRender.WIDTH, 0, 0, true),
    PROCESS(104, 17, 104, 35, 22, 16, EnumTypeRender.WIDTH, 0, 0, true),
    STEAM_PROCESS(48, 1, 71, 1, 22, 16, EnumTypeRender.WIDTH, 0, 0, true, false, true),
    STEAM_PROCESS2(42, 87, 65, 87, 22, 16, EnumTypeRender.WIDTH, 0, 0, true, false, true),
    PROCESS1(103, 108, 121, 108, 16, 22, EnumTypeRender.HEIGHT, 0, 0, true),
    PROCESS2(29, 135, 29, 119, 22, 16, EnumTypeRender.WIDTH, 0, 0, true),
    OIL(24, 78, 34, 78, 10, 15, EnumTypeRender.HEIGHT, 0, 0, true),
    UPGRADE(82, 91, 34, 78, 11, 14, EnumTypeRender.WIDTH, 0, 0, true),
    ANTIUPGRADE(82, 75, 34, 78, 11, 14, EnumTypeRender.WIDTH, 0, 0, true),
    NO(85, 60, 34, 78, 11, 11, EnumTypeRender.WIDTH, 0, 0, true),


    LEFT(84, 172, 239, 98, 9, 18, EnumTypeRender.WIDTH, 0, 0, true),
    RIGHT(95, 172, 239, 98, 9, 18, EnumTypeRender.WIDTH, 0, 0, true),
    TOP(108, 171, 239, 98, 18, 9, EnumTypeRender.WIDTH, 0, 0, true),
    DOWN(108, 182, 239, 98, 18, 9, EnumTypeRender.WIDTH, 0, 0, true),
    ACCEPT(128, 240, 239, 98, 16, 16, EnumTypeRender.WIDTH, 0, 0, true, true),
    CANCEL(145, 240, 239, 98, 16, 16, EnumTypeRender.WIDTH, 0, 0, true, true),
    SCROLL_UP(189, 221, 239, 98, 16, 16, EnumTypeRender.WIDTH, 0, 0, true, true),
    SCROLL_DOWN(206, 221, 239, 98, 16, 16, EnumTypeRender.WIDTH, 0, 0, true, true),

    BLACK(128, 222, 239, 98, 16, 16, EnumTypeRender.WIDTH, 0, 0, true, true),
    WHITE(145, 222, 239, 98, 16, 16, EnumTypeRender.WIDTH, 0, 0, true, true),
    REDSTONE(170, 221, 239, 98, 17, 17, EnumTypeRender.WIDTH, 0, 0, true, true),

    RAD(170, 17, 213, 17, 43, 16, EnumTypeRender.WIDTH, 0, 0, true),
    RAD_1(195, 88, 195, 102, 60, 15, EnumTypeRender.WIDTH, 0, 0, true),
    ENERGY_WEIGHT_1(215, 116, 215, 130, 183 - 143, 191 - 178, EnumTypeRender.WIDTH, 0, 0, true),
    ENERGY_HEIGHT(183, 119, 197, 119, 191 - 178, 183 - 143, EnumTypeRender.HEIGHT, 0, 0, true),
    ENERGY_HEIGHT_1(219, 104, 237, 104, 237 - 219, 145 - 104, EnumTypeRender.HEIGHT, 0, 0, true, false, true),
    STEAM_ENERGY_HEIGHT(94, 1, 94, 21, 167 - 94, 19, EnumTypeRender.HEIGHT, 0, 0, true, false, true),

    QUANTUM_ENERGY_HEIGHT(1, 1, 21, 1, 18, 72, EnumTypeRender.HEIGHT, 0, 0, true, true),
    BUTTON(1, 75, 1, 97, 200, 20, EnumTypeRender.WIDTH, 0, 0, true, true),
    FIRE(177, 159, 196, 159, 15, 174 - 159, EnumTypeRender.HEIGHT, 0, 0, true),
    GAS(65, 74, 65, 91, 16, 174 - 159, EnumTypeRender.HEIGHT, 0, 0, true),

    BUTTON1(45, 1, 1, 97, 16, 16, EnumTypeRender.WIDTH, 0, 0, true, true),
    FRAME(63, 1, 1, 97, 24, 24, EnumTypeRender.WIDTH, 0, 0, true, true),
    EXP_BUTTON(183, 1, 1, 97, 17, 16, EnumTypeRender.WIDTH, 0, 0, true, true),
    BIG_FRAME(97, 1, 1, 97, 55, 54, EnumTypeRender.WIDTH, 0, 0, true, true),
    EXP_HEIGHT(160, 1, 167, 1, 6, 49, EnumTypeRender.HEIGHT, 0, 0, true, true),
    QUANTUM_HEIGHT1(160, 1, 174, 1, 6, 49, EnumTypeRender.HEIGHT, 0, 0, true, true),
    SOLARIUM_MAKER(1, 167, 18, 167, 15, 15, EnumTypeRender.HEIGHT, 0, 0, true, true),
    BIGGEST_FRAME(205, 2, 1, 97, 252 - 205, 75, EnumTypeRender.WIDTH, 0, 0, true, true),
    INFO(67, 30, 1, 97, 18, 18, EnumTypeRender.WIDTH, 0, 0, true, true),
    LASER_PROCESS(0, 183, 0, 199, 29, 16, EnumTypeRender.WIDTH, 0, 0, true, true),
    FACTORY_PROCESS(0, 142, 0, 154, 24, 12, EnumTypeRender.WIDTH, 0, 0, true, true),
    STAMP_PROCESS(1, 239, 33, 239, 32, 15, EnumTypeRender.WIDTH, 0, 0, true, true),
    NIGHT_PROCESS(1, 216, 16, 216, 15, 23, EnumTypeRender.HEIGHT, 0, 0, true, true),
    RADIATION_PROCESS(32, 216, 49, 216, 16, 23, EnumTypeRender.HEIGHT, 0, 0, true, true),
    ENCHANT_PROCESS(67, 236, 95, 236, 93 - 67, 253 - 236, EnumTypeRender.WIDTH, 0, 0, true, true),
    POSITRONS(170, 239, 212, 239, 43, 17, EnumTypeRender.WIDTH, 0, 0, true, true),
    FLUIDS_SLOT(45, 30, 45, 30, 18, 18, EnumTypeRender.WIDTH, 0, 0, true, true),
    NULL(255, 255, 132, 24, 0, 0, EnumTypeRender.WIDTH, 0, 0, false),
    DEFAULT_REACTOR(185, 166, 185, 166, 9, 9, EnumTypeRender.WIDTH, 0, 0, false, true),
    ADV_REACTOR(197, 166, 185, 166, 9, 9, EnumTypeRender.WIDTH, 0, 0, false, true),
    IMP_REACTOR(209, 166, 185, 166, 9, 9, EnumTypeRender.WIDTH, 0, 0, false, true),
    PER_REACTOR(221, 166, 185, 166, 9, 9, EnumTypeRender.WIDTH, 0, 0, false, true),
    FLUID_REACTOR(185, 177, 185, 166, 9, 9, EnumTypeRender.WIDTH, 0, 0, false, true),
    GAS_REACTOR(197, 177, 185, 166, 9, 9, EnumTypeRender.WIDTH, 0, 0, false, true),
    GRAPHITE_REACTOR(209, 177, 185, 166, 9, 9, EnumTypeRender.WIDTH, 0, 0, false, true),
    HEAT_REACTOR(221, 177, 185, 166, 9, 9, EnumTypeRender.WIDTH, 0, 0, false, true),
    CHECK_MARK(220, 189, 185, 166, 10, 11, EnumTypeRender.WIDTH, 0, 0, false, true),
    NONE(0, 0, 0, 0, 1, 1, EnumTypeRender.WIDTH, 0, 0, false);

    private final int x;
    private final int y;
    private final int x1;
    private final int y1;
    private final int weight;
    private final int height;
    private final EnumTypeRender render;
    private final int endX;
    private final int endY;
    private final boolean hasDescription;
    private final boolean nextBar;
    private final boolean steam;
    private final boolean space;
    private boolean bio = false;


    EnumTypeComponent(
            int x,
            int y,
            int x1,
            int y1,
            int weight,
            int height,
            EnumTypeRender render,
            int endX,
            int endY,
            boolean hasDescription, boolean nextBar
    ) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.weight = weight;
        this.height = height;
        this.render = render;
        this.endX = endX;
        this.endY = endY;
        this.hasDescription = hasDescription;
        this.nextBar = nextBar;
        this.steam = false;
        space = false;
    }

    EnumTypeComponent(
            int x,
            int y,
            int x1,
            int y1,
            int weight,
            int height,
            EnumTypeRender render,
            int endX,
            int endY,
            boolean hasDescription, boolean nextBar, boolean steam
    ) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.weight = weight;
        this.height = height;
        this.render = render;
        this.endX = endX;
        this.endY = endY;
        this.hasDescription = hasDescription;
        this.nextBar = nextBar;
        this.steam = steam;
        space = false;
    }

    EnumTypeComponent(
            int x,
            int y,
            int x1,
            int y1,
            int weight,
            int height,
            EnumTypeRender render,
            int endX,
            int endY,
            boolean hasDescription, boolean nextBar, boolean steam, boolean bio
    ) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.weight = weight;
        this.height = height;
        this.render = render;
        this.endX = endX;
        this.endY = endY;
        this.hasDescription = hasDescription;
        this.nextBar = nextBar;
        this.steam = steam;
        this.bio = bio;
        space = false;
    }

    EnumTypeComponent(
            int x,
            int y,
            int x1,
            int y1,
            int weight,
            int height,
            EnumTypeRender render,
            int endX,
            int endY,
            boolean hasDescription, boolean nextBar, boolean steam, boolean bio, boolean space
    ) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.weight = weight;
        this.height = height;
        this.render = render;
        this.endX = endX;
        this.endY = endY;
        this.hasDescription = hasDescription;
        this.nextBar = nextBar;
        this.steam = steam;
        this.bio = bio;
        this.space = space;
    }

    EnumTypeComponent(
            int x,
            int y,
            int x1,
            int y1,
            int weight,
            int height,
            EnumTypeRender render,
            int endX,
            int endY,
            boolean hasDescription
    ) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.weight = weight;
        this.height = height;
        this.render = render;
        this.endX = endX;
        this.endY = endY;
        this.hasDescription = hasDescription;
        nextBar = false;
        this.steam = false;
        space = false;
    }

    public boolean isNextBar() {
        return nextBar;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public boolean isHasDescription() {
        return hasDescription;
    }

    public EnumTypeRender getRender() {
        return render;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getX() {
        return x;
    }

    public int getX1() {
        return x1;
    }

    public int getY() {
        return y;
    }

    public int getY1() {
        return y1;
    }

    public boolean isSteam() {
        return steam;
    }

    public boolean isBio() {
        return bio;
    }

    public boolean isSpace() {
        return space;
    }
}
