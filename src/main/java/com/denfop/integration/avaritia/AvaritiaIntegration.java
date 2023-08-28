package com.denfop.integration.avaritia;

import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.integration.de.IUDEItem;
import net.minecraft.item.Item;

public class AvaritiaIntegration {

    public static BlockTileEntity blockAvSolarPanel;
    public static Item neutroncore;
    public static Item infinitycore;

    public static void init() {
        blockAvSolarPanel = TileBlockCreator.instance.create(BlockAvaritiaSolarPanel.class);
        neutroncore = new IUDEItem("neutroncore");
        infinitycore = new IUDEItem("infinitycore");

    }

    public static void recipe() {


    }

}
