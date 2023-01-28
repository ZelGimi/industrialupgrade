package com.denfop.tiles.mechanism.worlcollector;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.tiles.base.EnumTypeCollector;
import com.denfop.tiles.base.IIsMolecular;
import com.denfop.tiles.base.TileEntityBaseWorldCollector;
import ic2.core.block.TileEntityBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityCrystallize extends TileEntityBaseWorldCollector implements IIsMolecular {

    protected ItemStack output_stack;

    public TileEntityCrystallize() {
        super(EnumTypeCollector.DEFAULT);
    }

    public void init() {

        addRecipe(Ic2Items.diamondDust, 0.125, new ItemStack(Items.DIAMOND));
        addRecipe(Ic2Items.emeraldDust, 0.125, new ItemStack(Items.EMERALD));
        addRecipe(Ic2Items.lapiDust, 0.0625, new ItemStack(Items.DYE, 1, 4));
        addRecipe(new ItemStack(Items.DIAMOND), 0.25, new ItemStack(Items.EMERALD));
        addRecipe(new ItemStack(Items.EMERALD), 0.25, new ItemStack(Items.DIAMOND));
        addRecipe(new ItemStack(IUItem.preciousgem, 1, 0), 0.25, new ItemStack(Items.EMERALD));
        addRecipe(new ItemStack(IUItem.preciousgem, 1, 1), 0.25, new ItemStack(Items.EMERALD));
        addRecipe(Ic2Items.iridiumOre, 20, new ItemStack(IUItem.iuingot, 1, 17));
        addRecipe(new ItemStack(Blocks.DIAMOND_ORE), 4, new ItemStack(Items.DIAMOND, 2));
        addRecipe(new ItemStack(Blocks.EMERALD_ORE), 4, new ItemStack(Items.EMERALD, 2));
        addRecipe(new ItemStack(Blocks.REDSTONE_ORE), 1, new ItemStack(Items.REDSTONE, 4));
        addRecipe(new ItemStack(Blocks.LAPIS_ORE), 1, new ItemStack(Items.DYE, 4, 4));
        addRecipe(new ItemStack(Items.GOLDEN_APPLE), 4, new ItemStack(Items.GOLDEN_APPLE, 1, 1));
        addRecipe(new ItemStack(Items.ROTTEN_FLESH), 4, new ItemStack(Items.PORKCHOP));
        addRecipe(new ItemStack(Items.SPIDER_EYE), 4, new ItemStack(Items.BEEF));
        addRecipe(new ItemStack(Items.STRING), 20, new ItemStack(Blocks.WEB));
        addRecipe(Ic2Items.latex, 10, new ItemStack(Items.SLIME_BALL));

    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.machineRecipe = output;
        if (this.machineRecipe != null) {
            output_stack = this.machineRecipe.getRecipe().output.items.get(0);
        } else {
            output_stack = new ItemStack(Items.AIR);
        }

        this.setOverclockRates();

    }

    public MachineRecipe getOutput() {

        this.machineRecipe = this.inputSlot.process();
        if (this.machineRecipe != null) {
            output_stack = this.machineRecipe.getRecipe().output.items.get(0);
        } else {
            output_stack = new ItemStack(Items.AIR);
        }

        return this.machineRecipe;
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("output_stack");
        return ret;
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public int getMode() {
        return 5;
    }

    @Override
    public ItemStack getItemStack() {
        return this.output_stack;
    }

    @Override
    public TileEntityBlock getEntityBlock() {
        return this;
    }

}
