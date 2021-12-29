package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.container.ContainerSolarGeneratorEnergy;
import com.denfop.gui.GUISolarGeneratorEnergy;
import com.denfop.invslot.InvSlotGenSunarrium;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.init.Localization;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileSolarGeneratorEnergy extends TileEntityInventory implements IHasGui, INetworkTileEntityEventListener {

    public final InvSlotGenSunarrium input;
    public AudioSource audioSource;
    public final InvSlotOutput outputSlot;

    public final ItemStack itemstack = new ItemStack(IUItem.sunnarium, 1, 4);

    public double sunenergy;

    public final double maxSunEnergy;

    public final double cof;
    private final String name;

    public TileSolarGeneratorEnergy(double cof, String name) {

        this.sunenergy = 0D;
        this.maxSunEnergy = 2500;
        this.cof = cof;
        this.outputSlot = new InvSlotOutput(this, "output", 1);
      this.input =  new InvSlotGenSunarrium(this);
        this.name = name;
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

    @Override
    public void onNetworkUpdate(String field) {

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

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("sunenergy");

        return ret;

    }

    public void updateEntityServer() {

        super.updateEntityServer();

        long tick = this.getWorld().provider.getWorldTime() % 24000L;
           if (this.world.canBlockSeeSky(this.pos.up()) &&
                (this.world.getBlockState(this.pos.up()).getMaterial().getMaterialMapColor() ==
                        MapColor.AIR)) {
            energy(tick);
            if (this.sunenergy >= maxSunEnergy) {
                if (this.outputSlot.canAdd(itemstack)) {
                    this.outputSlot.add(itemstack);
                    this.sunenergy -= maxSunEnergy;
                }
            }
        }

    }

    public void energy(long tick) {
        double k = 0;
       List<Double> lst = input.coefday();
       if(this.getWorld().provider.isDaytime() ) {
           if (tick <= 1000L) {
               k = 5;
           }
           if (tick > 1000L && tick <= 4000L) {
               k = 10;
           }
           if (tick > 4000L && tick <= 8000L) {
               k = 30;
           }
           if (tick > 8000L && tick <= 11000L) {
               k = 10;
           }
           if (tick > 11000L) {
               k = 5;
           }
           this.sunenergy += (k * this.cof * (1 + lst.get(0)));
       }

        if(lst.get(2) > 0 && !this.getWorld().provider.isDaytime()) {
          double  tick1 = tick - 12000;
            if (tick1 <= 1000L) {
                k = 5;
            }
            if (tick1 > 1000L && tick1 <= 4000L) {
                k = 10;
            }
            if (tick1 > 4000L && tick1 <= 8000L) {
                k = 30;
            }
            if (tick1 > 8000L && tick1 <= 11000L) {
                k = 10;
            }
            if (tick1 > 11000L) {
                k = 5;
            }
            this.sunenergy += (k * this.cof * (lst.get(2)-1) * (1+lst.get(1)));

        }
        if (this.sunenergy >= this.maxSunEnergy) {
            this.sunenergy = this.maxSunEnergy;
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sunenergy = nbttagcompound.getDouble("sunenergy");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("sunenergy", this.sunenergy);
        return nbttagcompound;
    }

    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return true;
    }


    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    @Override
    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IC2.audioManager.createSource(this, getStartSoundFile());
        }
        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (getInterruptSoundFile() != null) {
                        IC2.audioManager.playOnce(this, getInterruptSoundFile());
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
                break;
        }
    }

    @Override
    public void onGuiClosed(EntityPlayer arg0) {
    }


    public String getName() {

        return Localization.translate(this.name);
    }

    public ContainerBase<? extends TileSolarGeneratorEnergy> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSolarGeneratorEnergy(entityPlayer, this);
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUISolarGeneratorEnergy(new ContainerSolarGeneratorEnergy(entityPlayer, this));
    }

}
