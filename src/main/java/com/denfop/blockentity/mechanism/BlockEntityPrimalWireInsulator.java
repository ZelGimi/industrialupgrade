package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.sound.AudioFixer;
import com.denfop.api.sound.EnumTypeAudio;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockPrimalWireInsulatorEntity;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BlockEntityPrimalWireInsulator extends BlockEntityInventory implements IUpdateTick, AudioFixer {

    private static final List<AABB> aabbs = Collections.singletonList(new AABB(0, 0.0D, 0, 1, 1.25D,
            1
    ));
    public final InventoryRecipes inputSlotA;
    public final InventoryOutput outputSlot;
    public int progress;
    public MachineRecipe output;
    public Map<UUID, Double> data = PrimitiveHandler.getPlayersData(EnumPrimitive.WIRE_INSULATOR);

    public BlockEntityPrimalWireInsulator(BlockPos pos, BlockState state) {
        super(BlockPrimalWireInsulatorEntity.primal_wire_insulator, pos, state);
        this.inputSlotA = new InventoryRecipes(this, "wire_insulator", this) {
            @Override
            public int getStackSizeLimit() {
                if (output == null) {
                    return 1;
                }
                return output.getRecipe().input.getInputs().get(0).getAmount();
            }
        };
        this.progress = 0;
        this.outputSlot = new InventoryOutput(this, 1) {
            @Override
            public int getStackSizeLimit() {
                return 1;
            }
        };
    }

    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK)
            return null;
        return super.getCapability(cap, side);
    }


    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        for (int i = 1; i < 4; i++) {
            tooltip.add(Localization.translate("wire_insulator.info" + i));
        }
    }

    @Override
    public List<ItemStack> getSelfDrops(final int fortune, final boolean wrench) {
        List<ItemStack> drop = super.getSelfDrops(fortune, wrench);
        ItemStack stack = drop.get(0);
        return drop;
    }


    public List<AABB> getAabbs(boolean forCollision) {
        return aabbs;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.blockwireinsulator.getBlock();
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockPrimalWireInsulatorEntity.primal_wire_insulator;
    }


    @Override
    public EnumTypeAudio getTypeAudio() {
        return EnumTypeAudio.ON;
    }

    @Override
    public void setType(final EnumTypeAudio type) {

    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    @Override
    public void initiate(final int soundEvent) {
        if (soundEvent == 0) {
        }
    }

    @Override
    public boolean getEnable() {
        return true;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        data = PrimitiveHandler.getPlayersData(EnumPrimitive.WIRE_INSULATOR);
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
                inputSlotA.readFromNbt(is.registryAccess(), ((Inventory) (DecoderHandler.decode(is))).writeToNbt(is.registryAccess(), new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot1")) {
            try {
                outputSlot.readFromNbt(is.registryAccess(), ((Inventory) (DecoderHandler.decode(is))).writeToNbt(is.registryAccess(), new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot3")) {
            inputSlotA.set(0, ItemStack.EMPTY);
            inputSlotA.set(1, ItemStack.EMPTY);
        }
        if (name.equals("slot2")) {
            outputSlot.set(0, ItemStack.EMPTY);
        }
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            inputSlotA.readFromNbt(customPacketBuffer.registryAccess(), ((Inventory) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(customPacketBuffer.registryAccess(), new CompoundTag()));
            outputSlot.readFromNbt(customPacketBuffer.registryAccess(), ((Inventory) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(customPacketBuffer.registryAccess(), new CompoundTag()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        if (!this.getWorld().isClientSide) {
            if (stack.getItem() == IUItem.cutter.getItem() && this.output != null && this.outputSlot.isEmpty() && this.inputSlotA.continue_process(
                    this.output)) {
                this.getCooldownTracker().setTick(10);
                progress += (short) (20 + (short) (data.getOrDefault(player.getUUID(), 0.0) / 3.3d));
                if (!this.getWorld().isClientSide) {
                    this.initiate(0);
                }
                if (progress >= 100) {
                    this.progress = 0;
                    this.outputSlot.add(this.output.getRecipe().output.items.get(0));
                    if (!this.getWorld().isClientSide) {
                        PrimitiveHandler.addExperience(EnumPrimitive.WIRE_INSULATOR, 0.5, player.getUUID());
                    }
                    this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                    this.inputSlotA.consume(1, this.output.getRecipe().input.getInputs().get(1).getAmount());
                    this.output = null;
                    player.setItemInHand(hand, stack.getItem().getCraftingRemainingItem(stack));
                    if (!level.isClientSide) {
                        new PacketUpdateFieldTile(this, "slot3", this.inputSlotA);
                        new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
                    }
                }

                return this.getWorld().isClientSide;
            } else {
                if (!stack.isEmpty() && this.outputSlot.isEmpty()) {
                    if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.canPlaceItem(0, stack)) {
                        final ItemStack stack1 = stack.copy();
                        stack1.setCount(1);
                        this.inputSlotA.set(0, stack1);
                        stack.shrink(1);
                        if (!level.isClientSide) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        }
                        changeState();
                        return true;
                    } else if (!this.inputSlotA.get(0).isEmpty() && this.inputSlotA.get(0).is(stack.getItem())) {
                        int minCount = this.inputSlotA.getStackSizeLimit() - this.inputSlotA.get(0).getCount();
                        minCount = Math.min(stack.getCount(), minCount);
                        this.inputSlotA.get(0).grow(minCount);
                        stack.grow(-minCount);
                        if (!level.isClientSide) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        }
                        changeState();
                        return true;
                    }

                    if (this.inputSlotA.get(1).isEmpty() && this.inputSlotA.canPlaceItem(1, stack)) {
                        final ItemStack stack1 = stack.copy();
                        stack1.setCount(1);
                        this.inputSlotA.set(1, stack1);
                        stack.shrink(1);
                        if (!level.isClientSide) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        }
                        changeState();
                        return true;
                    } else if (!this.inputSlotA.get(1).isEmpty() && this.inputSlotA.get(1).is(stack.getItem())) {
                        int minCount = this.inputSlotA.getStackSizeLimit() - this.inputSlotA.get(1).getCount();
                        minCount = Math.min(stack.getCount(), minCount);
                        this.inputSlotA.get(1).grow(minCount);
                        stack.grow(-minCount);
                        if (!level.isClientSide) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        }
                        changeState();
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
                        changeState();
                        return true;
                    } else {
                        if (!inputSlotA.isEmpty()) {
                            if (!level.isClientSide) {
                                ModUtils.dropAsEntity(level, pos, inputSlotA.get(0));
                                ModUtils.dropAsEntity(level, pos, inputSlotA.get(1));
                            }
                            inputSlotA.set(0, ItemStack.EMPTY);
                            inputSlotA.set(1, ItemStack.EMPTY);
                            this.output = null;
                            if (!level.isClientSide) {
                                new PacketUpdateFieldTile(this, "slot3", false);
                            }
                            changeState();
                            return true;
                        }
                    }
                }
            }
        }


        return this.level.isClientSide;
    }


    private void changeState() {
        final ItemStack input = this.inputSlotA.get(0);
        final ItemStack input1 = this.inputSlotA.get(1);
        if (this.outputSlot.isEmpty()) {
            if (!input.isEmpty()) {
                if (input1.isEmpty()) {
                    switch (IUItem.cable.getMetaFromItemStack(input)) {
                        case 11:
                            setActive("copper");
                            break;
                        case 14:
                            setActive("gold");
                            break;
                        case 16:
                            setActive("iron");
                            break;
                        case 18:
                            setActive("tin");
                            break;
                    }
                } else {
                    switch (IUItem.cable.getMetaFromItemStack(input)) {
                        case 11:
                            setActive("copper_final");
                            break;
                        case 14:
                            setActive("gold_final");
                            break;
                        case 16:
                            setActive("iron_final");
                            break;
                        case 18:
                            setActive("tin_final");
                            break;
                    }
                }
            } else {
                setActive("");
            }
        } else {
            switch (IUItem.cable.getMetaFromItemStack(outputSlot.get(0))) {
                case 12:
                    setActive("copper_final");
                    break;
                case 15:
                    setActive("gold_final");
                    break;
                case 17:
                    setActive("iron_final");
                    break;
                case 19:
                    setActive("tin_final");
                    break;
            }
        }
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


}
