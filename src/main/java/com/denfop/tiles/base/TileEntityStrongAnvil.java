package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.*;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
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

public class TileEntityStrongAnvil extends TileEntityInventory implements IUpdateTick, IHasRecipe, IAudioFixer {

    private static final List<AABB> aabbs = Collections.singletonList(new AABB(0, 0.0D, -1, 1, 1.0D,
            2
    ));
    private static final List<AABB> aabbs1 = Collections.singletonList(new AABB(-1, 0.0D, 0, 2, 1.0D,
            1
    ));
    public final InvSlotRecipes inputSlotA;
    public final InvSlotOutput outputSlot;
    public int progress;
    public MachineRecipe output;
    public Map<UUID, Double> data;

    public TileEntityStrongAnvil(BlockPos pos, BlockState state) {
        super(BlockStrongAnvil.block_strong_anvil, pos, state);
        this.inputSlotA = new InvSlotRecipes(this, "strong_anvil", this) {
            @Override
            public boolean accepts(final ItemStack itemStack, final int index) {
                if (index == 4) {
                    return super.accepts(itemStack, 0);
                }
                return false;
            }
        };
        this.progress = 0;
        this.outputSlot = new InvSlotOutput(this, 1);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addanvil(String input, String output) {
        ItemStack stack = Recipes.inputFactory.getInput(output).getInputs().get(0).copy();
        stack.setCount(2);
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
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK)
            return null;
        return super.getCapability(cap, side);
    }


    public List<AABB> getAabbs(boolean forCollision) {
        if (!(facing == 4 || facing == 5)) {
            return aabbs1;
        }
        return aabbs;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("primitive_rcm.info"));
        tooltip.add(Localization.translate("primitive_use.info") + IUItem.ObsidianForgeHammer.getItem().getDescription().getString());

    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.strong_anvil.getBlock();
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockStrongAnvil.block_strong_anvil;
    }


    @Override
    public List<ItemStack> getSelfDrops(final int fortune, final boolean wrench) {
        List<ItemStack> drop = super.getSelfDrops(fortune, wrench);
        return drop;
    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        this.data = PrimitiveHandler.getPlayersData(EnumPrimitive.STRONG_ANVIL);
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
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        ItemStack stack = player.getItemInHand(hand);

        if ((stack.getItem() == IUItem.ObsidianForgeHammer.getItem()) && this.output != null && this.outputSlot.canAdd(
                this.output.getRecipe().output.items.get(
                        0))) {
            progress += 20;
            this.getCooldownTracker().setTick(10);
            if (!this.getWorld().isClientSide) {
                this.initiate(0);
            }
            progress += (int) (data.getOrDefault(player.getUUID(), 0.0) / 2.5D);
            if (progress >= 100) {
                this.progress = 0;
                player.setItemInHand(hand, stack.getItem().getCraftingRemainingItem(stack));
                if (!this.getWorld().isClientSide) {
                    PrimitiveHandler.addExperience(EnumPrimitive.STRONG_ANVIL, 0.5, player.getUUID());
                }
                ItemStack stack1 = this.output.getRecipe().output.items.get(0).copy();
                double chance = WorldBaseGen.random.nextDouble();
                if (chance < 0.65) {
                    stack1.setCount(1);
                } else if (chance < 0.65 + 0.325) {
                    stack1.setCount(2);
                } else {
                    stack1.setCount(3);
                }
                this.outputSlot.add(stack1);
                this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                if (this.inputSlotA.isEmpty() || this.outputSlot.get(0).getCount() >= 64) {
                    this.output = null;

                }
                if (!level.isClientSide) {
                    new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    new PacketUpdateFieldTile(this, "slot1", this.outputSlot);


                }
            }

            return this.getWorld().isClientSide;
        } else {
            if (!stack.isEmpty()) {
                if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.accepts(stack, 4)) {
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
        for (int i = 0; i < ItemRawMetals.Types.values().length; i++) {
            String s = ItemRawMetals.Types.values()[i].getName();
            switch (i) {
                case 3:
                    s = "tungsten";
                    break;
                case 2:
                    s = "vanady";
                    break;


            }
            if (s.equals("uranium")) {
                continue;
            }
            s = s.substring(0, 1).toUpperCase() + s.substring(1);
            addanvil(
                    "c:raw_materials/" + s,
                    "c:crushed/" + s
            );
        }
        String s = "iron";
        addanvil(
                "c:raw_materials/" + s,
                "c:crushed/" + s
        );
        s = "gold";
        addanvil(
                "c:raw_materials/" + s,
                "c:crushed/" + s
        );
        s = "copper";
        addanvil(
                "c:raw_materials/" + s,
                "c:crushed/" + s
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
            this.getWorld().playSound(null, this.pos, getSound(), SoundSource.BLOCKS, 0.2F, 1);
        }
    }

    @Override
    public boolean getEnable() {
        return true;
    }

}
