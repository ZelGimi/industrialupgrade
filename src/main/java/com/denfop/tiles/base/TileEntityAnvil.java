package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TileEntityAnvil extends TileEntityInventory implements IUpdateTick, IHasRecipe {

    public final InvSlotRecipes inputSlotA;
    public final InvSlotOutput outputSlot;
    public int progress;
    public MachineRecipe output;
    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(0, 0.0D, -1, 1, 1.0D,
            2
    ));
    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return aabbs;
    }
    public TileEntityAnvil() {

        this.inputSlotA = new InvSlotRecipes(this, "anvil", this);
        this.progress = 0;
        this.outputSlot = new InvSlotOutput(this, 1);
        Recipes.recipes.addInitRecipes(this);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.anvil;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockAnvil.block_anvil;
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
        if(!this.getWorld().isRemote){
            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
            new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
        }
        this.output = inputSlotA.process();
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if(name.equals("slot")) {
            try {
                inputSlotA.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new NBTTagCompound()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(name.equals("slot1")) {
            try {
                outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new NBTTagCompound()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
            EncoderHandler.encode(customPacketBuffer,inputSlotA);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            EncoderHandler.encode(customPacketBuffer,outputSlot);
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

        if (stack.getItem() == IUItem.ForgeHammer && this.output != null && this.outputSlot.canAdd(this.output.getRecipe().output.items.get(
                0))) {
            progress += 10;
            if (progress >= 100) {
                this.progress = 0;
                this.outputSlot.add(this.output.getRecipe().output.items.get(0));
                this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                if (this.inputSlotA.isEmpty() || this.outputSlot.get().getCount() >= 64) {
                    this.output = null;
                }
            }

            return true;
        } else {
            if (!stack.isEmpty()) {
                if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.accepts(stack, 0)) {
                    this.inputSlotA.put(0, stack.copy());
                    stack.setCount(0);
                    return true;
                } else if (!this.inputSlotA.get(0).isEmpty() && this.inputSlotA.get(0).isItemEqual(stack)) {
                    int minCount = 64 - this.inputSlotA.get(0).getCount();
                    minCount = Math.min(stack.getCount(), minCount);
                    this.inputSlotA.get(0).grow(minCount);
                    stack.grow(-minCount);
                    return true;
                }
            } else {
                if (!outputSlot.isEmpty()) {
                    if (!world.isRemote) {
                        ModUtils.dropAsEntity(world, pos, outputSlot.get());
                    }
                    outputSlot.get().setCount(0);
                    return true;
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
        for (String s : RegisterOreDictionary.list_string) {
            Recipes.recipes.addRecipe(
                    "anvil",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput("ingot" + s, 1)

                            ),
                            new RecipeOutput(null, OreDictionary.getOres("plate" + s))
                    )
            );
        }
    }

}
