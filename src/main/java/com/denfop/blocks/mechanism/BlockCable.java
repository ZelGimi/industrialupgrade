package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.transport.tiles.energycable.*;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockCable implements IMultiTileBlock, IMultiBlockItem {

    glass(TileEntityGlassCable.class, 0),
    glass1(TileEntityGlass1Cable.class, 1),
    glass2(TileEntityGlass2Cable.class, 2),
    glass3(TileEntityGlass3Cable.class, 3),
    glass4(TileEntityGlass4Cable.class, 4),
    glass5(TileEntityGlass5Cable.class, 5),
    glass6(TileEntityGlass6Cable.class, 6),
    glass7(TileEntityGlass7Cable.class, 7),
    glass8(TileEntityGlass8Cable.class, 8),
    glass9(TileEntityGlass9Cable.class, 9),
    glass10(TileEntityGlass10Cable.class, 10),
    copper(TileEntityCopperCable.class, 11),
    copper1(TileEntityCopper1Cable.class, 12),
    glass_1(TileEntityFiberGlassCable.class, 13),
    gold(TileEntityGoldCable.class, 14),
    gold1(TileEntityGold1Cable.class, 15),
    iron(TileEntityIronCable.class, 16),
    iron1(TileEntityIron1Cable.class, 17),
    tin(TileEntityTinCable.class, 18),
    tin1(TileEntityTin1Cable.class, 19),

    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockType;

    ;
  static   String[] name = {"itemcable", "itemcableo", "itemgoldсable", "itemgoldcablei", "itemgoldcableii", "itemironcable", "itemironcablei",
            "itemironcableii",
            "itemironcableiiii"
            , "itemglasscable"
            , "itemglasscablei",
            "itemcoppercable", "itemcoppercable1", "itemglasscable1",
            "itemgold_cable", "itemgold_cable1",
            "itemiron_cable", "itemiron_cable1", "itemtin_cable", "itemtin1_cable"
    };
    BlockCable(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;


    }

    ;

    public int getIDBlock() {
        return idBlock;
    }

    public void setIdBlock(int id) {
        idBlock = id;
    }

    public void buildDummies() {
        final ModContainer mc = ModLoadingContext.get().getActiveContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        if (this.teClass != null) {
            try {
                this.dummyTe = (TileEntityBlock) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public BlockEntityType<? extends TileEntityBlock> getBlockType() {
        return this.blockType.get();
    }

    @Override
    public void setDefaultState(BlockState blockState) {
        this.defaultState = blockState;
    }

    @Override
    public void setType(RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockEntityType) {
        this.blockType = blockEntityType;
    }


    @Override
    public MapColor getMaterial() {
        return CABLE;
    }

    @Override
    public String getName() {
        return this.name();
    }


    @Override
    public String getMainPath() {
        return "wiring";
    }

    @Override
    public int getId() {
        return this.itemMeta;
    }


    @Override
    public boolean hasItem() {
        return true;
    }

    @Override
    public Class<? extends TileEntityBlock> getTeClass() {
        return this.teClass;
    }

    @Override
    public boolean hasActive() {
        return false;
    }

    @Override
    @Nonnull
    public Set<Direction> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 0.5f;
    }

    @Override
    @Nonnull
    public HarvestTool getHarvestTool() {
        return HarvestTool.None;
    }

    @Override
    @Nonnull
    public DefaultDrop getDefaultDrop() {
        return DefaultDrop.Self;
    }

    @Override
    public boolean allowWrenchRotating() {
        return true;
    }

    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }


    @Override
    public boolean hasUniqueRender(ItemStack var1) {
        return true;
    }

    @Override
    public boolean hasUniqueName() {
        return true;
    }

    @Override
    public String getUniqueName() {
        return "iu." + name[this.itemMeta];
    }

}
