package com.denfop.items.modules;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.utils.CapturedMob;
import com.denfop.utils.ModUtils;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemEntityModule extends ItemMulti<ItemEntityModule.Types> implements IModelRegister {

    protected static final String NAME = "entitymodules";

    public ItemEntityModule() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.tabssp1);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    public static String getMobTypeFromStack(ItemStack item) {


        if (ModUtils.nbt(item) != null) {
            return null;
        }
        return ModUtils.nbt(item).getString("id");
    }



    @Override
    public boolean itemInteractionForEntity(
            final ItemStack stack,
            final EntityPlayer player,
            final EntityLivingBase entity,
            final EnumHand hand
    ) {
        if (entity.getEntityWorld().isRemote) {
            return false;
        }
        if (stack.getItemDamage() == 1) {
            if (entity instanceof EntityPlayer) {
                return false;
            }

            String entityId = EntityList.getEntityString(entity);
            NBTTagCompound root = new NBTTagCompound();
            root.setString("id", entityId);
            if (entity instanceof EntitySheep) {
                root.setInteger("type", ((EntitySheep) entity).getFleeceColor().getColorValue());
            }


            entity.writeToNBT(root);
            root.setString("nameEntity", entity.getName());
            root.setInteger("id_mob", entity.getEntityId());


            CapturedMob capturedMob = CapturedMob.create(entity);

            entity.setDead();
            stack.setCount(stack.getCount() - 1);
            ItemStack stack1 = capturedMob.toStack(this, 1, 1);


            if (!player.inventory.addItemStackToInventory(stack1)) {
                double var8 = 0.7D;
                double var10 = (double) player.getEntityWorld().rand.nextFloat() * var8 + (1.0D - var8) * 0.5D;
                double var12 = (double) player.getEntityWorld().rand.nextFloat() * var8 + (1.0D - var8) * 0.5D;
                double var14 = (double) player.getEntityWorld().rand.nextFloat() * var8 + (1.0D - var8) * 0.5D;
                EntityItem var16 = new EntityItem(
                        player.getEntityWorld(),
                        player.posX + var10,
                        player.posY + var12,
                        player.posZ + var14,
                        stack1
                );
                var16.setDefaultPickupDelay();
                player.getEntityWorld().spawnEntity(var16);
            }
            player.inventoryContainer.detectAndSendChanges();
            return true;
        } else if (stack.getItemDamage() == 0) {

            if (entity instanceof EntityPlayer) {
                ItemStack stack1 = stack.copy();
                NBTTagCompound root = new NBTTagCompound();
                root.setString("name", entity.getDisplayName().getFormattedText());
                entity.writeToNBT(root);
                stack1.setTagCompound(root);
                entity.setDead();
                stack.setCount(stack.getCount() - 1);
                if (player.inventory.addItemStackToInventory(stack1)) {
                } else {
                    double var8 = 0.7D;
                    double var10 = (double) player.getEntityWorld().rand.nextFloat() * var8 + (1.0D - var8) * 0.5D;
                    double var12 = (double) player.getEntityWorld().rand.nextFloat() * var8 + (1.0D - var8) * 0.5D;
                    double var14 = (double) player.getEntityWorld().rand.nextFloat() * var8 + (1.0D - var8) * 0.5D;
                    EntityItem var16 = new EntityItem(
                            player.getEntityWorld(),
                            player.posX + var10,
                            player.posY + var12,
                            player.posZ + var14,
                            stack1
                    );
                    var16.setDefaultPickupDelay();
                    player.getEntityWorld().spawnEntity(var16);
                }
                player.inventoryContainer.detectAndSendChanges();
                return true;
            } else {
                return false;
            }
        }
        return false;


    }


    @Override
    public void addInformation(
            final ItemStack itemStack,
            @Nullable final World worldIn,
            final List<String> info,
            final ITooltipFlag flagIn
    ) {
        super.addInformation(itemStack, worldIn, info, flagIn);
        if (itemStack.getItemDamage() == 1) {



        } else {

            NBTTagCompound nbt = ModUtils.nbt(itemStack);
            if (!(nbt.getString("name").isEmpty())) {
                info.add(nbt.getString("name"));
            }
        }
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public enum Types implements IIdProvider {
        module_player(0),
        module_mob(1),
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
