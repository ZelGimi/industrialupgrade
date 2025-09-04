package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.*;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockMiniSmelteryEntity;
import com.denfop.componets.Fluids;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.mini_smeltery.RenderMiniSmeltery;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class BlockEntityMiniSmeltery extends BlockEntityInventory implements IHasRecipe {

    public final FluidHandlerRecipe fluid_handler;
    public final Fluids.InternalFluidTank fluidTank1;
    public InventoryOutput outputSlot;
    protected short progress;
    private int amount;

    public BlockEntityMiniSmeltery(BlockPos pos, BlockState blockState) {
        super(BlockMiniSmelteryEntity.mini_smeltery, pos, blockState);
        this.progress = 0;

        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTankInsert("fluidTank1", 2 * 8 * 144);
        outputSlot = new InventoryOutput(this, 1);

        this.fluid_handler = new FluidHandlerRecipe("mini_smeltery", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));

        Recipes.recipes.getRecipeFluid().addInitRecipes(this);

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        for (int i = 1; i < 4; i++) {
            tooltip.add(Localization.translate("mini_smeltery.info" + i));
        }
    }

    public List<AABB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AABB(0.0D, 0.0D, 0.0D, 1.0D, 1D, 1.0D));

    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenmikhail.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(0))) // mikhail
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenaluminium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(1))) // aluminium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenvanadium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(2))) // vanadium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltentungsten.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(3))) // tungsten
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltencobalt.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(4))) // cobalt
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenmagnesium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(5))) // magnesium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltennickel.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(6))) // nickel
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenplatinum.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(7))) // platinum
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltentitanium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(8))) // titanium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenchromium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(9))) // chromium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenspinel.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(10))) // spinel
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltensilver.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(11))) // silver
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenzinc.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(12))) // zinc
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenmanganese.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(13))) // manganese
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmolteniridium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(14))) // iridium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltengermanium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(15))) // germanium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltencopper.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(16))) // copper
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltengold.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(17))) // gold
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmolteniron.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(18))) // iron
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenlead.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(19))) // lead
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltentin.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(20))) // tin
        ));

        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenosmium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(22))) // osmium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltentantalum.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(23))) // tantalum
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltencadmium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(24))) // cadmium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenarsenic.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(25))) // arsenic
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenbarium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(26))) // barium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenbismuth.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(27))) // bismuth
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltengadolinium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(28))) // gadolinium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltengallium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(29))) // gallium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenhafnium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(30))) // hafnium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenyttrium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(31))) // yttrium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenmolybdenum.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(32))) // molybdenum
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenneodymium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(33))) // neodymium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenniobium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(34))) // niobium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenpalladium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(35))) // palladium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenpolonium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(36))) // polonium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenstrontium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(37))) // strontium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenthallium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(38))) // thallium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenzirconium.getInstance().get(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot.getStack(39))) // zirconium
        ));


    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");

    }


    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putShort("progress", this.progress);
        return nbttagcompound;
    }


    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide()) {
            this.fluid_handler.load();
            new PacketUpdateFieldTile(this, "slot", outputSlot);
            new PacketUpdateFieldTile(this, "fluidtank", fluidTank1);
        }
        if (this.getWorld().isClientSide) {
            GlobalRenderManager.addRender(level, worldPosition, createFunction(this));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public Function<RenderLevelStageEvent, Void> createFunction(BlockEntityMiniSmeltery te) {
        Function<RenderLevelStageEvent, Void> function = o -> {
            o.getPoseStack().pushPose();
            o.getPoseStack().translate(te.getPos().getX(), te.getPos().getY(),
                    te.getPos().getZ());
            RenderMiniSmeltery.render(te, o);

            o.getPoseStack().popPose();
            return null;
        };
        return function;
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        try {
            EncoderHandler.encode(customPacketBuffer, outputSlot);
            EncoderHandler.encode(customPacketBuffer, fluidTank1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (this.level.isClientSide) {
            return true;
        }
        if (!this.getWorld().isClientSide && player
                .getItemInHand(hand)
                .getCapability(
                        Capabilities.FluidHandler.ITEM,
                        null
                ) != null && this.fluidTank1.getFluidAmount() + 1000 <= this.fluidTank1.getCapacity()) {
            ModUtils.interactWithFluidHandler(player, hand,
                    this.getComp(Fluids.class).getCapability(Capabilities.FluidHandler.BLOCK, side)
            );
            if (!level.isClientSide) {
                new PacketUpdateFieldTile(this, "fluidtank", fluidTank1);
            }
            return true;
        } else {
            if (!outputSlot.isEmpty()) {
                if (!level.isClientSide) {
                    ModUtils.dropAsEntity(level, pos, outputSlot.get(0));
                }
                outputSlot.set(0, ItemStack.EMPTY);
                if (!level.isClientSide) {
                    new PacketUpdateFieldTile(this, "slot3", false);
                }
                return true;
            }
        }
        return true;
    }


    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            outputSlot.readFromNbt(customPacketBuffer.registryAccess(), ((Inventory) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(customPacketBuffer.registryAccess(), new CompoundTag()));
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank1 != null) {
                this.fluidTank1.readFromNBT(customPacketBuffer.registryAccess(), fluidTank1.writeToNBT(customPacketBuffer.registryAccess(), new CompoundTag()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onUnloaded() {
        super.onUnloaded();
        if (this.getWorld().isClientSide) {
            GlobalRenderManager.removeRender(level, worldPosition);
        }
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (amount != fluidTank1.getFluidAmount()) {
            amount = fluidTank1.getFluidAmount();
            new PacketUpdateFieldTile(this, "fluidtank", fluidTank1);
        }
        if ((this.fluid_handler.output() == null && this.fluidTank1.getFluidAmount() >= 1)) {
            this.fluid_handler.getOutput();
        } else {
            if (this.fluid_handler.output() != null && !this.fluid_handler.checkFluids()) {
                this.fluid_handler.setOutput(null);
            }
        }


        if (this.fluid_handler.output() != null && this.outputSlot.isEmpty() && this.outputSlot.canAdd(this.fluid_handler
                .output()
                .getOutput().items) && this.fluid_handler.canOperate()) {


            this.progress = (short) (this.progress + 2);
            double k = this.progress;

            if (this.progress >= 100) {

                operate();
                new PacketUpdateFieldTile(this, "fluidtank", fluidTank1);
                new PacketUpdateFieldTile(this, "slot", outputSlot);
                this.progress = 0;
            }
        } else {


            if (this.fluid_handler.output() == null) {
                this.progress = 0;
            }

        }

    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("slot")) {
            try {
                outputSlot.readFromNbt(is.registryAccess(), ((Inventory) (DecoderHandler.decode(is))).writeToNbt(is.registryAccess(), new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("fluidtank")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank1.readFromNBT(is.registryAccess(), fluidTank1.writeToNBT(is.registryAccess(), new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot3")) {
            outputSlot.set(0, ItemStack.EMPTY);
        }
    }


    public void operate() {
        for (int i = 0; i < 1; i++) {
            operateOnce();

            this.fluid_handler.checkOutput();
            if (this.fluid_handler.output() == null) {
                break;
            }
        }
    }

    public void operateOnce() {
        this.fluid_handler.consume();
        this.outputSlot.add(this.fluid_handler.output().getOutput().items);
        new PacketUpdateFieldTile(this, "slot", outputSlot);
        new PacketUpdateFieldTile(this, "fluidtank", fluidTank1);
    }


    public MultiBlockEntity getTeBlock() {
        return BlockMiniSmelteryEntity.mini_smeltery;
    }

    public BlockTileEntity getBlock() {
        return IUItem.mini_smeltery.getBlock();
    }


}
