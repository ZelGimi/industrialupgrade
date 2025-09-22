package com.denfop.blocks;

import com.denfop.Constants;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.utils.ModUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.Set;

public enum MultiTileBlock implements IMultiTileBlock {
    invalid(
            null
    );
    public static final MultiTileBlock[] values = values();
    public static final ResourceLocation loc = new ResourceLocation(Constants.MOD_ID, "te");
    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final boolean hasActive;
    private final Set<EnumFacing> supportedFacings;
    private final boolean allowWrenchRotating;
    private final HarvestTool harvestTool;
    private final DefaultDrop defaultDrop;
    private final float hardness;
    int idBlock;
    private TileEntityBlock dummyTe;

    MultiTileBlock(
            Class<? extends TileEntityBlock> teClass
    ) {
        this.teClass = teClass;
        this.itemMeta = -1;
        this.hasActive = false;
        this.supportedFacings = ModUtils.noFacings;
        this.allowWrenchRotating = false;
        this.harvestTool = HarvestTool.None;
        this.defaultDrop = DefaultDrop.None;
        this.hardness = 5;


    }

    public int getIDBlock() {
        return idBlock;
    }

    ;

    public void setIdBlock(int id) {
        idBlock = id;
    }

    ;

    public void buildDummies() {
        ModContainer mc = Loader.instance().activeModContainer();
        if (mc != null && (Constants.MOD_ID).equals(mc.getModId())) {

            for (MultiTileBlock block : values) {
                if (block.teClass != null) {
                    try {
                        block.dummyTe = block.teClass.newInstance();
                    } catch (Exception var6) {

                    }
                }
            }

        } else {
            throw new IllegalAccessError("Don't mess with this please.");
        }
    }


    public boolean hasItem() {
        return this.teClass != null && this.itemMeta != -1;
    }

    public String getName() {
        return this.name();
    }

    public ResourceLocation getIdentifier() {
        return loc;
    }

    public Class<? extends TileEntityBlock> getTeClass() {
        return this.teClass;
    }

    public boolean hasActive() {
        return this.hasActive;
    }

    public int getId() {
        return this.itemMeta;
    }

    public float getHardness() {
        return this.hardness;
    }

    public HarvestTool getHarvestTool() {
        return this.harvestTool;
    }

    public DefaultDrop getDefaultDrop() {
        return this.defaultDrop;
    }

    public boolean allowWrenchRotating() {
        return this.allowWrenchRotating;
    }

    public Set<EnumFacing> getSupportedFacings() {
        return this.supportedFacings;
    }


    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }

    public enum DefaultDrop {
        Self,
        None,
        Generator,
        Machine,
        AdvMachine;

        DefaultDrop() {
        }
    }

    public enum HarvestTool {
        None("", -1),
        Pickaxe("pickaxe", 0),
        Shovel("shovel", 0),
        Axe("axe", 0),
        Wrench("wrench", 0);

        public final String toolClass;
        public final int level;

        HarvestTool(String toolClass, int level) {
            this.toolClass = toolClass;
            this.level = level;
        }
    }


}
