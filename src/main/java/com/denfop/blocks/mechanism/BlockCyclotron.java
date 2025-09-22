package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.cyclotron.TileEntityCyclotronCasing;
import com.denfop.tiles.cyclotron.TileEntityCyclotronChamber;
import com.denfop.tiles.cyclotron.TileEntityCyclotronController;
import com.denfop.tiles.cyclotron.TileEntityCyclotronCoolant;
import com.denfop.tiles.cyclotron.TileEntityCyclotronCryogen;
import com.denfop.tiles.cyclotron.TileEntityCyclotronElectrostaticDeflector;
import com.denfop.tiles.cyclotron.TileEntityCyclotronParticleAccelerator;
import com.denfop.tiles.cyclotron.TileEntityCyclotronPositrons;
import com.denfop.tiles.cyclotron.TileEntityCyclotronQuantum;
import com.denfop.utils.ModUtils;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockCyclotron implements IMultiTileBlock {

    cyclotron_controller(TileEntityCyclotronController.class, 0),
    cyclotron_positrons(TileEntityCyclotronPositrons.class, 1),
    cyclotron_cryogen(TileEntityCyclotronCryogen.class, 2),
    cyclotron_coolant(TileEntityCyclotronCoolant.class, 3),
    cyclotron_bombardment_chamber(TileEntityCyclotronChamber.class, 4),
    cyclotron_quantum(TileEntityCyclotronQuantum.class, 5),
    cyclotron_casing(TileEntityCyclotronCasing.class, 6),
    cyclotron_particle_accelerator(TileEntityCyclotronParticleAccelerator.class, 7),
    cyclotron_electrostatic_deflector(TileEntityCyclotronElectrostaticDeflector.class, 8),
    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("cyclotron");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;

    BlockCyclotron(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockCyclotron(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
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

    @Override
    public Material getMaterial() {
        return Material.IRON;
    }

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockCyclotron block : BlockCyclotron.values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception e) {

                }
            }
        }
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
        return IDENTITY;
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
        return 3.0f;
    }

    @Override
    @Nonnull
    public MultiTileBlock.HarvestTool getHarvestTool() {
        return MultiTileBlock.HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public MultiTileBlock.DefaultDrop getDefaultDrop() {
        return MultiTileBlock.DefaultDrop.Self;
    }

    @Override
    public boolean allowWrenchRotating() {
        return false;
    }

    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }
}
