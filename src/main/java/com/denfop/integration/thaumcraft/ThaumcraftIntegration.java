package com.denfop.integration.thaumcraft;


import com.denfop.Constants;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;

public class ThaumcraftIntegration {

    public static Aspect NIGHT;
    public static Aspect MATTERY;
    public static Aspect DAY;
    public static Aspect ENERGY;
    public static BlockTileEntity blockThaumSolarPanel;

    public static void init() {
        blockThaumSolarPanel = TileBlockCreator.instance.create(BlockThaumSolarPanel.class);
        ENERGY = new Aspect("energy", ModUtils.convertRGBcolorToInt(208, 52, 10), new Aspect[]{Aspect.ENERGY, Aspect.FIRE},
                new ResourceLocation(Constants.TEXTURES, "textures/aspect/energy.png"), 771
        );
        DAY = new Aspect(
                "day",
                ModUtils.convertRGBcolorToInt(232, 212, 6),
                new Aspect[]{Aspect.LIFE, Aspect.LIGHT},
                new ResourceLocation(Constants.TEXTURES, "textures/aspect/day.png"),
                771
        );
        NIGHT = new Aspect(
                "night",
                ModUtils.convertRGBcolorToInt(26, 19, 19),
                new Aspect[]{Aspect.ENTROPY, Aspect.VOID},
                new ResourceLocation(Constants.TEXTURES, "textures/aspect/night.png"),
                771
        );
        MATTERY = new Aspect("mattery", ModUtils.convertRGBcolorToInt(178, 5, 199), new Aspect[]{Aspect.LIFE, Aspect.SOUL},
                new ResourceLocation(Constants.TEXTURES, "textures/aspect/materia.png"), 771
        );

    }

}
