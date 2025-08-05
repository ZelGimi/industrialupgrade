package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TileEntityRollingMachine extends TileEntityInventory implements IUpdateTick, IHasRecipe {

    public final InvSlotRecipes inputSlotA;
    public final InvSlotOutput outputSlot;
    public int progress;
    public MachineRecipe output;
    public int tick = 0;
    public Map<UUID, Double> data = PrimitiveHandler.getPlayersData(EnumPrimitive.ROLLING);

    public TileEntityRollingMachine(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.rolling_machine, pos, state);
        this.inputSlotA = new InvSlotRecipes(this, "cutting", this) {
        };
        this.progress = 0;
        this.outputSlot = new InvSlotOutput(this, 1);
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("primitive_rcm.info"));
        tooltip.add(Localization.translate("primitive_use.info") + IUItem.cutter.getItem().getDescription().getString());
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.rolling_machine;
    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        data = PrimitiveHandler.getPlayersData(EnumPrimitive.ROLLING);
        if (!this.getWorld().isClientSide) {
            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
            new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
        }
        this.output = inputSlotA.process();
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("slot")) {
            try {
                inputSlotA.readFromNbt(is.registryAccess(), ((InvSlot) (DecoderHandler.decode(is))).writeToNbt(is.registryAccess(), new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot1")) {
            try {
                outputSlot.readFromNbt(is.registryAccess(), ((InvSlot) (DecoderHandler.decode(is))).writeToNbt(is.registryAccess(), new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot3")) {
            inputSlotA.set(0, ItemStack.EMPTY);
        }
        if (name.equals("slot2")) {
            outputSlot.set(0, ItemStack.EMPTY);
        }
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            inputSlotA.readFromNbt(customPacketBuffer.registryAccess(), ((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(customPacketBuffer.registryAccess(), new CompoundTag()));
            outputSlot.readFromNbt(customPacketBuffer.registryAccess(), ((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(customPacketBuffer.registryAccess(), new CompoundTag()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (tick > 0) {
            tick--;

        }
        if (tick <= 10) {
            this.progress = 0;
        }
        this.setActive(tick >= 5);
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        try {
            EncoderHandler.encode(customPacketBuffer, inputSlotA);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            EncoderHandler.encode(customPacketBuffer, outputSlot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == IUItem.cutter.getItem() && this.output != null && this.inputSlotA
                .get(0)
                .getCount() >= this.output.getRecipe().input
                .getInputs()
                .get(0)
                .getAmount() && this.outputSlot.canAdd(this.output.getRecipe().output.items.get(
                0))) {
            progress += (short) (10 + (short) (data.getOrDefault(player.getUUID(), 0.0) / 5d));
            this.getCooldownTracker().setTick(10);
            if (progress >= 100) {
                this.progress = 0;
                player.setItemInHand(hand, stack.getItem().getCraftingRemainingItem(stack));
                if (!this.getWorld().isClientSide) {
                    PrimitiveHandler.addExperience(EnumPrimitive.ROLLING, 0.5, player.getUUID());
                }
                this.outputSlot.add(this.output.getRecipe().output.items.get(0));
                this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                if (this.inputSlotA.isEmpty() || this.outputSlot.get(0).getCount() >= 64) {
                    this.output = null;

                }
                if (!level.isClientSide) {
                    new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
                }
            }
            this.tick = 25;
            return true;
        } else {
            if (!stack.isEmpty()) {
                if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.accepts(stack, 0)) {
                    this.inputSlotA.set(0, stack.copy());
                    stack.setCount(0);
                    if (!level.isClientSide) {
                        new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    }
                    return true;
                } else if (!this.inputSlotA.get(0).isEmpty() && this.inputSlotA.get(0).is(stack.getItem())) {
                    int minCount = 64 - this.inputSlotA.get(0).getCount();
                    minCount = Math.min(stack.getCount(), minCount);
                    this.inputSlotA.get(0).grow(minCount);
                    stack.grow(-minCount);
                    if (!level.isClientSide) {
                        new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    }
                    return true;
                }
            } else {
                if (!outputSlot.isEmpty()) {
                    if (!level.isClientSide) {
                        ModUtils.dropAsEntity(level, pos, outputSlot.get(0));
                    }
                    outputSlot.set(0, ItemStack.EMPTY);
                    if (!level.isClientSide) {
                        new PacketUpdateFieldTile(this, "slot2", false);
                    }
                    return true;
                } else {
                    if (!inputSlotA.isEmpty()) {
                        if (!level.isClientSide) {
                            ModUtils.dropAsEntity(level, pos, inputSlotA.get(0));
                        }
                        inputSlotA.set(0, ItemStack.EMPTY);
                        this.output = null;
                        if (!level.isClientSide) {
                            new PacketUpdateFieldTile(this, "slot3", false);
                        }
                        return true;
                    }
                }
            }
        }


        return false;
    }


    @Override
    public void onUpdate() {

    }


    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    @Override
    public void init() {
    }

}
