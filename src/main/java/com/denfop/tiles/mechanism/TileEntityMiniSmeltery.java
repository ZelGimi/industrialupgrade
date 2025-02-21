package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockDryer;
import com.denfop.blocks.mechanism.BlockMiniSmeltery;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerOilPurifier;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.gui.GuiOilPurifier;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.mini_smeltery.RenderMiniSmeltery;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class TileEntityMiniSmeltery extends TileEntityInventory implements  IHasRecipe {

    public final FluidHandlerRecipe fluid_handler;
    public final Fluids.InternalFluidTank fluidTank1;
    public InvSlotOutput outputSlot;
    protected short progress;

    public TileEntityMiniSmeltery() {
        super();
        this.progress = 0;

        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTankInsert("fluidTank1", 2 * 8 * 144);
        outputSlot = new InvSlotOutput(this, 1);

        this.fluid_handler = new FluidHandlerRecipe("mini_smeltery", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));

        Recipes.recipes.getRecipeFluid().addInitRecipes(this);

    }
    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        for (int i = 1; i < 4; i++) {
            tooltip.add(Localization.translate("mini_smeltery.info" + i));
        }
    }
    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
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
    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1D, 1.0D));

    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenmikhail.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 0)) // mikhail
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenaluminium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 1)) // aluminium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenvanadium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 2)) // vanadium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltentungsten.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 3)) // tungsten
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltencobalt.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 4)) // cobalt
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenmagnesium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 5)) // magnesium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltennickel.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 6)) // nickel
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenplatinum.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 7)) // platinum
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltentitanium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 8)) // titanium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenchromium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 9)) // chromium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenspinel.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 10)) // spinel
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltensilver.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 11)) // silver
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenzinc.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 12)) // zinc
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenmanganese.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 13)) // manganese
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmolteniridium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 14)) // iridium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltengermanium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 15)) // germanium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltencopper.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 16)) // copper
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltengold.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 17)) // gold
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmolteniron.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 18)) // iron
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenlead.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 19)) // lead
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltentin.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 20)) // tin
        ));

        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenosmium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 22)) // osmium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltentantalum.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 23)) // tantalum
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltencadmium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 24)) // cadmium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenarsenic.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 25)) // arsenic
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenbarium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 26)) // barium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenbismuth.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 27)) // bismuth
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltengadolinium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 28)) // gadolinium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltengallium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 29)) // gallium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenhafnium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 30)) // hafnium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenyttrium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 31)) // yttrium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenmolybdenum.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 32)) // molybdenum
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenneodymium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 33)) // neodymium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenniobium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 34)) // niobium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenpalladium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 35)) // palladium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenpolonium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 36)) // polonium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenstrontium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 37)) // strontium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenthallium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 38)) // thallium
        ));
        Recipes.recipes.getRecipeFluid().addRecipe("mini_smeltery", new BaseFluidMachineRecipe(
                new InputFluid(new FluidStack(FluidName.fluidmoltenzirconium.getInstance(), 144)),
                new RecipeOutput(null, new ItemStack(IUItem.rawIngot, 1, 39)) // zirconium
        ));


    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");

    }


    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
        return nbttagcompound;
    }


    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            this.fluid_handler.load();
            new PacketUpdateFieldTile(this, "slot", outputSlot);
            new PacketUpdateFieldTile(this, "fluidtank", fluidTank1);
        }
        if (this.getWorld().isRemote){
            GlobalRenderManager.addRender(world,pos,createFunction(this));
        }
    }
    @SideOnly(Side.CLIENT)
    public Function createFunction(TileEntityMiniSmeltery te){
        Function function= o -> {
            GlStateManager.pushMatrix();
            GlStateManager.translate(te.getPos().getX(), te.getPos().getY() ,
                    te.getPos().getZ());
            RenderMiniSmeltery.render(te);

            GlStateManager.popMatrix();
            return 0;
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
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (this.world.isRemote) {
            return true;
        }
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(
                        CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
                        null
                ) && this.fluidTank1.getFluidAmount() + 1000 <= this.fluidTank1.getCapacity()) {
            ModUtils.interactWithFluidHandler(player, hand,
                    this.getComp(Fluids.class).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
            if (!world.isRemote) {
                new PacketUpdateFieldTile(this, "fluidtank", fluidTank1);
            }
            return true;
        } else {
            if (!outputSlot.isEmpty()) {
                if (!world.isRemote) {
                    ModUtils.dropAsEntity(world, pos, outputSlot.get(), player);
                }
                outputSlot.put(0, ItemStack.EMPTY);
                if (!world.isRemote) {
                    new PacketUpdateFieldTile(this, "slot3", false);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new NBTTagCompound()));
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank1 != null) {
                this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new NBTTagCompound()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onUnloaded() {
        super.onUnloaded();
        if (this.getWorld().isRemote){
            GlobalRenderManager.removeRender(world,pos);
        }
    }

    public void updateEntityServer() {
        super.updateEntityServer();

        if ((this.fluid_handler.output() == null && this.fluidTank1.getFluidAmount() >= 1)) {
            this.fluid_handler.getOutput();
        } else {
            if (this.fluid_handler.output() != null && !this.fluid_handler.checkFluids()) {
                this.fluid_handler.setOutput(null);
            }
        }


        if (this.fluid_handler.output() != null&& this.outputSlot.isEmpty() && this.outputSlot.canAdd(this.fluid_handler
                .output()
                .getOutput().items) && this.fluid_handler.canOperate()) {



            this.progress = (short) (this.progress + 1);
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
                outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new NBTTagCompound()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("fluidtank")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new NBTTagCompound()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot3")) {
            outputSlot.put(0, ItemStack.EMPTY);
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


    public IMultiTileBlock getTeBlock() {
        return BlockMiniSmeltery.mini_smeltery;
    }

    public BlockTileEntity getBlock() {
        return IUItem.mini_smeltery;
    }

    public ContainerOilPurifier getGuiContainer(EntityPlayer entityPlayer) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public GuiOilPurifier getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return null;
    }




}
