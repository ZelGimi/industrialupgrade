package com.denfop.tiles.mechanism;


import com.denfop.api.recipe.InvSlotMultiRecipes;
import com.denfop.container.ContainerMultiMetalFormer;
import com.denfop.gui.GuiMultiMetalFormer;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.network.INetworkClientTileEntityEventListener;
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
                0
        );

    }

    public String getStartSoundFile() {
        return "Machines/metalformer.ogg";
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
        return new GuiMultiMetalFormer(new ContainerMultiMetalFormer(entityPlayer, this, sizeWorkingSlot));
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
        final InvSlotMultiRecipes slot = this.inputSlots;
        switch (mode1) {
            case 0:
                slot.setNameRecipe("extruding");
                break;
            case 1:
                slot.setNameRecipe("rolling");
                break;
            case 2:
                slot.setNameRecipe("cutting");
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
