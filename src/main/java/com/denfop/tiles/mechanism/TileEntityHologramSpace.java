package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.FakeAsteroid;
import com.denfop.api.space.fakebody.FakePlanet;
import com.denfop.api.space.fakebody.FakeSatellite;
import com.denfop.api.space.fakebody.IFakeAsteroid;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.api.space.fakebody.IFakePlanet;
import com.denfop.api.space.fakebody.IFakeSatellite;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.events.client.SolarSystemRenderer;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;


public class TileEntityHologramSpace extends TileEntityBlock {

    public List<IFakeBody> fakeBodyList = new ArrayList<>();
    @SideOnly(Side.CLIENT)
    SolarSystemRenderer solarSystemRenderer;
    private UUID uuid;

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.getWorld().isRemote) {
            solarSystemRenderer = new SolarSystemRenderer();
            GlobalRenderManager.addRender(world, pos, createFunction(this));
        }
    }

    @SideOnly(Side.CLIENT)
    public Function createFunction(TileEntityHologramSpace te) {
        Function function = o -> {
            solarSystemRenderer.render(te);
            return 0;
        };
        return function;
    }
    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.uuid = nbtTagCompound.getUniqueId("player");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setUniqueId("player", uuid);
        return nbtTagCompound;
    }
    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof EntityPlayer) {
            this.uuid = placer.getUniqueID();
        }
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (this.getWorld().isRemote) {
            GlobalRenderManager.removeRender(world, pos);
        }
    }

    @Override
    public CustomPacketBuffer writeUpdatePacket() {
        this.fakeBodyList = SpaceNet.instance.getFakeSpaceSystem().getBodyMap().get(uuid);
        if (this.fakeBodyList != null) {
            CustomPacketBuffer packetBuffer = new CustomPacketBuffer();
            packetBuffer.writeInt(fakeBodyList.size());
            for (IFakeBody fakeBody : fakeBodyList) {
                if (fakeBody instanceof IFakePlanet) {
                    packetBuffer.writeByte(0);
                }
                if (fakeBody instanceof IFakeSatellite) {
                    packetBuffer.writeByte(1);
                }
                if (fakeBody instanceof IFakeAsteroid) {
                    packetBuffer.writeByte(2);
                }
                if (fakeBody != null) {
                    try {
                        EncoderHandler.encode(packetBuffer, fakeBody.writeNBTTagCompound(new NBTTagCompound()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
           return packetBuffer;
        }else{
            CustomPacketBuffer packetBuffer = new CustomPacketBuffer();
            packetBuffer.writeInt(0);
            return packetBuffer;
        }

    }

    @Override
    public void readUpdatePacket(final CustomPacketBuffer is) {
        super.readUpdatePacket(is);
        int size = is.readInt();
        this.fakeBodyList.clear();
        for (int i = 0; i < size; i++) {
            byte id = is.readByte();
            try {
                if (id == 0) {
                    NBTTagCompound nbt = (NBTTagCompound) DecoderHandler.decode(is);
                    FakePlanet fakePlanet = new FakePlanet(nbt);
                    this.fakeBodyList.add(fakePlanet);
                }
                if (id == 1) {
                    NBTTagCompound nbt = (NBTTagCompound) DecoderHandler.decode(is);
                    FakeSatellite fakePlanet = new FakeSatellite(nbt);
                    this.fakeBodyList.add(fakePlanet);
                }
                if (id == 2) {
                    NBTTagCompound nbt = (NBTTagCompound) DecoderHandler.decode(is);
                    FakeAsteroid fakePlanet = new FakeAsteroid(nbt);
                    this.fakeBodyList.add(fakePlanet);
                }
            } catch (Exception e) {
            }
            ;
        }
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("fakebody")) {
            int size = is.readInt();
            this.fakeBodyList.clear();
            for (int i = 0; i < size; i++) {
                byte id = is.readByte();
                try {
                    if (id == 0) {
                        NBTTagCompound nbt = (NBTTagCompound) DecoderHandler.decode(is);
                        FakePlanet fakePlanet = new FakePlanet(nbt);
                        this.fakeBodyList.add(fakePlanet);
                    }
                    if (id == 1) {
                        NBTTagCompound nbt = (NBTTagCompound) DecoderHandler.decode(is);
                        FakeSatellite fakePlanet = new FakeSatellite(nbt);
                        this.fakeBodyList.add(fakePlanet);
                    }
                    if (id == 2) {
                        NBTTagCompound nbt = (NBTTagCompound) DecoderHandler.decode(is);
                        FakeAsteroid fakePlanet = new FakeAsteroid(nbt);
                        this.fakeBodyList.add(fakePlanet);
                    }
                } catch (Exception e) {
                }
                ;
            }
        }
    }

    @Override
    public boolean needUpdate() {
        return true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();

    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.hologram_space;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
