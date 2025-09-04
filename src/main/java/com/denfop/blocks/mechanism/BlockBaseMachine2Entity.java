package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.item.MultiBlockItem;
import com.denfop.blockentity.base.*;
import com.denfop.blockentity.mechanism.BlockEntityPlasticCreator;
import com.denfop.blockentity.mechanism.BlockEntityPlasticPlateCreator;
import com.denfop.blockentity.mechanism.exp.BlockEntityStorageExp;
import com.denfop.blockentity.mechanism.generator.energy.fluid.BlockEntityDieselGenerator;
import com.denfop.blockentity.mechanism.generator.energy.fluid.BlockEntityHydrogenGenerator;
import com.denfop.blockentity.mechanism.generator.energy.fluid.BlockEntityPetrolGenerator;
import com.denfop.blockentity.mechanism.generator.things.fluid.BlockEntityHeliumGenerator;
import com.denfop.blockentity.mechanism.generator.things.fluid.BlockEntityLavaGenerator;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.Set;

;

public enum BlockBaseMachine2Entity implements MultiBlockEntity, MultiBlockItem {

    combiner_matter(BlockEntityCombinerMatter.class, 0),
    fisher(BlockEntityFisher.class, 1),
    analyzer(BlockEntityAnalyzer.class, 2),
    painter(BlockEntityPainting.class, 3),
    gen_disel(BlockEntityDieselGenerator.class, 4),
    gen_pet(BlockEntityPetrolGenerator.class, 5),
    adv_pump(BlockEntityAdvPump.class, 6),
    imp_pump(BlockEntityImpPump.class, 7),
    expierence_block(BlockEntityStorageExp.class, 8),
    gen_hyd(BlockEntityHydrogenGenerator.class, 9),
    gen_obsidian(BlockEntityObsidianGenerator.class, 10),
    plastic_creator(BlockEntityPlasticCreator.class, 11),
    lava_gen(BlockEntityLavaGenerator.class, 12),
    plastic_plate_creator(BlockEntityPlasticPlateCreator.class, 13),
    helium_generator(BlockEntityHeliumGenerator.class, 14),
    electrolyzer_iu(BlockEntityElectrolyzer.class, 15),

    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockBaseMachine2Entity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
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
        if (this.getTeClass() != null) {
            try {
                this.dummyTe = (BlockEntityBase) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (Exception e) {

            }
        }
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
    public BlockEntityType<? extends BlockEntityBase> getBlockType() {
        return this.blockType.get();
    }

    @Override
    public String getMainPath() {
        return "basemachine2";
    }

    @Override
    public String getName() {
        return this.name();
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
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    @Nonnull
    public Set<Direction> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 1.0F;
    }

    @Override
    @Nonnull
    public HarvestTool getHarvestTool() {
        return HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public DefaultDrop getDefaultDrop() {
        return DefaultDrop.Machine;
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
    public boolean hasUniqueRender(final ItemStack itemStack) {
        return false;
    }

}
