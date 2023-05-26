package com.denfop.tiles.mechanism.triple.heat;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.*;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.HeatComponent;
import com.denfop.container.ContainerTripleElectricMachine;
import com.denfop.gui.GuiAdvAlloySmelter;
import com.denfop.tiles.base.EnumTripleElectricMachine;
import com.denfop.tiles.base.TileEntityTripleElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityAdvAlloySmelter extends TileEntityTripleElectricMachine implements IHasRecipe, IType {

    public final HeatComponent heat;
    private boolean auto;

    public TileEntityAdvAlloySmelter() {
        super(1, 300, 1, Localization.translate("iu.AdvAlloymachine.name"), EnumTripleElectricMachine.ADV_ALLOY_SMELTER);
        this.auto = false;
        this.heat = this.addComponent(HeatComponent
                .asBasicSink(this, 5000));
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addAlloysmelter(String container, String fill, String fill1, ItemStack output, int temperature) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("advalloysmelter", new BaseMachineRecipe(
                new Input(input.forOreDict(container), input.forOreDict(fill), input.forOreDict(fill1)),
                new RecipeOutput(nbt, output)
        ));

    }

    @Override

    public void init() {
        addAlloysmelter("ingotCopper", "ingotZinc", "ingotLead", new ItemStack(IUItem.alloysingot, 1, 3), 4500);
        addAlloysmelter("ingotAluminium", "ingotMagnesium", "ingotManganese", new ItemStack(IUItem.alloysingot, 1, 5), 4000);
        addAlloysmelter("ingotAluminum", "ingotMagnesium", "ingotManganese", new ItemStack(IUItem.alloysingot, 1, 5), 4000);

        addAlloysmelter("ingotAluminium",
                "ingotCopper", "ingotTin",
                new ItemStack(IUItem.alloysingot, 1, 0), 3000
        );
        addAlloysmelter("ingotAluminum",
                "ingotCopper", "ingotTin",
                new ItemStack(IUItem.alloysingot, 1, 0), 3000
        );
        addAlloysmelter("ingotAluminium",
                "ingotVanadium", "ingotCobalt",
                new ItemStack(IUItem.alloysingot, 1, 6), 4500
        );
        addAlloysmelter("ingotAluminum",
                "ingotVanadium", "ingotCobalt",
                new ItemStack(IUItem.alloysingot, 1, 6), 4500
        );
        addAlloysmelter("ingotChromium",
                "ingotTungsten", "ingotNickel",
                new ItemStack(IUItem.alloysingot, 1, 7), 5000
        );
    }

    protected List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.auto) {
            ret.add(new ItemStack(IUItem.autoheater));
            this.auto = false;
        }
        return ret;
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
        final ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem().equals(IUItem.autoheater) && !this.auto) {
            this.auto = true;
            stack.shrink(1);
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.auto = nbttagcompound.getBoolean("auto");
    }


    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        if (this.auto) {
            if (this.heat.getEnergy() + 1 <= this.heat.getCapacity()) {
                this.heat.addEnergy(2);
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("auto", this.auto);
        return nbttagcompound;
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAdvAlloySmelter(new ContainerTripleElectricMachine(entityPlayer, this, type));
    }

    @Override
    public void operateOnce(final MachineRecipe output, final List<ItemStack> processResult) {
        this.inputSlotA.consume();
        this.outputSlot.add(processResult);
    }

    public String getStartSoundFile() {
        return "Machines/alloysmelter.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        sound = !sound;
        IUCore.network.get(true).updateTileEntityField(this, "sound");

        if (!sound) {
            if (this.getType() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                IC2.network.get(true).initiateTileEntityEvent(this, 2, true);

            }
        }
    }

}
