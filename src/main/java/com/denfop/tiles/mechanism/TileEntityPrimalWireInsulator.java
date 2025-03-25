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
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockPrimalWireInsulator;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TileEntityPrimalWireInsulator extends TileEntityInventory implements IUpdateTick, IAudioFixer {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(0, 0.0D, 0, 1, 1.25D,
            1
    ));
    public final InvSlotRecipes inputSlotA;
    public final InvSlotOutput outputSlot;
    public int progress;
    public MachineRecipe output;
    public Map<UUID,Double> data = PrimitiveHandler.getPlayersData(EnumPrimitive.WIRE_INSULATOR);

    public TileEntityPrimalWireInsulator() {

        this.inputSlotA = new InvSlotRecipes(this, "wire_insulator", this) {
            @Override
            public int getStackSizeLimit() {
                if (output == null) {
                    return 1;
                }
                return output.getRecipe().input.getInputs().get(0).getAmount();
            }
        };
        this.progress = 0;
        this.outputSlot = new InvSlotOutput(this, 1) {
            @Override
            public int getStackSizeLimit() {
                return 1;
            }
        };
    }

    @Override
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {
        return super.hasCapability(capability, facing) && capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
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
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        return drop;
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        final NBTTagCompound nbt = ModUtils.nbt(stack);

    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return aabbs;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.blockwireinsulator;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockPrimalWireInsulator.primal_wire_insulator;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
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
            this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 64F, 1);
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
        if (!this.getWorld().isRemote) {
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
                inputSlotA.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new NBTTagCompound()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot1")) {
            try {
                outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new NBTTagCompound()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot3")) {
            inputSlotA.put(0, ItemStack.EMPTY);
            inputSlotA.put(1, ItemStack.EMPTY);
        }
        if (name.equals("slot2")) {
            outputSlot.put(0, ItemStack.EMPTY);
        }
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            inputSlotA.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new NBTTagCompound()));
            outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new NBTTagCompound()));
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
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!this.getWorld().isRemote) {
            if (stack.getItem() == IUItem.cutter && this.output != null && this.outputSlot.isEmpty() && this.inputSlotA.continue_process(this.output)) {
                this.getCooldownTracker().setTick(10);
                progress += (short) (20 + (short) (data.getOrDefault(player.getUniqueID(),0.0) / 3.3d));
                if (!this.getWorld().isRemote) {
                    this.initiate(0);
                }
                if (progress >= 100) {
                    this.progress = 0;
                    this.outputSlot.add(this.output.getRecipe().output.items.get(0));
                    if (!this.getWorld().isRemote)
                    PrimitiveHandler.addExperience(EnumPrimitive.WIRE_INSULATOR,0.5,player.getUniqueID());
                    this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                    this.inputSlotA.consume(1, this.output.getRecipe().input.getInputs().get(1).getAmount());
                    this.output = null;
                    player.setHeldItem(hand, stack.getItem().getContainerItem(stack));
                    if (!world.isRemote) {
                        new PacketUpdateFieldTile(this, "slot3", this.inputSlotA);
                        new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
                    }
                }

                return this.getWorld().isRemote;
            } else {
                if (!stack.isEmpty() && this.outputSlot.isEmpty()) {
                    if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.accepts(stack, 0)) {
                        final ItemStack stack1 = stack.copy();
                        stack1.setCount(1);
                        this.inputSlotA.put(0, stack1);
                        stack.shrink(1);
                        if (!world.isRemote) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        }
                        changeState();
                        return true;
                    } else if (!this.inputSlotA.get(0).isEmpty() && this.inputSlotA.get(0).isItemEqual(stack)) {
                        int minCount = this.inputSlotA.getStackSizeLimit() - this.inputSlotA.get(0).getCount();
                        minCount = Math.min(stack.getCount(), minCount);
                        this.inputSlotA.get(0).grow(minCount);
                        stack.grow(-minCount);
                        if (!world.isRemote) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        }
                        changeState();
                        return true;
                    }

                    if (this.inputSlotA.get(1).isEmpty() && this.inputSlotA.accepts(stack, 1)) {
                        final ItemStack stack1 = stack.copy();
                        stack1.setCount(1);
                        this.inputSlotA.put(1, stack1);
                        stack.shrink(1);
                        if (!world.isRemote) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        }
                        changeState();
                        return true;
                    } else if (!this.inputSlotA.get(1).isEmpty() && this.inputSlotA.get(1).isItemEqual(stack)) {
                        int minCount = this.inputSlotA.getStackSizeLimit() - this.inputSlotA.get(1).getCount();
                        minCount = Math.min(stack.getCount(), minCount);
                        this.inputSlotA.get(1).grow(minCount);
                        stack.grow(-minCount);
                        if (!world.isRemote) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        }
                        changeState();
                        return true;
                    }
                } else {
                    if (!outputSlot.isEmpty()) {
                        if (!world.isRemote) {
                            ModUtils.dropAsEntity(world, pos, outputSlot.get(), player);
                        }
                        outputSlot.put(0, ItemStack.EMPTY);
                        if (!world.isRemote) {
                            new PacketUpdateFieldTile(this, "slot2", false);
                        }
                        changeState();
                        return true;
                    } else {
                        if (!inputSlotA.isEmpty()) {
                            if (!world.isRemote) {
                                ModUtils.dropAsEntity(world, pos, inputSlotA.get(), player);
                                ModUtils.dropAsEntity(world, pos, inputSlotA.get(1), player);
                            }
                            inputSlotA.put(0, ItemStack.EMPTY);
                            inputSlotA.put(1, ItemStack.EMPTY);
                            this.output = null;
                            if (!world.isRemote) {
                                new PacketUpdateFieldTile(this, "slot3", false);
                            }
                            changeState();
                            return true;
                        }
                    }
                }
            }
        }


        return this.world.isRemote;
    }

    private void changeState() {
        final ItemStack input = this.inputSlotA.get(0);
        final ItemStack input1 = this.inputSlotA.get(1);
        if (this.outputSlot.isEmpty()) {
            if (!input.isEmpty()) {
                if (input1.isEmpty()) {
                    switch (input.getItemDamage()) {
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
                    switch (input.getItemDamage()) {
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
        }else{
            switch (outputSlot.get().getItemDamage()) {
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

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);


    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
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
