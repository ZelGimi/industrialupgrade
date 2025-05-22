package com.denfop.tiles.mechanism;

import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.space.research.api.IRocketLaunchPad;
import com.denfop.api.space.research.event.ResearchTableReLoadEvent;
import com.denfop.api.space.research.event.RocketPadLoadEvent;
import com.denfop.api.space.research.event.RocketPadReLoadEvent;
import com.denfop.api.space.research.event.RocketPadUnLoadEvent;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerRocketLaunchPad;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.gui.GuiRocketLaunchPad;
import com.denfop.invslot.InvSlot;
import com.denfop.items.space.ItemRover;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.render.rocketpad.DataRocket;
import com.denfop.render.rocketpad.RocketPadRender;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class TileEntityRocketLaunchPad extends TileEntityInventory implements IRocketLaunchPad {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(-1, 0.0D, -1, 2, 2.0D,
            2
    ));
    public final InvSlotOutput outputSlot;
    public final InvSlot roverSlot;
    public final Energy energy;
    public final Fluids fluids;
    public final Fluids.InternalFluidTank tank;
    public final Fluids.InternalFluidTank[] tanks;
    public List<DataRocket> rocketList = new ArrayList<>();
    boolean added = false;
    private UUID player;
    @SideOnly(Side.CLIENT)
    private RocketPadRender render;

    public TileEntityRocketLaunchPad() {
        this.outputSlot = new InvSlotOutput(this, 27);
        this.roverSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof IRoversItem;
            }
        };
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = this.fluids.addTankInsert(
                "tank",
                40000,
                Fluids.fluidPredicate(FluidName.fluidhydrazine.getInstance(),
                        FluidName.fluiddimethylhydrazine.getInstance(), FluidName.fluiddecane.getInstance(),
                        FluidName.fluidxenon.getInstance()
                )
        );
        this.tanks = new Fluids.InternalFluidTank[9];
        for (int i = 0; i < 9; i++) {
            tanks[i] = this.fluids.addTankExtract("tank" + i, 10000);
        }
        this.energy = this.addComponent(Energy.asBasicSink(this, 5000000, 14));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.rocket_launch_pad;
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

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return aabbs;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getWorldTime() % 80 == 0){
            MinecraftForge.EVENT_BUS.post(new RocketPadReLoadEvent(this.getWorld(), this));
        }
        if (!this.roverSlot.isEmpty()) {
            charge(roverSlot.get());
            refuel(roverSlot.get(), (IRoversItem) roverSlot.get().getItem());
        }
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof EntityPlayer) {
            this.player = placer.getUniqueID();
        }
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.player = nbtTagCompound.getUniqueId("player");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setUniqueId("player", player);
        return nbtTagCompound;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

        if (this.getWorld().isRemote) {
            GlobalRenderManager.addRender(world, pos, createFunction(this));
        } else {
            if (!added) {
                MinecraftForge.EVENT_BUS.post(new RocketPadLoadEvent(this.getWorld(), this));
                added = true;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public Function createFunction(TileEntityRocketLaunchPad te) {
        Function function = o -> {
            if (this.render == null) {
                this.render = new RocketPadRender();
            }
            render.render(te);
            return 0;
        };
        return function;
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (added) {
            MinecraftForge.EVENT_BUS.post(new RocketPadUnLoadEvent(this.getWorld(), this));
            added = false;
        }
        if (this.getWorld().isRemote) {
            GlobalRenderManager.removeRender(world, pos);
        }
    }

    public void refuel(ItemStack itemStack, IRoversItem roversItem) {
        final IFluidHandlerItem fluidHandler = roversItem.getFluidHandler(itemStack);
        if (this.tank.getFluidAmount() > 0) {
            if (fluidHandler.fill(this.tank.getFluid(), false) > 0) {
                this.tank.drain(fluidHandler.fill(this.tank.getFluid(), true), true);
            }
        }
    }

    public void charge(ItemStack itemStack) {
        if (this.energy.getEnergy() > 0) {
            this.energy.useEnergy(ElectricItem.manager.charge(itemStack, this.energy.getEnergy(), 14, true, false));
        }
    }

    @Override
    public ItemStack getRoverStack() {
        return roverSlot.get();
    }

    @Override
    public InvSlot getRoverSlot() {
        return roverSlot;
    }

    @Override
    public UUID getPlayer() {
        return player;
    }

    @Override
    public World getWorldPad() {
        return world;
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer packetBuffer = super.writePacket();
        packetBuffer.writeBoolean(roverSlot.isEmpty());
        if (!roverSlot.isEmpty()) {
            packetBuffer.writeItemStack(roverSlot.get());
        }
        return packetBuffer;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        boolean isNotEmpty = customPacketBuffer.readBoolean();
        if (!isNotEmpty) {
            try {
                this.roverSlot.put(0, customPacketBuffer.readItemStack());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("datarocket")) {
            try {
                ItemStack stack = is.readItemStack();
                this.rocketList.add(new DataRocket((IRoversItem) stack.getItem(), this.pos.getY()));
                this.roverSlot.put(0, ItemStack.EMPTY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void addDataRocket(final ItemStack stack) {
        CustomPacketBuffer packetBuffer = new CustomPacketBuffer();
        packetBuffer.writeString("datarocket");
        packetBuffer.writeItemStack(stack);
        IUCore.network.getServer().addTileFieldToUpdate(this, packetBuffer);
    }

    @Override
    public InvSlotOutput getSlotOutput() {
        return outputSlot;
    }

    @Override
    public void addFluidStack(final FluidStack fluidStack) {
        for (int i = 0; i < 9; i++) {
            final Fluids.InternalFluidTank tank = this.tanks[i];
            if (tank.fill(fluidStack, false) > 0) {
                tank.fill(fluidStack, true);
                break;
            }
        }
    }

    @Override
    public ContainerRocketLaunchPad getGuiContainer(final EntityPlayer var1) {
        return new ContainerRocketLaunchPad(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiRocketLaunchPad(getGuiContainer(var1));
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
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) && !(player
                .getHeldItem(hand)
                .getItem() instanceof ItemRover)) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    this.getComp(Fluids.class).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public IRoversItem getRover() {
        if (roverSlot.get().isEmpty()) {
            return null;
        }
        return (IRoversItem) roverSlot.get().getItem();
    }

    @Override
    public void consumeRover() {
        roverSlot.put(0, ItemStack.EMPTY);
    }

}
