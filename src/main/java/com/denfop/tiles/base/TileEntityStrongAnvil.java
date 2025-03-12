package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockStrongAnvil;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.invslot.InvSlot;
import com.denfop.items.resource.ItemRawMetals;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
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

public class TileEntityStrongAnvil extends TileEntityInventory implements IUpdateTick, IHasRecipe, IAudioFixer {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(0, 0.0D, -1, 1, 1.0D,
            2
    ));
    private static final List<AxisAlignedBB> aabbs1 = Collections.singletonList(new AxisAlignedBB(-1, 0.0D, 0, 2, 1.0D,
            1
    ));
    public final InvSlotRecipes inputSlotA;
    public final InvSlotOutput outputSlot;
    public int progress;
    public MachineRecipe output;
    public Map<UUID,Double> data;

    public TileEntityStrongAnvil() {

        this.inputSlotA = new InvSlotRecipes(this, "strong_anvil", this){
            @Override
            public boolean accepts(final ItemStack itemStack, final int index) {
                if (index == 4)
                    return super.accepts(itemStack,0);
                return false;
            }
        };
        this.progress = 0;
        this.outputSlot = new InvSlotOutput(this, 1);
        Recipes.recipes.addInitRecipes(this);
    }

    @Override
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {
        return super.hasCapability(capability, facing) && capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        if (!(facing == 4 || facing == 5)) {
            return aabbs1;
        }
        return aabbs;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("primitive_rcm.info"));
        tooltip.add(Localization.translate("primitive_use.info") + IUItem.ObsidianForgeHammer.getItemStackDisplayName());

    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.strong_anvil;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockStrongAnvil.block_strong_anvil;
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
    public List<ItemStack> getSelfDrops(final int fortune, final boolean wrench) {
        List<ItemStack> drop = super.getSelfDrops(fortune, wrench);
        return drop;
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);


    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.data = PrimitiveHandler.getPlayersData(EnumPrimitive.STRONG_ANVIL);
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
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        ItemStack stack = player.getHeldItem(hand);

        if ((stack.getItem() == IUItem.ObsidianForgeHammer) && this.output != null && this.outputSlot.canAdd(
                this.output.getRecipe().output.items.get(
                        0))) {
            progress += 2;
            this.getCooldownTracker().setTick(10);
            if (!this.getWorld().isRemote) {
                this.initiate(0);
            }
            progress += (int) (data.getOrDefault(player.getUniqueID(),0.0)/5D);
            if (progress >= 100) {
                this.progress = 0;
                player.setHeldItem(hand, stack.getItem().getContainerItem(stack));
                if (!this.getWorld().isRemote)
                PrimitiveHandler.addExperience(EnumPrimitive.STRONG_ANVIL,0.5,player.getUniqueID());
                ItemStack stack1 = this.output.getRecipe().output.items.get(0).copy();
                double chance = WorldBaseGen.random.nextDouble();
                if (chance < 0.65){
                    stack1.setCount(1);
                }else if(chance < 0.65+0.325){
                    stack1.setCount(2);
                }else{
                    stack1.setCount(3);
                }
                this.outputSlot.add(stack1);
                this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                if (this.inputSlotA.isEmpty() || this.outputSlot.get().getCount() >= 64) {
                    this.output = null;

                }
                if (!world.isRemote) {
                    new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    new PacketUpdateFieldTile(this, "slot1", this.outputSlot);


                }
            }

            return this.getWorld().isRemote;
        } else {
            if (!stack.isEmpty()) {
                if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.accepts(stack, 4)) {
                    this.inputSlotA.put(0, stack.copy());
                    stack.setCount(0);
                    if (!world.isRemote) {
                        new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    }
                    return true;
                } else if (!this.inputSlotA.get(0).isEmpty() && this.inputSlotA.get(0).isItemEqual(stack)) {
                    int minCount = 64 - this.inputSlotA.get(0).getCount();
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


        return false;
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

    @Override
    public void init() {
        for (int i = 0; i < ItemRawMetals.Types.values().length; i++) {
            String s = ItemRawMetals.Types.values()[i].getName();
            if (s.equals("uranium"))
                continue;
            s = s.substring(0, 1).toUpperCase() + s.substring(1);
            addanvil(
                    "raw"+s,
                    "crushed"+s
            );
        }
    }
    public static void addanvil(String input, String output) {
        ItemStack stack = OreDictionary.getOres(output).get(0).copy();
        final IInputHandler input1 = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "strong_anvil",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input, 1)
                        ),
                        new RecipeOutput(null, stack)
                )
        );


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
        return EnumSound.molot.getSoundEvent();
    }

    @Override
    public void initiate(final int soundEvent) {
        if (soundEvent == 0) {
            this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 0.2F, 1);
        }
    }

    @Override
    public boolean getEnable() {
        return true;
    }

}
