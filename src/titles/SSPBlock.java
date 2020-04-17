// 
// Decompiled by Procyon v0.5.36
// 

package com.Denfop.ssp.tiles;

import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.Loader;
import ic2.core.util.Util;
import net.minecraft.util.EnumFacing;
import java.util.Set;
import ic2.core.ref.TeBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.EnumRarity;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.ITeBlock;

public enum SSPBlock implements ITeBlock
{
    spectral_solar_panel((Class<? extends TileEntityBlock>)TileEntitySpectral.class, 2), 
    singular_solar_panel((Class<? extends TileEntityBlock>)TileEntitySingular.class, 3, EnumRarity.RARE), 
    admin_solar_panel((Class<? extends TileEntityBlock>)TileEntityAdmin.class, 4, EnumRarity.EPIC), 
    photonic_solar_panel((Class<? extends TileEntityBlock>)TileEntityphotonic.class, 5, EnumRarity.EPIC);
    
    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;
    private static final SSPBlock[] VALUES;
    public static final ResourceLocation IDENTITY;
    
    private SSPBlock(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);
    }
    
    private SSPBlock(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;
        GameRegistry.registerTileEntity((Class)teClass, "suoer_solar_panels:" + this.getName());
    }
    
    public boolean hasItem() {
        return true;
    }
    
    public String getName() {
        return this.name();
    }
    
    public int getId() {
        return this.itemMeta;
    }
    
    public ResourceLocation getIdentifier() {
        return SSPBlock.IDENTITY;
    }
    
    public Class<? extends TileEntityBlock> getTeClass() {
        return this.teClass;
    }
    
    
    
    public float getHardness() {
        return 3.0f;
    }
    
    public float getExplosionResistance() {
        return 15.0f;
    }
    
    public TeBlock.HarvestTool getHarvestTool() {
        return TeBlock.HarvestTool.Pickaxe;
    }
    
    public TeBlock.DefaultDrop getDefaultDrop() {
        return TeBlock.DefaultDrop.Self;
    }
    
    public boolean allowWrenchRotating() {
        return false;
    }
    
    public Set<EnumFacing> getSupportedFacings() {
        return (Set<EnumFacing>)Util.horizontalFacings;
    }
    
    public EnumRarity getRarity() {
        return this.rarity;
    }
    
    public static void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !"super_solar_panels".equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final SSPBlock block : SSPBlock.VALUES) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = (TileEntityBlock)block.teClass.newInstance();
                }
                catch (Exception e) {
                    if (Util.inDev()) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }
    
    static {
        VALUES = values();
        IDENTITY = new ResourceLocation("super_solar_panels", "machines");
    }

	@Override
	public boolean hasActive() {
		// TODO Auto-generated method stub
		return false;
	}
}
