package com.denfop.tiles.mechanism.worlcollector;

import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.tiles.base.EnumTypeCollector;
import com.denfop.tiles.base.TileBaseWorldCollector;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class TileEarthAssembler extends TileBaseWorldCollector {

    public TileEarthAssembler() {
        super(EnumTypeCollector.EARTH);
    }

    public void init() {
        addRecipe("dustCoal", new ItemStack(Items.DIAMOND), Config.molecular8);

        addRecipe("ingotCopper", OreDictionary.getOres("ingotNickel").get(0), Config.molecular9);

        addRecipe("ingotLead", OreDictionary.getOres("ingotGold").get(0), Config.molecular10);

        if (OreDictionary.getOres("ingotSilver").size() >= 1) {
            addRecipe("ingotTin", OreDictionary.getOres("ingotSilver").get(0), Config.molecular11);
        }

        if (OreDictionary.getOres("ingotSilver").size() >= 1) {
            addRecipe("ingotSilver",
                    OreDictionary.getOres("ingotTungsten").get(0), Config.molecular12
            );
        }
        addRecipe("ingotMikhail",
                OreDictionary.getOres("ingotMagnesium").get(0), Config.molecular35
        );

        addRecipe("ingotMagnesium", OreDictionary.getOres("ingotCaravky").get(0), Config.molecular36);

        addRecipe("ingotManganese", OreDictionary.getOres("ingotCobalt").get(0), 350000);


        addRecipe("ingotCaravky", new ItemStack(IUItem.iuingot, 1, 18), 600000);
        addRecipe("ingotCobalt", new ItemStack(IUItem.iuingot, 1, 16), 350000);
        addRecipe("ingotGermanium", new ItemStack(IUItem.iuingot, 1, 15), 300000);

        addRecipe("ingotSpinel", OreDictionary.getOres("ingotIridium").get(0), Config.molecular4);

        addRecipe("ingotTungsten",
                OreDictionary.getOres("ingotSpinel").get(0), Config.molecular13
        );

        addRecipe("ingotChromium",
                OreDictionary.getOres("ingotMikhail").get(0), Config.molecular14
        );

        addRecipe("ingotPlatinum",
                OreDictionary.getOres("ingotChromium").get(0), Config.molecular15
        );

        addRecipe("ingotGold", OreDictionary.getOres("ingotPlatinum").get(0), Config.molecular16);

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.earth_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

}
