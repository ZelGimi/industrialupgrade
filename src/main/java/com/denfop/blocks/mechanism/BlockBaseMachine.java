package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileNeutronGenerator;
import com.denfop.tiles.mechanism.TileGenerationMicrochip;
import com.denfop.tiles.mechanism.TileGenerationStone;
import com.denfop.tiles.mechanism.TileModuleMachine;
import com.denfop.tiles.mechanism.dual.heat.TileAlloySmelter;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityGeneratorAdv;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityGeneratorImp;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityGeneratorPer;
import com.denfop.tiles.mechanism.generator.things.matter.TileAdvancedMatter;
import com.denfop.tiles.mechanism.generator.things.matter.TileImprovedMatter;
import com.denfop.tiles.mechanism.generator.things.matter.TileUltimateMatter;
import com.denfop.tiles.mechanism.quarry.TileAdvQuantumQuarry;
import com.denfop.tiles.mechanism.quarry.TileImpQuantumQuarry;
import com.denfop.tiles.mechanism.quarry.TilePerQuantumQuarry;
import com.denfop.tiles.mechanism.quarry.TileQuantumQuarry;
import com.denfop.utils.ModUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockBaseMachine implements IMultiTileBlock {

    adv_matter(TileAdvancedMatter.class, 1),
    imp_matter(TileImprovedMatter.class, 2),
    per_matter(TileUltimateMatter.class, 3),
    alloy_smelter(TileAlloySmelter.class, 4),
    neutron_generator(TileNeutronGenerator.class, 5),
    generator_microchip(TileGenerationMicrochip.class, 6),
    gen_stone(TileGenerationStone.class, 7),
    quantum_quarry(TileQuantumQuarry.class, 8),
    modulator(TileModuleMachine.class, 9),
    adv_gen(TileEntityGeneratorAdv.class, 10),
    imp_gen(TileEntityGeneratorImp.class, 11),
    per_gen(TileEntityGeneratorPer.class, 12),
    adv_quantum_quarry(TileAdvQuantumQuarry.class, 13),
    imp_quantum_quarry(TileImpQuantumQuarry.class, 14),
    per_quantum_quarry(TilePerQuantumQuarry.class, 15),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("basemachine");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;

    BlockBaseMachine(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockBaseMachine(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


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
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockBaseMachine block : BlockBaseMachine.values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception e) {

                }
            }
        }
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
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
    @Nonnull
    public ResourceLocation getIdentifier() {
        return BlockBaseMachine.IDENTITY;
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
    public Set<EnumFacing> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 1.0F;
    }

    @Override
    @Nonnull
    public MultiTileBlock.HarvestTool getHarvestTool() {
        return MultiTileBlock.HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public MultiTileBlock.DefaultDrop getDefaultDrop() {
        return MultiTileBlock.DefaultDrop.Machine;
    }

    @Override
    public boolean allowWrenchRotating() {
        return true;
    }

    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }
}
