// 
// Decompiled by Procyon v0.5.36
// 

package com.Denfop.ssp.tiles;

import com.chocohead.advsolar.tiles.TileEntitySolarPanel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntityAdmin extends TileEntitySolarPanel
{
    public static TileEntitySolarPanel.SolarConfig settings;
    
    public TileEntityAdmin() {
        super(TileEntityAdmin.settings);
    }

	
}
