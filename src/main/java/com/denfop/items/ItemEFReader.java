package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.blocks.ISubEnum;
import com.denfop.gui.GUIEFReader;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStackEvent;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemEFReader extends ItemSubTypes<ItemEFReader.Types> implements IModelRegister, IItemStackInventory,
        IUpdatableItemStackEvent {

    protected static final String NAME = "ef";

    public ItemEFReader() {
        super(Types.class);
        this.setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    @Override
    public IAdvInventory getInventory(final EntityPlayer var1, final ItemStack var2) {
        return new EFReaderInventory(var1, var2);
    }

    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {
        if (IUCore.proxy.isSimulating()) {
            RayTraceResult position = this.rayTrace(world, player, true);
            if (position != null && position.typeOfHit == RayTraceResult.Type.BLOCK && EnergyNetGlobal.instance.getTile(
                    world,
                    position.getBlockPos()
            ) != EnergyNetGlobal.EMPTY) {
                final NBTTagCompound nbt = ModUtils.nbt(player.getHeldItem(hand));
                nbt.setInteger("x", position.getBlockPos().getX());
                nbt.setInteger("y", position.getBlockPos().getY());
                nbt.setInteger("z", position.getBlockPos().getZ());
                player.openGui(IUCore.instance, 1, world, (int) player.posX, (int) player.posY, (int) player.posZ);
                return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
            } else {
                IUCore.proxy.messagePlayer(player, "This block isn`t energyTile");
                return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateField(final String name, final CustomPacketBuffer buffer, final ItemStack stack) {
        if (IUCore.proxy.getGui() != null) {
            GuiScreen gui = (GuiScreen) IUCore.proxy.getGui();
            if (gui instanceof GUIEFReader) {
                GUIEFReader guiefReader = (GUIEFReader) gui;
                guiefReader.readField(name, buffer);
            }
        }
    }

    @Override
    public void updateEvent(final int event, final ItemStack stack) {

    }

    public enum Types implements ISubEnum {
        reader(0),
        ;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.ID;
        }
    }

}
