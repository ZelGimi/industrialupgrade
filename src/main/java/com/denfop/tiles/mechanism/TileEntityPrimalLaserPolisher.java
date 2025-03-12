package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
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
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockPrimalLaserPolisher;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TileEntityPrimalLaserPolisher extends TileEntityInventory implements IUpdateTick, IHasRecipe {

    public final InvSlotRecipes inputSlotA;
    public final InvSlotOutput outputSlot;
    public int progress;
    public MachineRecipe output;
    public int tick = 0;
    public Map<UUID,Double> data;

    public TileEntityPrimalLaserPolisher() {

        this.inputSlotA = new InvSlotRecipes(this, "primal_laser_polisher", this);
        this.progress = 0;
        this.outputSlot = new InvSlotOutput(this, 1);
        Recipes.recipes.addInitRecipes(this);
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.primal_repair3"));
        tooltip.add(Localization.translate("iu.primal_repair6"));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.primalPolisher;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockPrimalLaserPolisher.primal_laser_polisher;
    }

    @Override
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {
        return super.hasCapability(capability, facing) && capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
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
    public void onLoaded() {
        super.onLoaded();
        data = PrimitiveHandler.getPlayersData(EnumPrimitive.LASER);
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
    public void updateEntityServer() {
        super.updateEntityServer();


        this.setActive(!this.inputSlotA.isEmpty());
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

        if (stack.getItem() == IUItem.laser && this.output != null && this.outputSlot.isEmpty() && this.getWorld().isDaytime()) {
            progress += (short) (5 + (short) (data.getOrDefault(player.getUniqueID(),0.0) / 10d));
            this.getCooldownTracker().setTick(10);
            if (progress >= 100) {
                this.progress = 0;
                player.setHeldItem(hand, stack.getItem().getContainerItem(stack));
                if (!this.getWorld().isRemote)
                PrimitiveHandler.addExperience(EnumPrimitive.LASER,0.5,player.getUniqueID());
                this.outputSlot.add(this.output.getRecipe().output.items.get(0));
                this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                if (this.inputSlotA.isEmpty() || this.outputSlot.get().getCount() >= 64) {
                    this.output = null;

                }
                if (!world.isRemote) {
                    new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
                }
            }
            return true;
        } else {
            if (!stack.isEmpty()) {
                if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.accepts(stack, 0)) {
                    ItemStack stack1 = stack.copy();
                    stack1.setCount(1);
                    this.inputSlotA.put(0, stack1);
                    this.output = inputSlotA.process();
                    stack.shrink(1);
                    if (!world.isRemote) {
                        new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    }
                    return true;
                } else if (!outputSlot.isEmpty()) {
                    if (!world.isRemote) {
                        ModUtils.dropAsEntity(world, pos, outputSlot.get(), player);
                    }
                    outputSlot.put(0, ItemStack.EMPTY);
                    if (!world.isRemote) {
                        new PacketUpdateFieldTile(this, "slot2", false);
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
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "primal_laser_polisher",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements, 1, 493))),
                        new RecipeOutput(null, new ItemStack(IUItem.crafting_elements, 1, 495))
                )
        );
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 0))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 0))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 1))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 1))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 2))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 2))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 3))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 3))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 4))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 6))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 5))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 7))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 6))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 8))
        ));

        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 7))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 9))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 8))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 10))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 9))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 11))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 10))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 12))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 10))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 12))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 11))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 14))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 12))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 15))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 13))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 16))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 14))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 17))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 15))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 18))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 16))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 21))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 17))),
                new RecipeOutput(null, new ItemStack(Items.GOLD_INGOT, 1))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 18))),
                new RecipeOutput(null, new ItemStack(Items.IRON_INGOT, 1))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 19))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 22))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 20))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 24))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 22))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 25))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 23))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 26))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, 24))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, 27))
        ));
        for (int i = 25; i < 40; i++) {
            Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                    new Input(input.getInput(new ItemStack(IUItem.rawIngot, 1, i))),
                    new RecipeOutput(null, new ItemStack(IUItem.iuingot, 1, i+3))
            ));
        }
    }

}
