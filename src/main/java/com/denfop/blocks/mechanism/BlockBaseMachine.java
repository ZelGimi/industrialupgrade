package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.tiles.base.TileEntityNeutronGenerator;
import com.denfop.tiles.mechanism.TileEntityGenerationMicrochip;
import com.denfop.tiles.mechanism.TileEntityGenerationStone;
import com.denfop.tiles.mechanism.TileEntityModuleMachine;
import com.denfop.tiles.mechanism.dual.heat.TileEntityAlloySmelter;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityGeneratorAdv;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityGeneratorImp;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityGeneratorPer;
import com.denfop.tiles.mechanism.generator.things.matter.TileEntityAdvancedMatter;
import com.denfop.tiles.mechanism.generator.things.matter.TileEntityImprovedMatter;
import com.denfop.tiles.mechanism.generator.things.matter.TileEntityUltimateMatter;
import com.denfop.tiles.mechanism.quarry.TileEntityAdvQuantumQuarry;
import com.denfop.tiles.mechanism.quarry.TileEntityImpQuantumQuarry;
import com.denfop.tiles.mechanism.quarry.TileEntityPerQuantumQuarry;
import com.denfop.tiles.mechanism.quarry.TileEntityQuantumQuarry;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.ref.IC2Material;
import ic2.core.ref.TeBlock;
import ic2.core.util.Util;
import net.minecraft.block.material.Material;
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

public enum BlockBaseMachine implements ITeBlock {

    adv_matter(TileEntityAdvancedMatter.class, 1),
    imp_matter(TileEntityImprovedMatter.class, 2),
    per_matter(TileEntityUltimateMatter.class, 3),
    alloy_smelter(TileEntityAlloySmelter.class, 4),
    neutron_generator(TileEntityNeutronGenerator.class, 5),
    generator_microchip(TileEntityGenerationMicrochip.class, 6),
    gen_stone(TileEntityGenerationStone.class, 7),
    quantum_quarry(TileEntityQuantumQuarry.class, 8),
    modulator(TileEntityModuleMachine.class, 9),
    adv_gen(TileEntityGeneratorAdv.class, 10),
    imp_gen(TileEntityGeneratorImp.class, 11),
    per_gen(TileEntityGeneratorPer.class, 12),
    adv_quantum_quarry(TileEntityAdvQuantumQuarry.class, 13),
    imp_quantum_quarry(TileEntityImpQuantumQuarry.class, 14),
    per_quantum_quarry(TileEntityPerQuantumQuarry.class, 15),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("basemachine");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
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

    public static void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockBaseMachine block : BlockBaseMachine.values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception e) {
                    if (Util.inDev()) {
                        e.printStackTrace();
                    }
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
        return Util.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
    }

    @Override
    public float getExplosionResistance() {
        return 0.0f;
    }

    @Override
    public Material getMaterial() {
        return IC2Material.MACHINE;
    }

    @Override
    @Nonnull
    public TeBlock.HarvestTool getHarvestTool() {
        return TeBlock.HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public TeBlock.DefaultDrop getDefaultDrop() {
        return TeBlock.DefaultDrop.Machine;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity() {
        return this.rarity;
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
