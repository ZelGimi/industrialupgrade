package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.lightning_rod.TileEntityLightningRodAntennaMast;
import com.denfop.tiles.lightning_rod.TileEntityLightningRodCasing;
import com.denfop.tiles.lightning_rod.TileEntityLightningRodController;
import com.denfop.tiles.lightning_rod.TileEntityLightningRodGrounding;
import com.denfop.tiles.lightning_rod.TileEntityLightningRodPoles;
import com.denfop.tiles.lightning_rod.TileEntityLightningRodReceiver;
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

public enum BlockLightningRod implements IMultiTileBlock {

    lightning_rod_controller(TileEntityLightningRodController.class, 0),
    lightning_rod_casing(TileEntityLightningRodCasing.class, 1),
    lightning_rod_grounding(TileEntityLightningRodGrounding.class, 2),
    lightning_rod_poles(TileEntityLightningRodPoles.class, 3),
    lightning_rod_antenna_mast(TileEntityLightningRodAntennaMast.class, 4),
    lightning_rod_receiver(TileEntityLightningRodReceiver.class, 5);


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("lightning_rod");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;

    BlockLightningRod(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockLightningRod(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
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
        for (final BlockLightningRod block : values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception ignored) {

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
        return true;
    }

    @Override
    @Nonnull
    public Set<EnumFacing> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 1.0f;
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
