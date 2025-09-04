package com.denfop.blockentity.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.space.research.api.IRocketLaunchPad;
import com.denfop.api.space.research.event.RocketPadLoadEvent;
import com.denfop.api.space.research.event.RocketPadReLoadEvent;
import com.denfop.api.space.research.event.RocketPadUnLoadEvent;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuRocketLaunchPad;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.render.rocketpad.DataRocket;
import com.denfop.render.rocketpad.RocketPadRender;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenRocketLaunchPad;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class BlockEntityRocketLaunchPad extends BlockEntityInventory implements IRocketLaunchPad {

    private static final List<AABB> aabbs = Collections.singletonList(new AABB(-1, 0.0D, -1, 2, 2.0D,
            2
    ));
    public final InventoryOutput outputSlot;
    public final Inventory roverSlot;
    public final Energy energy;
    public final Fluids fluids;
    public final Fluids.InternalFluidTank tank;
    public final Fluids.InternalFluidTank[] tanks;
    public List<DataRocket> rocketList = new ArrayList<>();
    boolean added = false;
    private UUID player = new UUID(WorldBaseGen.random.nextLong(), WorldBaseGen.random.nextLong());


    public BlockEntityRocketLaunchPad(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.rocket_launch_pad, pos, state);
        this.outputSlot = new InventoryOutput(this, 27);
        this.roverSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof IRoversItem;
            }
        };
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = this.fluids.addTankInsert(
                "tank",
                40000,
                Fluids.fluidPredicate(FluidName.fluidhydrazine.getInstance().get(),
                        FluidName.fluiddimethylhydrazine.getInstance().get(), FluidName.fluiddecane.getInstance().get(),
                        FluidName.fluidxenon.getInstance().get()
                )
        );
        this.tanks = new Fluids.InternalFluidTank[9];
        for (int i = 0; i < 9; i++) {
            tanks[i] = this.fluids.addTankExtract("tank" + i, 10000);
        }
        this.energy = this.addComponent(Energy.asBasicSink(this, 5000000, 14));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.rocket_launch_pad;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    public List<AABB> getAabbs(boolean forCollision) {
        return aabbs;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 80 == 0) {
            NeoForge.EVENT_BUS.post(new RocketPadReLoadEvent(this.getWorld(), this));
        }
        if (!this.roverSlot.isEmpty()) {
            charge(roverSlot.get(0));
            refuel(roverSlot.get(0), (IRoversItem) roverSlot.get(0).getItem());
        }
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof Player) {
            this.player = placer.getUUID();
        }
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.player = nbtTagCompound.getUUID("player");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putUUID("player", player);
        return nbtTagCompound;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

        if (this.getWorld().isClientSide) {
            GlobalRenderManager.addRender(level, pos, createFunction(this));
        } else {
            if (!added) {
                NeoForge.EVENT_BUS.post(new RocketPadLoadEvent(this.getWorld(), this));
                added = true;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public Function<RenderLevelStageEvent, Void> createFunction(BlockEntityRocketLaunchPad te) {
        Function<RenderLevelStageEvent, Void> function = event -> {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();
            poseStack.translate(te.getPos().getX(), te.getPos().getY(),
                    te.getPos().getZ()
            );
            int combinedLight = event.getLevelRenderer().getLightColor(level, pos);
            RocketPadRender.render(te, event.getPartialTick().getGameTimeDeltaTicks(), event.getPoseStack(), Minecraft.getInstance().renderBuffers().bufferSource(), combinedLight, NO_OVERLAY);
            poseStack.popPose();
            return null;
        };
        return function;
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (added) {
            NeoForge.EVENT_BUS.post(new RocketPadUnLoadEvent(this.getWorld(), this));
            added = false;
        }
        if (this.getWorld().isClientSide) {
            GlobalRenderManager.removeRender(level, pos);
        }
    }

    public void refuel(ItemStack itemStack, IRoversItem roversItem) {
        final IFluidHandlerItem fluidHandler = roversItem.getFluidHandler(itemStack);
        if (this.tank.getFluidAmount() > 0) {
            if (fluidHandler.fill(this.tank.getFluid(), IFluidHandler.FluidAction.SIMULATE) > 0) {
                this.tank.drain(fluidHandler.fill(this.tank.getFluid(), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
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
        return roverSlot.get(0);
    }

    @Override
    public Inventory getRoverSlot() {
        return roverSlot;
    }

    @Override
    public UUID getPlayer() {
        return player;
    }

    @Override
    public Level getWorldPad() {
        return level;
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer packetBuffer = super.writePacket();
        packetBuffer.writeBoolean(roverSlot.isEmpty());
        if (!roverSlot.isEmpty()) {
            try {
                EncoderHandler.encode(packetBuffer, roverSlot.get(0));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return packetBuffer;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        boolean isNotEmpty = customPacketBuffer.readBoolean();
        if (!isNotEmpty) {
            try {
                this.roverSlot.set(0, (ItemStack) DecoderHandler.decode(customPacketBuffer));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("datarocket")) {
            ItemStack stack = ItemStack.EMPTY;
            try {
                stack = (ItemStack) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.rocketList.add(new DataRocket((IRoversItem) stack.getItem(), this.pos.getY()));
            this.roverSlot.set(0, ItemStack.EMPTY);
        }
    }

    @Override
    public void addDataRocket(final ItemStack stack) {
        CustomPacketBuffer packetBuffer = new CustomPacketBuffer(level.registryAccess());
        packetBuffer.writeString("datarocket");
        try {
            EncoderHandler.encode(packetBuffer, stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IUCore.network.getServer().addTileFieldToUpdate(this, packetBuffer);
    }

    @Override
    public InventoryOutput getSlotOutput() {
        return outputSlot;
    }

    @Override
    public void addFluidStack(final FluidStack fluidStack) {
        for (int i = 0; i < 9; i++) {
            final Fluids.InternalFluidTank tank = this.tanks[i];
            if (tank.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) > 0) {
                tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                break;
            }
        }
    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        CustomPacketBuffer packetBuffer = super.writeUpdatePacket();
        try {
            EncoderHandler.encode(packetBuffer, this.roverSlot.get(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packetBuffer;
    }

    @Override
    public void readUpdatePacket(CustomPacketBuffer packetBuffer) {
        super.readUpdatePacket(packetBuffer);
        try {
            this.roverSlot.set(0, (ItemStack) DecoderHandler.decode(packetBuffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean needUpdate() {
        return true;
    }

    @Override
    public ContainerMenuRocketLaunchPad getGuiContainer(final Player var1) {
        return new ContainerMenuRocketLaunchPad(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenRocketLaunchPad((ContainerMenuRocketLaunchPad) menu);
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.hasFluidHandler(player.getItemInHand(hand))) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(Capabilities.FluidHandler.BLOCK, side)
            );
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }

    @Override
    public IRoversItem getRover() {
        if (roverSlot.get(0).isEmpty()) {
            return null;
        }
        return (IRoversItem) roverSlot.get(0).getItem();
    }

    @Override
    public void consumeRover() {
        roverSlot.set(0, ItemStack.EMPTY);
    }

}
