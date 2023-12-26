package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentTimer;
import com.denfop.container.ContainerGraphite;
import com.denfop.gui.GuiGraphite;
import com.denfop.invslot.InvSlot;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Timer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityGraphiteHandler extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final ComponentTimer timer;
    public final InvSlotRecipes inputSlotA;
    public final InvSlot flintSlot;
    public  int col;
    private MachineRecipe output;

    public TileEntityGraphiteHandler() {
        super(1000, 14, 1);
        Recipes.recipes.addInitRecipes(this);
        inputSlotA = new InvSlotRecipes(this, "graphite_recipe", this);
        inputSlotA.setStackSizeLimit(1);
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(0, 10, 0)));
        this.flintSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT,1){
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == Items.FLINT;
            }
        };
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer packetBuffer = super.writeContainerPacket();
        packetBuffer.writeInt(this.col);
        return packetBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.col = customPacketBuffer.readInt();
    }


    @Override
    public void init() {
        addRecipe(357, 410);
        addRecipe(410, 310);
        addRecipe(310, 368);
    }

    public static void addRecipe(int container, int output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "graphite_recipe",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements, 1, container)),
                                input.getInput(new ItemStack(Blocks.SAND, 1))),
                        new RecipeOutput(null, new ItemStack(IUItem.crafting_elements, 1, output))
                )
        );
    }



    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.graphite_handler;
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
    public ContainerGraphite getGuiContainer(final EntityPlayer var1) {
        return new ContainerGraphite(var1, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGraphite(getGuiContainer(var1));
    }



    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if(this.col == 0&& this.flintSlot.isEmpty()){
            this.timer.setCanWorkWithOut(false);
            this.setActive(false);
            return;
        }else if(!this.flintSlot.isEmpty()){
            if(this.col + 30 <= 90){
                this.col+=30;
                this.flintSlot.get().shrink(1);
            }
        }

        if(this.energy.getEnergy() < 1  || this.inputSlotA.get().isEmpty() || this.output == null || this.outputSlot.get().getCount() >= 64){
            this.timer.setCanWorkWithOut(false);
            this.setActive(false);
            return;
        }
        this.setActive(true);
        if(!this.timer.isCanWork()){

            this.timer.setCanWork(true);
        }
        if(this.getWorld().provider.getWorldTime() % 40 == 0){
            this.col-=1;
        }
        this.energy.useEnergy(1);
        if(this.timer.getTimers().get(0).getTime() <= 0){
            this.inputSlotA.consume();
            this.outputSlot.add(this.output.getRecipe().output.items.get(0));
            getOutput();
        }
    }
    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        this.timer.resetTime();
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing
        );
    }


}
