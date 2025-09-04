package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.item.MultiBlockItem;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.transport.tiles.energycable.*;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.Set;

;

public enum BlockCableEntity implements MultiBlockEntity, MultiBlockItem {

    glass(BlockEntityGlassCable.class, 0),
    glass1(BlockEntityGlass1Cable.class, 1),
    glass2(BlockEntityGlass2Cable.class, 2),
    glass3(BlockEntityGlass3Cable.class, 3),
    glass4(BlockEntityGlass4Cable.class, 4),
    glass5(BlockEntityGlass5Cable.class, 5),
    glass6(BlockEntityGlass6Cable.class, 6),
    glass7(BlockEntityGlass7Cable.class, 7),
    glass8(BlockEntityGlass8Cable.class, 8),
    glass9(BlockEntityGlass9Cable.class, 9),
    glass10(BlockEntityGlass10Cable.class, 10),
    copper(BlockEntityCopperCable.class, 11),
    copper1(BlockEntityCopper1Cable.class, 12),
    glass_1(BlockEntityFiberGlassCable.class, 13),
    gold(BlockEntityGoldCable.class, 14),
    gold1(BlockEntityGold1Cable.class, 15),
    iron(BlockEntityIronCable.class, 16),
    iron1(BlockEntityIron1Cable.class, 17),
    tin(BlockEntityTinCable.class, 18),
    tin1(BlockEntityTin1Cable.class, 19),

    ;


    static String[] name = {"itemcable", "itemcableo", "itemgoldcable", "itemgoldcablei", "itemgoldcableii", "itemironcable", "itemironcablei",
            "itemironcableii",
            "itemironcableiiii"
            , "itemglasscable"
            , "itemglasscablei",
            "itemcoppercable", "itemcoppercable1", "itemglasscable1",
            "itemgold_cable", "itemgold_cable1",
            "itemiron_cable", "itemiron_cable1", "itemtin_cable", "itemtin1_cable"
    };
    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;

    ;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockType;

    BlockCableEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
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
        final ModContainer mc = IUCore.instance.modContainer;
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
    public BlockEntityType<? extends BlockEntityBase> getBlockType() {
        return this.blockType.get();
    }

    @Override
    public void setDefaultState(BlockState blockState) {
        this.defaultState = blockState;
    }

    @Override
    public void setType(DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockEntityType) {
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

    @Override
    public boolean hasUniqueName() {
        return true;
    }

    @Override
    public String getUniqueName() {
        return "iu." + name[this.itemMeta];
    }

}
