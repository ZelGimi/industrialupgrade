// 
// Decompiled by Procyon v0.5.36
// 

package com.Denfop.ssp.tiles;
import com.Denfop.ssp.*;
import com.Denfop.ssp.items.*;
import com.Denfop.ssp.tiles.airpanel.*;
import com.Denfop.ssp.tiles.earthpanel.*;
import com.Denfop.ssp.tiles.endpanel.*;
import com.Denfop.ssp.tiles.firepanel.*;
import com.Denfop.ssp.tiles.rainpanels.*;
import com.Denfop.ssp.tiles.Moonpanel.*;
import com.Denfop.ssp.tiles.neutronfabricator.*;
import com.Denfop.ssp.tiles.overtimepanel.*;
import com.Denfop.ssp.tiles.Sunpanel.*;
import com.Denfop.ssp.tiles.Transformer.*;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.Loader;
import ic2.core.util.Util;
import net.minecraft.util.EnumFacing;
import java.util.Set;
import ic2.core.ref.TeBlock;
import ic2.core.ref.TeBlock.DefaultDrop;
import ic2.core.ref.TeBlock.HarvestTool;
import ic2.core.ref.TeBlock.ITePlaceHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import ic2.core.block.TileEntityBlock;
import ic2.core.IC2;
import ic2.core.block.ITeBlock;

public enum SSPBlock implements ITeBlock

{
	
    advanced_solar_panel((Class<? extends TileEntityBlock>)TileEntityAdvancedSolar.class, 1), 
    hybrid_solar_panel((Class<? extends TileEntityBlock>)TileEntityHybridSolar.class, 2, EnumRarity.RARE), 
    ultimate_solar_panel((Class<? extends TileEntityBlock>)TileEntityUltimateHybridSolar.class, 3, EnumRarity.EPIC), 
    quantum_solar_panel((Class<? extends TileEntityBlock>)TileEntityQuantumSolar.class, 4, EnumRarity.EPIC),
    spectral_solar_panel((Class<? extends TileEntityBlock>)TileEntitySpectral.class, 5), 
    singular_solar_panel((Class<? extends TileEntityBlock>)TileEntitySingular.class, 6, EnumRarity.RARE), 
    admin_solar_panel((Class<? extends TileEntityBlock>)TileEntityAdmin.class, 7, EnumRarity.EPIC), 
    photonic_solar_panel((Class<? extends TileEntityBlock>)TileEntityphotonic.class, 8, EnumRarity.EPIC),
    neutronium_solar_panel((Class<? extends TileEntityBlock>)TileEntityneutron.class, 9, EnumRarity.EPIC),
	nutronfabricator((Class<? extends TileEntityBlock>)TileEntityMassFabricator.class, 10),
	proton_solar_panel((Class<? extends TileEntityBlock>)TileEntityproton.class, 11, EnumRarity.EPIC),
	transformator((Class<? extends TileEntityBlock>)TileEntityTransformerEV.class, 12),
	transformator1((Class<? extends TileEntityBlock>)TileEntityTransformerEV1.class, 13),
	transformator2((Class<? extends TileEntityBlock>)TileEntityTransformerEV2.class, 14),
	transformator3((Class<? extends TileEntityBlock>)TileEntityTransformerEV3.class, 15),
	  advanced_solar_panelsun((Class<? extends TileEntityBlock>)TileEntityAdvancedSolarsun.class, 16), 
	    hybrid_solar_panelsun((Class<? extends TileEntityBlock>)TileEntityHybridSolarsun.class, 17, EnumRarity.RARE), 
	    ultimate_solar_panelsun((Class<? extends TileEntityBlock>)TileEntityUltimateHybridSolarsun.class, 18, EnumRarity.EPIC), 
	    quantum_solar_panelsun((Class<? extends TileEntityBlock>)TileEntityQuantumSolarsun.class, 19, EnumRarity.EPIC),
	    spectral_solar_panelsun((Class<? extends TileEntityBlock>)TileEntitySpectralsun.class, 20), 
	    proton_solar_panelsun((Class<? extends TileEntityBlock>)TileEntityprotonsun.class, 50), 
	    singular_solar_panelsun((Class<? extends TileEntityBlock>)TileEntitySingularsun.class, 21, EnumRarity.RARE), 
	    admin_solar_panelsun((Class<? extends TileEntityBlock>)TileEntityAdminsun.class, 22, EnumRarity.EPIC), 
	    photonic_solar_panelsun((Class<? extends TileEntityBlock>)TileEntityphotonicsun.class, 23, EnumRarity.EPIC),
	    neutronium_solar_panelsun((Class<? extends TileEntityBlock>)TileEntityneutronsun.class, 24, EnumRarity.EPIC),
	    advanced_solar_panelmoon((Class<? extends TileEntityBlock>)TileEntityAdvancedSolarmoon.class, 26), 
	    hybrid_solar_panelmoon((Class<? extends TileEntityBlock>)TileEntityHybridSolarmoon.class, 27, EnumRarity.RARE), 
	    ultimate_solar_panelmoon((Class<? extends TileEntityBlock>)TileEntityUltimateHybridSolarmoon.class, 28, EnumRarity.EPIC), 
	    quantum_solar_panelmoon((Class<? extends TileEntityBlock>)TileEntityQuantumSolarmoon.class, 29, EnumRarity.EPIC),
	    spectral_solar_panelmoon((Class<? extends TileEntityBlock>)TileEntitySpectralmoon.class, 30), 
	    singular_solar_panelmoon((Class<? extends TileEntityBlock>)TileEntitySingularmoon.class, 31, EnumRarity.RARE), 
	    admin_solar_panelmoon((Class<? extends TileEntityBlock>)TileEntityAdminmoon.class, 32, EnumRarity.EPIC), 
	    photonic_solar_panelmoon((Class<? extends TileEntityBlock>)TileEntityphotonicmoon.class, 33, EnumRarity.EPIC),
	    neutronium_solar_panelmoon((Class<? extends TileEntityBlock>)TileEntityneutronmoon.class, 34, EnumRarity.EPIC),
	    proton_solar_panelmoon((Class<? extends TileEntityBlock>)TileEntityprotonmoon.class, 48, EnumRarity.EPIC),
	//
	   
	    proton_solar_panelrain((Class<? extends TileEntityBlock>)TileEntityprotonrain.class, 49, EnumRarity.EPIC),
	    molecular_transformer((Class)TileEntityMolecularAssembler.class, 25, EnumRarity.RARE),
	  advanced_solar_panelrain((Class<? extends TileEntityBlock>)TileEntityAdvancedSolarrain.class, 36), 
	    hybrid_solar_panelrain((Class<? extends TileEntityBlock>)TileEntityHybridSolarrain.class, 37, EnumRarity.RARE), 
	    ultimate_solar_panelrain((Class<? extends TileEntityBlock>)TileEntityUltimateHybridSolarrain.class, 38, EnumRarity.EPIC), 
	    quantum_solar_panelrain((Class<? extends TileEntityBlock>)TileEntityQuantumSolarrain.class, 39, EnumRarity.EPIC),
	    spectral_solar_panelrain((Class<? extends TileEntityBlock>)TileEntitySpectralrain.class, 40), 
	    singular_solar_panelrain((Class<? extends TileEntityBlock>)TileEntitySingularrain.class, 41, EnumRarity.RARE), 
	    admin_solar_panelrain((Class<? extends TileEntityBlock>)TileEntityAdminrain.class, 42, EnumRarity.EPIC), 
	    photonic_solar_panelrain((Class<? extends TileEntityBlock>)TileEntityphotonicrain.class, 43, EnumRarity.EPIC),
	    neutronium_solar_panelrain((Class<? extends TileEntityBlock>)TileEntityneutronrain.class, 44, EnumRarity.EPIC),
        advancedmfsu((Class<? extends TileEntityBlock>)advancedmfsu.class, 45, EnumRarity.EPIC),
        ultimatemfsu((Class<? extends TileEntityBlock>)ultimatemfsu.class, 46, EnumRarity.EPIC),
	quantummfsu((Class<? extends TileEntityBlock>)quantummfsu.class, 47, EnumRarity.EPIC),
	//
	adminair((Class<? extends TileEntityBlock>)TileEntityAdminair.class, 70, EnumRarity.EPIC), 
	advancedsolarair((Class<? extends TileEntityBlock>)TileEntityAdvancedSolarair.class, 51, EnumRarity.EPIC), 
	hybridsolarair((Class<? extends TileEntityBlock>)TileEntityHybridSolarair.class, 52, EnumRarity.EPIC), 
	neutronair((Class<? extends TileEntityBlock>)TileEntityneutronair.class, 53, EnumRarity.EPIC), 
	photonicair((Class<? extends TileEntityBlock>)TileEntityphotonicair.class, 54, EnumRarity.EPIC), 
	protonair((Class<? extends TileEntityBlock>)TileEntityprotonair.class, 55, EnumRarity.EPIC), 
	quantumsolarair((Class<? extends TileEntityBlock>)TileEntityQuantumSolarair.class, 56, EnumRarity.EPIC), 
	singularair((Class<? extends TileEntityBlock>)TileEntitySingularair.class, 57, EnumRarity.EPIC), 
	spectralair((Class<? extends TileEntityBlock>)TileEntitySpectralair.class, 58, EnumRarity.EPIC), 
	ultimatehybridsolarair((Class<? extends TileEntityBlock>)TileEntityUltimateHybridSolarair.class, 59, EnumRarity.EPIC), 
	adminearth((Class<? extends TileEntityBlock>)TileEntityAdminearth.class, 60, EnumRarity.EPIC), 
	advancedsolarearth((Class<? extends TileEntityBlock>)TileEntityAdvancedSolarearth.class, 61, EnumRarity.EPIC), 
	hybridsolarearth((Class<? extends TileEntityBlock>)TileEntityHybridSolarearth.class, 62, EnumRarity.EPIC), 
	neutronearth((Class<? extends TileEntityBlock>)TileEntityneutronearth.class, 63, EnumRarity.EPIC), 
	photonicearth((Class<? extends TileEntityBlock>)TileEntityphotonicearth.class, 64, EnumRarity.EPIC), 
	protonearth((Class<? extends TileEntityBlock>)TileEntityprotonearth.class, 65, EnumRarity.EPIC), 
	quantumsolarearth((Class<? extends TileEntityBlock>)TileEntityQuantumSolarearth.class, 66, EnumRarity.EPIC), 
	singularearth((Class<? extends TileEntityBlock>)TileEntitySingularearth.class, 67, EnumRarity.EPIC), 
	spectralearth((Class<? extends TileEntityBlock>)TileEntitySpectralearth.class, 68, EnumRarity.EPIC), 
	ultimatehybridsolarearth((Class<? extends TileEntityBlock>)TileEntityUltimateHybridSolarearth.class, 69, EnumRarity.EPIC);
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
        GameRegistry.registerTileEntity((Class)teClass, "super_solar_panels:" + this.getName());
       
        
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
        return 0.0f;
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
