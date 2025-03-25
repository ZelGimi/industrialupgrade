package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentTimer;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerProgrammingTable;
import com.denfop.gui.GuiProgrammingTable;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Timer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityProgrammingTable extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe, IManufacturerBlock {

    public final ComponentTimer timer;
    public final InvSlotRecipes inputSlotA;
    public final InvSlotUpgrade upgradeSlot;
    private final ComponentUpgradeSlots componentUpgrade;
    public MachineRecipe output;
    public int level;

    public TileEntityProgrammingTable() {
        super(100, 1, 1);
        Recipes.recipes.addInitRecipes(this);
        inputSlotA = new InvSlotRecipes(this, "programming", this);
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(0, 2, 0)) {
            @Override
            public int getTickFromSecond() {
                return (int) Math.max(1, 20 - ((TileEntityProgrammingTable) this.parent).getLevel() * 1.75);
            }
        });
        this.upgradeSlot = new InvSlotUpgrade(this,4);
        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.level = 0;
    }

    public static void addRecipe(int container) {

    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.level -= level;
    }

    @Override
    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "programming",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements, 1, 487))),
                        new RecipeOutput(null, new ItemStack(IUItem.crafting_elements, 1, 488))
                )
        );
    }

    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (level < 10) {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation)) {
                return super.onActivated(player, hand, side, hitX, hitY, hitZ);
            } else {
                stack.shrink(1);
                this.level++;
                return true;
            }
        } else {

            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation, this.level));
            this.level = 0;
        }
        return ret;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.level = nbttagcompound.getInteger("level");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("level", this.level);
        return nbttagcompound;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.programming_table;
    }

    @Override
    public void onUpdate() {

    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            inputSlotA.load();
            this.getOutput();
        }


    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();

        if (this.inputSlotA.get().isEmpty() || this.output == null || this.outputSlot
                .get()
                .getCount() >= 64 || !this.energy.canUseEnergy(2)) {
            this.timer.setCanWork(false);
            this.setActive(false);
            return;
        }
        this.setActive(true);
        if (!this.timer.isCanWork()) {
            this.timer.setCanWork(true);
        }
        this.energy.useEnergy(2);
        if (this.timer.getTimers().get(0).getTime() <= 0) {
            this.inputSlotA.consume();
            this.outputSlot.add(this.output.getRecipe().output.items.get(0));
            getOutput();
        }
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        this.timer.resetTime();
        return this.output;
    }

    @Override
    public ContainerProgrammingTable getGuiContainer(final EntityPlayer var1) {
        return new ContainerProgrammingTable(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiProgrammingTable(getGuiContainer(var1));
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }


}
