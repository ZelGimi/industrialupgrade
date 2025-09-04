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
import com.denfop.blocks.mechanism.BlockCompressorEntity;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemCraftingElements;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BlockEntityCompressor extends BlockEntityInventory implements IUpdateTick, AudioFixer {

    private static final List<AABB> aabbs = Collections.singletonList(new AABB(0, 0.0D, 0, 1, 1.25D,
            1
    ));
    public final InventoryRecipes inputSlotA;
    public final InventoryOutput outputSlot;
    public int progress;
    public MachineRecipe output;
    public int durability = 96;
    public Map<UUID, Double> data;

    public BlockEntityCompressor(BlockPos pos, BlockState state) {
        super(BlockCompressorEntity.compressor, pos, state);
        this.inputSlotA = new InventoryRecipes(this, "compressor", this) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack itemStack) {
                if (index == 4)
                    return super.canPlaceItem(0, itemStack);
                return false;
            }

            @Override
            public int getStackSizeLimit() {
                if (output == null) {
                    return 1;
                }
                return output.getRecipe().input.getInputs().get(0).getAmount();
            }
        };
        this.progress = 0;
        this.outputSlot = new InventoryOutput(this, 1);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return LazyOptional.empty();
        return super.getCapability(cap, facing);
    }


    @Override
    public List<ItemStack> getSelfDrops(final int fortune, final boolean wrench) {
        List<ItemStack> drop = super.getSelfDrops(fortune, wrench);
        ItemStack stack = drop.get(0);
        final CompoundTag nbt = ModUtils.nbt(stack);
        nbt.putInt("durability", durability);
        return drop;
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        final CompoundTag nbt = ModUtils.nbt(stack);
        if (nbt.contains("durability")) {
            durability = nbt.getInt("durability");
        }
    }

    public List<AABB> getAabbs(boolean forCollision) {
        return aabbs;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.primal_repair2"));
        tooltip.add(Localization.translate("iu.primal_repair.info"));
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
        return EnumSound.primal_compressor.getSoundEvent();
    }

    @Override
    public void initiate(final int soundEvent) {
        if (soundEvent == 0) {
            this.getWorld().playSound(null, this.pos, getSound(), SoundSource.BLOCKS, 0.2F, 1);
        }
    }

    @Override
    public boolean getEnable() {
        return true;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.blockCompressor.getBlock();
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockCompressorEntity.compressor;
    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
            new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
        }
        data = PrimitiveHandler.getPlayersData(EnumPrimitive.COMPRESSOR);
        this.output = inputSlotA.process();
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("slot")) {
            try {
                inputSlotA.readFromNbt(((Inventory) (DecoderHandler.decode(is))).writeToNbt(new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot1")) {
            try {
                outputSlot.readFromNbt(((Inventory) (DecoderHandler.decode(is))).writeToNbt(new CompoundTag()));
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
            inputSlotA.readFromNbt(((Inventory) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new CompoundTag()));
            outputSlot.readFromNbt(((Inventory) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new CompoundTag()));
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
    public boolean onSneakingActivated(
            final Player player,
            final InteractionHand hand,
            final Direction side,
            final Vec3 hitX
    ) {
        ItemStack stack = player.getItemInHand(hand);
        if (durability >= 0 && durability < 96 && stack.getItem() instanceof ItemCraftingElements<?> && IUItem.crafting_elements.getMeta((ItemCraftingElements) stack.getItem()) == 76) {
            durability = 96;
            stack.shrink(1);
            new PacketUpdateFieldTile(this, "durability", this.durability);
        }
        return super.onSneakingActivated(player, hand, side, hitX);
    }

    @Override
    public boolean onActivated(
            final Player player,
            final InteractionHand hand,
            final Direction side,
            final Vec3 hitX
    ) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.getWorld().isClientSide) {
            if (stack.isEmpty() && this.output != null && this.outputSlot.isEmpty() && this.inputSlotA.continue_process(this.output) && durability > 0) {
                progress += (int) (15 + (data.getOrDefault(player.getUUID(), 0.0) / 10d));
                this.getCooldownTracker().setTick(15);
                this.setActive(String.valueOf((int) ((progress * 9D) / 100)));
                if (!this.getWorld().isClientSide) {
                    this.initiate(0);
                }
                if (progress >= 100) {
                    this.progress = 0;
                    if (!this.getWorld().isClientSide)
                        PrimitiveHandler.addExperience(EnumPrimitive.COMPRESSOR, 0.75, player.getUUID());
                    this.setActive(false);
                    durability--;
                    this.outputSlot.add(this.output.getRecipe().output.items.get(0));
                    this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                    this.output = null;
                    if (!level.isClientSide) {
                        new PacketUpdateFieldTile(this, "slot3", this.inputSlotA);
                        new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
                    }
                }

                return this.getWorld().isClientSide;
            } else {
                if (!stack.isEmpty()) {
                    if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.canPlaceItem(4, stack)) {
                        final ItemStack stack1 = stack.copy();
                        stack1.setCount(1);
                        this.inputSlotA.set(0, stack1);
                        stack.shrink(1);
                        if (!level.isClientSide) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        }
                        return true;
                    } else if (!this.inputSlotA.get(0).isEmpty() && this.inputSlotA.get(0).is(stack.getItem())) {
                        int minCount = this.inputSlotA.getStackSizeLimit() - this.inputSlotA.get(0).getCount();
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
        }


        return this.getWorld().isClientSide;
    }

    @Override
    public void onUpdate() {

    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.durability = nbttagcompound.getInt("durability");

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("durability", durability);
        return nbttagcompound;
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
