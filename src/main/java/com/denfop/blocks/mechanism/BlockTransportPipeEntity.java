package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.item.MultiBlockItem;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.transport.tiles.transport.*;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockTransportPipeEntity implements MultiBlockEntity, MultiBlockItem {

    itemcable(BlockEntityTransportPipe.class, 0),
    itemcable1(BlockEntityTransportPipe1.class, 1),
    itemcable2(BlockEntityTransportPipe2.class, 2),
    itemcable3(BlockEntityTransportPipe3.class, 3),
    itemcable4(BlockEntityTransportPipe4.class, 4),
    itemcable5(BlockEntityTransportPipe5.class, 5),
    itemcable6(BlockEntityTransportPipe6.class, 6),
    itemcable7(BlockEntityTransportPipe7.class, 7),
    itemcable8(BlockEntityTransportPipe8.class, 8),
    itemcable9(BlockEntityTransportPipe9.class, 9),
    itemcable10(BlockEntityTransportPipe10.class, 10),
    itemcable11(BlockEntityTransportPipe11.class, 11),
    itemcable12(BlockEntityTransportPipe12.class, 12),
    itemcable13(BlockEntityTransportPipe13.class, 13),
    itemcable14(BlockEntityTransportPipe14.class, 14),
    itemcable15(BlockEntityTransportPipe15.class, 15),
    itemcable16(BlockEntityTransportPipe16.class, 16),
    itemcable17(BlockEntityTransportPipe17.class, 17),
    itemcable18(BlockEntityTransportPipe18.class, 18),
    itemcable19(BlockEntityTransportPipe19.class, 19),
    itemcable20(BlockEntityTransportPipe20.class, 20),
    itemcable21(BlockEntityTransportPipe21.class, 21),
    itemcable22(BlockEntityTransportPipe22.class, 22),
    itemcable23(BlockEntityTransportPipe23.class, 23),
    itemcable24(BlockEntityTransportPipe24.class, 24),
    itemcable25(BlockEntityTransportPipe25.class, 25),
    itemcable26(BlockEntityTransportPipe26.class, 26),
    itemcable27(BlockEntityTransportPipe27.class, 27),
    itemcable28(BlockEntityTransportPipe28.class, 28),
    itemcable29(BlockEntityTransportPipe29.class, 29),
    ;


    static String[] name = {
            "itempipes", "itempipes1", "itempipes2", "itempipes3", "itempipes4", "itempipes5",
            "itempipes6", "itempipes7", "itempipes8", "itempipes9", "itempipes10", "itempipes11",
            "itempipes12", "itempipes13", "itempipes14", "itempipes15", "itempipes16", "itempipes17",
            "itempipes18", "itempipes19", "itempipes20", "itempipes21", "itempipes22", "itempipes23",
            "itempipes24", "itempipes25", "itempipes26", "itempipes27", "itempipes28", "itempipes29"
    };
    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;

    ;
    private RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockTransportPipeEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;


    }

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
                this.dummyTe = (BlockEntityBase) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public boolean hasUniqueName() {
        return true;
    }

    @Override
    public String getUniqueName() {
        return "iu." + name[this.itemMeta] + "_transport";
    }

    @Override
    public BlockEntityType<? extends BlockEntityBase> getBlockType() {
        return this.blockType.get();
    }

    @Override
    public void setDefaultState(BlockState blockState) {
        this.defaultState = blockState;
    }

    @Override
    public void setType(RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockEntityType) {
        this.blockType = blockEntityType;
    }


    @Override
    public Material getMaterial() {
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
    public Class<? extends BlockEntityBase> getTeClass() {
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
    public BlockEntityBase getDummyTe() {
        return this.dummyTe;
    }


    @Override
    public boolean hasUniqueRender(ItemStack var1) {
        return true;
    }


}
