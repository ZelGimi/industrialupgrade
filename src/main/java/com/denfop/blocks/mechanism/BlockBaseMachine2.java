package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.*;
import com.denfop.tiles.mechanism.TilePlasticCreator;
import com.denfop.tiles.mechanism.TilePlasticPlateCreator;
import com.denfop.tiles.mechanism.exp.TileStorageExp;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileDieselGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileHydrogenGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TilePetrolGenerator;
import com.denfop.tiles.mechanism.generator.things.fluid.TileHeliumGenerator;
import com.denfop.tiles.mechanism.generator.things.fluid.TileLavaGenerator;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockBaseMachine2 implements IMultiTileBlock, IMultiBlockItem {

    combiner_matter(TileCombinerMatter.class, 0),
    fisher(TileFisher.class, 1),
    analyzer(TileAnalyzer.class, 2),
    painter(TilePainting.class, 3),
    gen_disel(TileDieselGenerator.class, 4),
    gen_pet(TilePetrolGenerator.class, 5),
    adv_pump(TileAdvPump.class, 6),
    imp_pump(TileImpPump.class, 7),
    expierence_block(TileStorageExp.class, 8),
    gen_hyd(TileHydrogenGenerator.class, 9),
    gen_obsidian(TileObsidianGenerator.class, 10),
    plastic_creator(TilePlasticCreator.class, 11),
    lava_gen(TileLavaGenerator.class, 12),
    plastic_plate_creator(TilePlasticPlateCreator.class, 13),
    helium_generator(TileHeliumGenerator.class, 14),
    electrolyzer_iu(TileElectrolyzer.class, 15),

    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlockBaseMachine2(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
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
        if (this.getTeClass() != null) {
            try {
                this.dummyTe = (TileEntityBlock) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (Exception e) {

            }
        }
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
    public BlockEntityType<? extends TileEntityBlock> getBlockType() {
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
    public Class<? extends TileEntityBlock> getTeClass() {
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
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public boolean hasUniqueRender(final ItemStack itemStack) {
        return false;
    }

}
