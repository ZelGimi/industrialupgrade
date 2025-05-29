package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.transport.tiles.transport.*;
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

public enum BlockTransportPipe implements IMultiTileBlock, IMultiBlockItem {

    itemcable(TileEntityTransportPipe.class, 0),
    itemcable1(TileEntityTransportPipe1.class, 1),
    itemcable2(TileEntityTransportPipe2.class, 2),
    itemcable3(TileEntityTransportPipe3.class, 3),
    itemcable4(TileEntityTransportPipe4.class, 4),
    itemcable5(TileEntityTransportPipe5.class, 5),
    itemcable6(TileEntityTransportPipe6.class, 6),
    itemcable7(TileEntityTransportPipe7.class, 7),
    itemcable8(TileEntityTransportPipe8.class, 8),
    itemcable9(TileEntityTransportPipe9.class, 9),
    itemcable10(TileEntityTransportPipe10.class, 10),
    itemcable11(TileEntityTransportPipe11.class, 11),
    itemcable12(TileEntityTransportPipe12.class, 12),
    itemcable13(TileEntityTransportPipe13.class, 13),
    itemcable14(TileEntityTransportPipe14.class, 14),
    itemcable15(TileEntityTransportPipe15.class, 15),
    itemcable16(TileEntityTransportPipe16.class, 16),
    itemcable17(TileEntityTransportPipe17.class, 17),
    itemcable18(TileEntityTransportPipe18.class, 18),
    itemcable19(TileEntityTransportPipe19.class, 19),
    itemcable20(TileEntityTransportPipe20.class, 20),
    itemcable21(TileEntityTransportPipe21.class, 21),
    itemcable22(TileEntityTransportPipe22.class, 22),
    itemcable23(TileEntityTransportPipe23.class, 23),
    itemcable24(TileEntityTransportPipe24.class, 24),
    itemcable25(TileEntityTransportPipe25.class, 25),
    itemcable26(TileEntityTransportPipe26.class, 26),
    itemcable27(TileEntityTransportPipe27.class, 27),
    itemcable28(TileEntityTransportPipe28.class, 28),
    itemcable29(TileEntityTransportPipe29.class, 29),
    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlockTransportPipe(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
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
    public boolean hasUniqueName() {
        return true;
    }
    static       String[] name = {
            "itempipes", "itempipes1", "itempipes2", "itempipes3", "itempipes4", "itempipes5",
            "itempipes6", "itempipes7", "itempipes8", "itempipes9", "itempipes10", "itempipes11",
            "itempipes12", "itempipes13", "itempipes14", "itempipes15", "itempipes16", "itempipes17",
            "itempipes18", "itempipes19", "itempipes20", "itempipes21", "itempipes22", "itempipes23",
            "itempipes24", "itempipes25", "itempipes26", "itempipes27", "itempipes28", "itempipes29"
    };


    @Override
    public String getUniqueName() {
        return "iu." + name[this.itemMeta]+ "_transport";
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


}
