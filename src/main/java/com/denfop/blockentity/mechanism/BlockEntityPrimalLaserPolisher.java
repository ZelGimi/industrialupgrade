package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.*;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockPrimalLaserPolisherEntity;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BlockEntityPrimalLaserPolisher extends BlockEntityInventory implements IUpdateTick, IHasRecipe {

    public final InventoryRecipes inputSlotA;
    public final InventoryOutput outputSlot;
    public int progress;
    public MachineRecipe output;
    public int tick = 0;
    public Map<UUID, Double> data;

    public BlockEntityPrimalLaserPolisher(BlockPos pos, BlockState state) {
        super(BlockPrimalLaserPolisherEntity.primal_laser_polisher, pos, state);
        this.inputSlotA = new InventoryRecipes(this, "primal_laser_polisher", this);
        this.progress = 0;
        this.outputSlot = new InventoryOutput(this, 1);
        Recipes.recipes.addInitRecipes(this);
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.primal_repair3"));
        tooltip.add(Localization.translate("iu.primal_repair6"));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.primalPolisher.getBlock();
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockPrimalLaserPolisherEntity.primal_laser_polisher;
    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        data = PrimitiveHandler.getPlayersData(EnumPrimitive.LASER);
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
            final Player player,
            final InteractionHand hand,
            final Direction side,
            final Vec3 hitX
    ) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() == IUItem.laser.getItem() && this.output != null && this.outputSlot.isEmpty() && this.getWorld().isDay()) {
            progress += (short) (5 + (short) (data.getOrDefault(player.getUUID(), 0.0) / 10d));
            this.getCooldownTracker().setTick(10);
            if (progress >= 100) {
                this.progress = 0;
                player.setItemInHand(hand, stack.getItem().getCraftingRemainingItem(stack));
                if (!this.getWorld().isClientSide)
                    PrimitiveHandler.addExperience(EnumPrimitive.LASER, 0.5, player.getUUID());
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
            return true;
        } else {
            if (!stack.isEmpty()) {
                if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.canPlaceItem(0, stack)) {
                    ItemStack stack1 = stack.copy();
                    stack1.setCount(1);
                    this.inputSlotA.set(0, stack1);
                    this.output = inputSlotA.process();
                    stack.shrink(1);
                    if (!level.isClientSide) {
                        new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    }
                    return true;
                } else if (!outputSlot.isEmpty()) {
                    if (!level.isClientSide) {
                        ModUtils.dropAsEntity(level, pos, outputSlot.get(0));
                    }
                    outputSlot.set(0, ItemStack.EMPTY);
                    if (!level.isClientSide) {
                        new PacketUpdateFieldTile(this, "slot2", false);
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
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "primal_laser_polisher",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(493)))),
                        new RecipeOutput(null, new ItemStack(IUItem.crafting_elements.getStack(495)))
                )
        );
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(0)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(0)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(1)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(1)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(2)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(2)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(3)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(3)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(4)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(6)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(5)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(7)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(6)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(8)))
        ));

        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(7)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(9)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(8)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(10)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(9)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(11)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(10)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(12)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(10)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(12)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(11)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(14)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(12)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(15)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(13)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(16)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(14)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(17)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(15)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(18)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(16)))),
                new RecipeOutput(null, new ItemStack(Items.COPPER_INGOT))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(17)))),
                new RecipeOutput(null, new ItemStack(Items.GOLD_INGOT, 1))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(18)))),
                new RecipeOutput(null, new ItemStack(Items.IRON_INGOT, 1))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(19)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(22)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(20)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(24)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(22)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(25)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(23)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(26)))
        ));
        Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(24)))),
                new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(27)))
        ));
        for (int i = 25; i < 40; i++) {
            Recipes.recipes.addRecipe("primal_laser_polisher", new BaseMachineRecipe(
                    new Input(input.getInput(new ItemStack(IUItem.rawIngot.getStack(i)))),
                    new RecipeOutput(null, new ItemStack(IUItem.iuingot.getStack(i + 3)))
            ));
        }
    }

}
