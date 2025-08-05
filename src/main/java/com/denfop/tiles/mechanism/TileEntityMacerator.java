package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMacerator;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemCraftingElements;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ComposterBlock;
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

public class TileEntityMacerator extends TileEntityInventory implements IUpdateTick, IAudioFixer {

    private static final List<AABB> aabbs = Collections.singletonList(new AABB(0, 0.0D, 0, 1, 1.25D,
            1
    ));
    public final InvSlotRecipes inputSlotA;
    public final InvSlotOutput outputSlot;
    public int progress;
    public MachineRecipe output;
    public int durability = 96;
    public Map<UUID, Double> data = PrimitiveHandler.getPlayersData(EnumPrimitive.MACERATOR);

    public TileEntityMacerator(BlockPos pos, BlockState state) {
        super(BlockMacerator.macerator, pos, state);
        this.inputSlotA = new InvSlotRecipes(this, "macerator", this) {
            @Override
            public boolean accepts(final ItemStack itemStack, final int index) {
                if (index == 4) {
                    List<TagKey<Item>> tags = itemStack.getTags().filter(itemTagKey -> itemTagKey.location().getPath().split("/").length > 1).toList();
                    for (TagKey<Item> i : tags) {
                        String name = i.location().getPath();
                        if (name.startsWith("ores") || name.startsWith("raw_materials")|| (name.startsWith("storage_blocks/raw_"))) {
                            return false;
                        }
                    }
                    return super.accepts(itemStack, 0);
                }
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
        this.outputSlot = new InvSlotOutput(this, 1);
    }

    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK)
            return null;
        return super.getCapability(cap, side);
    }


    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.primal_repair1"));

        tooltip.add(Localization.translate("iu.primal_repair.info"));
        tooltip.add(Localization.translate("iu.primal_macerator.infi"));
        tooltip.add(Localization.translate( "iu.primal_macerator.info2"));

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
    public BlockTileEntity getBlock() {
        return IUItem.blockMacerator.getBlock();
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockMacerator.macerator;
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
        return EnumSound.primal_macerator.getSoundEvent();
    }

    @Override
    public void initiate(final int soundEvent) {
        if (soundEvent == 0) {
            this.getWorld().playSound(null, this.pos, getSound(), SoundSource.BLOCKS, 64F, 1);
        }
    }

    @Override
    public boolean getEnable() {
        return true;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        data = PrimitiveHandler.getPlayersData(EnumPrimitive.MACERATOR);
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
    public boolean onSneakingActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        ItemStack stack = player.getItemInHand(hand);
        if (durability >= 0 && durability < 96 && stack.getItem() instanceof ItemCraftingElements<?> && ((ItemCraftingElements<?>) stack.getItem()).getElement().getId() == 41) {
            durability = 96;
            stack.shrink(1);
            new PacketUpdateFieldTile(this, "durability", this.durability);
        }
        return super.onSneakingActivated(player, hand, side, vec3);
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.getWorld().isClientSide) {
            if (stack.isEmpty() && this.output != null && this.outputSlot.isEmpty() && this.inputSlotA.continue_process(this.output) && durability > 0) {
                progress += (int) (15 + (data.getOrDefault(player.getUUID(), 0.0) / 10d));
                this.getCooldownTracker().setTick(10);
                this.setActive(!this.getActive());
                if (!this.getWorld().isClientSide) {
                    this.initiate(0);
                }
                if (progress >= 100) {
                    this.progress = 0;
                    this.setActive(false);
                    if (!this.getWorld().isClientSide) {
                        PrimitiveHandler.addExperience(EnumPrimitive.MACERATOR, 0.75, player.getUUID());
                    }
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
                    if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.accepts(stack, 4)) {
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


        return this.level.isClientSide;
    }


    @Override
    public void onUpdate() {

    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);


    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
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
