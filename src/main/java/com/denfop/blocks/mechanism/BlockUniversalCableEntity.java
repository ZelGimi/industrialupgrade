package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.item.MultiBlockItem;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.transport.tiles.universal_cable.*;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
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

public enum BlockUniversalCableEntity implements MultiBlockEntity, MultiBlockItem {

    universal(BlockEntityGlassUniversalCable.class, 0),
    universal1(BlockEntityGlassUniversalCable1.class, 1),
    universal2(BlockEntityGlassUniversalCable2.class, 2),
    universal3(BlockEntityGlassUniversalCable3.class, 3),
    universal4(BlockEntityGlassUniversalCable4.class, 4),
    universal5(BlockEntityGlassUniversalCable5.class, 5),
    universal6(BlockEntityGlassUniversalCable6.class, 6),
    universal7(BlockEntityGlassUniversalCable7.class, 7),
    universal8(BlockEntityGlassUniversalCable8.class, 8),
    universal9(BlockEntityGlassUniversalCable9.class, 9),
    universal10(BlockEntityGlassUniversalCable10.class, 10),

    ;


    private static final String[] name = {
            "itemuniversalcable",
            "itemuniversalccableo",
            "itemuniversalcgoldcable",
            "itemuniversalcgoldcablei",
            "itemuniversalcgoldcableii",
            "itemuniversalcironcable",
            "itemuniversalcironcablei",
            "itemuniversalcironcableii",
            "itemuniversalcironcableiiii"
            , "itemuniversalcglasscable"
            , "itemuniversalcglasscablei"};
    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;

    ;
    private RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockUniversalCableEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
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
        return "iu.universal_cable." + name[this.itemMeta];
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
