package com.denfop.tiles.mechanism;


import com.denfop.container.ContainerMultiMetalFormer;
import com.denfop.gui.GUIMultiMetalFormer;
import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.recipe.Recipes;
import ic2.core.ContainerBase;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityQuadMetalFormer extends TileEntityMultiMachine
        implements INetworkClientTileEntityEventListener {

    private int mode;

    public TileEntityQuadMetalFormer() {
        super(EnumMultiMachine.QUAD_METAL_FORMER.usagePerTick, EnumMultiMachine.QUAD_METAL_FORMER.lenghtOperation,
                Recipes.metalformerExtruding, 0
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", EnumMultiMachine.QUAD_METAL_FORMER.sizeWorkingSlot,
                Recipes.metalformerExtruding);
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_METAL_FORMER;
    }

    public ContainerBase<? extends TileEntityMultiMachine> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerMultiMetalFormer(entityPlayer, this, this.sizeWorkingSlot);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIMultiMetalFormer(new ContainerMultiMetalFormer(entityPlayer, this, sizeWorkingSlot));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        setMode(nbttagcompound.getInteger("mode"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("mode", this.mode);
        return nbttagcompound;
    }

    public String getInventoryName() {
        return Localization.translate("iu.MetalFormer3.name");
    }


    public void onNetworkEvent(EntityPlayer player, int event) {
        if (event == 0) {
            cycleMode();
        }
    }

    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("mode")) {
            setMode(this.mode);
        }
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode1) {
        InvSlotProcessableMultiGeneric slot = (InvSlotProcessableMultiGeneric) this.inputSlots;
        switch (mode1) {
            case 0:
                slot.setRecipeManager(Recipes.metalformerExtruding);
                this.recipe = Recipes.metalformerExtruding;
                break;
            case 1:
                slot.setRecipeManager(Recipes.metalformerRolling);
                this.recipe = Recipes.metalformerRolling;
                break;
            case 2:
                slot.setRecipeManager(Recipes.metalformerCutting);
                this.recipe = Recipes.metalformerCutting;
                break;
            default:
                throw new RuntimeException("invalid mode: " + mode1);
        }
        this.mode = mode1;
    }

    private void cycleMode() {
        setMode((getMode() + 1) % 3);
    }


}
