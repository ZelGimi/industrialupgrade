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
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TileEntityMacerator extends TileEntityInventory implements IUpdateTick, IAudioFixer {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(0, 0.0D, 0, 1, 1.25D,
            1
    ));
    public final InvSlotRecipes inputSlotA;
    public final InvSlotOutput outputSlot;
    public int progress;
    public MachineRecipe output;
    public int durability = 96;
    public Map<UUID,Double> data = PrimitiveHandler.getPlayersData(EnumPrimitive.MACERATOR);

    public TileEntityMacerator() {

        this.inputSlotA = new InvSlotRecipes(this, "macerator", this) {
            @Override
            public boolean accepts(final ItemStack itemStack, final int index) {
                if (index == 4){
                   int[] ids = OreDictionary.getOreIDs(itemStack);
                   for (int i : ids){
                       String name = OreDictionary.getOreName(i);
                       if (name.startsWith("ore") || name.startsWith("raw"))
                           return false;
                   }
                    return super.accepts(itemStack,0);
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
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {
        return super.hasCapability(capability, facing) && capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.primal_repair1"));
    }

    @Override
    public List<ItemStack> getSelfDrops(final int fortune, final boolean wrench) {
        List<ItemStack> drop = super.getSelfDrops(fortune, wrench);
        ItemStack stack = drop.get(0);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setInteger("durability", durability);
        return drop;
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        if (nbt.hasKey("durability")) {
            durability = nbt.getInteger("durability");
        }
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return aabbs;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.blockMacerator;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockMacerator.macerator;
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
    public EnumTypeAudio getType() {
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
    public boolean onSneakingActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        ItemStack stack = player.getHeldItem(hand);
        if (durability >= 0 && durability < 96 && stack.getItem() == IUItem.crafting_elements && stack.getItemDamage() == 41) {
            durability = 96;
            stack.shrink(1);
            new PacketUpdateFieldTile(this, "durability", this.durability);
        }
        return super.onSneakingActivated(player, hand, side, hitX, hitY, hitZ);
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
            if (stack.isEmpty() && this.output != null && this.outputSlot.isEmpty() && this.inputSlotA.continue_process(this.output) && durability > 0) {
                progress += (int) (2 + (data.getOrDefault(player.getUniqueID(),0.0)/20d));
                this.getCooldownTracker().setTick(10);
                this.setActive(!this.getActive());
                if (!this.getWorld().isRemote) {
                    this.initiate(0);
                }
                if (progress >= 100) {
                    this.progress = 0;
                    this.setActive(false);
                    if (!this.getWorld().isRemote)
                    PrimitiveHandler.addExperience(EnumPrimitive.MACERATOR,0.75,player.getUniqueID());
                    durability--;
                    this.outputSlot.add(this.output.getRecipe().output.items.get(0));
                    this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                    this.output = null;
                    if (!world.isRemote) {
                        new PacketUpdateFieldTile(this, "slot3", this.inputSlotA);
                        new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
                    }
                }

                return this.getWorld().isRemote;
            } else {
                if (!stack.isEmpty()) {
                    if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.accepts(stack, 4)) {
                        final ItemStack stack1 = stack.copy();
                        stack1.setCount(1);
                        this.inputSlotA.put(0, stack1);
                        stack.shrink(1);
                        if (!world.isRemote) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        }
                        return true;
                    } else if (!this.inputSlotA.get(0).isEmpty() && this.inputSlotA.get(0).isItemEqual(stack)) {
                        int minCount = this.inputSlotA.getStackSizeLimit() - this.inputSlotA.get(0).getCount();
                        minCount = Math.min(stack.getCount(), minCount);
                        this.inputSlotA.get(0).grow(minCount);
                        stack.grow(-minCount);
                        if (!world.isRemote) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        }
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
                        return true;
                    } else {
                        if (!inputSlotA.isEmpty()) {
                            if (!world.isRemote) {
                                ModUtils.dropAsEntity(world, pos, inputSlotA.get(), player);
                            }
                            inputSlotA.put(0, ItemStack.EMPTY);
                            this.output = null;
                            if (!world.isRemote) {
                                new PacketUpdateFieldTile(this, "slot3", false);
                            }
                            return true;
                        }
                    }
                }
            }
        }


        return this.world.isRemote;
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
