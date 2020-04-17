package com.Denfop.ssp.items;

import java.util.Locale;
import ic2.core.block.state.IIdProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import ic2.core.init.BlocksItems;
import net.minecraft.util.ResourceLocation;
import ic2.core.ref.ItemName;
import ic2.core.item.ItemMulti;

public class CraftingThings extends ItemMulti<com.chocohead.advsolar.items.ItemCraftingThings.CraftingTypes>
{
    protected static final String NAME = "crafting";
    
    public CraftingThings() {
        super((ItemName)null, (Class)CraftingTypes.class);
        ((CraftingThings)BlocksItems.registerItem((Item)this, new ResourceLocation("super_solar_panels", "crafting"))).setUnlocalizedName("crafting");
    }
    
    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation((Item)this, meta, new ModelResourceLocation("super_solar_panels:crafting/" + CraftingTypes.getFromID(meta).getName(), (String)null));
    }
    
    public String func_77658_a() {
        return "super_solar_panels." + super.getUnlocalizedName().substring(4);
    }
    
    public enum CraftingTypes implements IIdProvider
    {
    	enderquantumcomponent(0), 
    	solarsplitter(1), 
    	bluecomponent(2), 
        greencomponent(3), 
        redcomponent(4),
        singularcore(5),
        spectralcore(6),
        photoniy_ingot(7),
        photoniy(8);
        
        private final String name;
        private final int ID;
        private static final CraftingTypes[] VALUES;
        
        private CraftingTypes(final int ID) {
            this.name = this.name().toLowerCase(Locale.ENGLISH);
            this.ID = ID;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getId() {
            return this.ID;
        }
        
        public static CraftingTypes getFromID(final int ID) {
            return CraftingTypes.VALUES[ID % CraftingTypes.VALUES.length];
        }
        
        static {
            VALUES = values();
        }
    }
}
