package com.denfop.api.tesseract;

import com.denfop.api.energy.IDual;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.invslot.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TesseractLocalSystem {

    Map<BlockPos, ITesseract> tesseractMap = new HashMap<>();
    Map<Integer, ChannelHandler> inputChannels = new HashMap<>();
    Map<Integer, ChannelHandler> outputChannels = new HashMap<>();

    List<Channel> publicChannelList = new LinkedList<>();

    public TesseractLocalSystem() {
    }

    public void add(ITesseract tesseract) {
        if (!tesseractMap.containsKey(tesseract.getBlockPos())) {
            tesseractMap.put(tesseract.getBlockPos(), tesseract);
            for (Channel channel : tesseract.getChannels()) {
                addChannel(channel);
            }
        }
    }

    public void removeChannel(Channel channel) {
        if (channel.getMode() == TypeMode.INOUT) {
            ChannelHandler channelHandler = inputChannels.get(channel.getChannel());
            channelHandler.removeChannel(channel);
            ChannelHandler channelHandler1 = outputChannels.get(channel.getChannel());
            channelHandler1.removeChannel(channel);
        } else if (channel.getMode() == TypeMode.INPUT) {
            ChannelHandler channelHandler = inputChannels.get(channel.getChannel());
            channelHandler.removeChannel(channel);
        } else if (channel.getMode() == TypeMode.OUTPUT) {
            ChannelHandler channelHandler = outputChannels.get(channel.getChannel());
            channelHandler.removeChannel(channel);
        }
        if (!channel.isPrivate()) {
            publicChannelList.remove(channel);
        }
    }

    public void addChannel(Channel channel) {
        if (channel.getMode() == TypeMode.INOUT) {
            ChannelHandler channelHandler = inputChannels.get(channel.getChannel());
            if (channelHandler == null) {
                channelHandler = new ChannelHandler();
                channelHandler.addChannel(channel);
                inputChannels.put(channel.getChannel(), channelHandler);
            } else {
                channelHandler.addChannel(channel);
            }
            ChannelHandler channelHandler1 = outputChannels.get(channel.getChannel());
            if (channelHandler1 == null) {
                channelHandler1 = new ChannelHandler();
                channelHandler1.addChannel(channel);
                outputChannels.put(channel.getChannel(), channelHandler1);
            } else {
                channelHandler1.addChannel(channel);
            }
        } else if (channel.getMode() == TypeMode.INPUT) {
            ChannelHandler channelHandler = inputChannels.get(channel.getChannel());
            if (channelHandler == null) {
                channelHandler = new ChannelHandler();
                channelHandler.addChannel(channel);
                inputChannels.put(channel.getChannel(), channelHandler);
            } else {
                channelHandler.addChannel(channel);
            }
        } else if (channel.getMode() == TypeMode.OUTPUT) {
            ChannelHandler channelHandler = outputChannels.get(channel.getChannel());
            if (channelHandler == null) {
                channelHandler = new ChannelHandler();
                channelHandler.addChannel(channel);
                outputChannels.put(channel.getChannel(), channelHandler);
            } else {
                channelHandler.addChannel(channel);
            }
        }
        if (!channel.isPrivate()) {
            publicChannelList.add(channel);
        }
    }

    public void remove(ITesseract tesseract) {
        ITesseract tesseract1 = tesseractMap.remove(tesseract.getBlockPos());
        if (tesseract1 != null) {
            for (Channel channel : tesseract1.getChannels()) {
                removeChannel(channel);
            }
        }
    }

    public void onTick() {
        for (Map.Entry<Integer, ChannelHandler> entry : this.outputChannels.entrySet()) {
            int channel = entry.getKey();
            ChannelHandler handlerOutput = entry.getValue();
            ChannelHandler handlerInput = this.inputChannels.get(channel);
            if (handlerInput == null) {
                break;
            }
            cycle:
            for (Channel channel1 : handlerOutput.getListEnergy()) {
                if (channel1.isActive()) {
                    for (Channel channel2 : handlerInput.getListEnergy()) {
                        if (!channel2.isActive() || channel1 == channel2) {
                            continue;
                        }
                        if (channel1.isPrivate()) {
                            if (channel2.isPrivate() && channel1.getTesseract().getPlayer().equals(channel2
                                    .getTesseract()
                                    .getPlayer())) {
                                Energy one = channel1.getTesseract().getEnergy();
                                if (one.getEnergy() == 0) {
                                    continue cycle;
                                }
                                Energy two = channel2.getTesseract().getEnergy();
                                final double energy = ((IDual) one.getDelegate()).canExtractEnergy();
                                final double demanded = ((IDual) two.getDelegate()).getDemandedEnergy();
                                final double sent = Math.min(energy, demanded);
                                ((IDual) one.getDelegate()).extractEnergy(sent);
                                ((IDual) two.getDelegate()).receiveEnergy(sent);
                            }
                        } else {
                            if (channel2.isPrivate()) {
                                continue;
                            }
                            Energy one = channel1.getTesseract().getEnergy();
                            if (one.getEnergy() == 0) {
                                continue cycle;
                            }
                            Energy two = channel2.getTesseract().getEnergy();
                            final double energy = ((IDual) one.getDelegate()).canExtractEnergy();
                            final double demanded = ((IDual) two.getDelegate()).getDemandedEnergy();
                            final double sent = Math.min(energy, demanded);
                            ((IDual) one.getDelegate()).extractEnergy(sent);
                            ((IDual) two.getDelegate()).receiveEnergy(sent);
                        }
                    }
                }
            }
            cycle:
            for (Channel channel1 : handlerOutput.getListFluid()) {
                if (channel1.isActive()) {
                    for (Channel channel2 : handlerInput.getListFluid()) {
                        if (!channel2.isActive() || channel1 == channel2) {
                            continue;
                        }
                        if (channel1.isPrivate()) {
                            if (channel2.isPrivate() && channel1.getTesseract().getPlayer().equals(channel2
                                    .getTesseract()
                                    .getPlayer())) {
                                Fluids.InternalFluidTank one = channel1.getTesseract().getTank();
                                Fluids.InternalFluidTank two = channel2.getTesseract().getTank();
                                if (one.getFluidAmount() == 0) {
                                    continue cycle;
                                }
                                FluidStack stack = one.getFluid();
                                if (two.getFluidAmount() == 0) {
                                    two.fill(stack, true);
                                    one.drain(stack.amount, true);
                                } else {
                                    if (two.getFluid().isFluidEqual(stack)) {
                                        final int canFill = two.getCapacity() - two.getFluidAmount();
                                        final int canDrain = stack.amount;
                                        final int sent = Math.min(canFill, canDrain);
                                        two.fill(new FluidStack(two.getFluid().getFluid(), sent), true);
                                        one.drain(sent, true);
                                    }
                                }
                            }
                        } else {
                            if (channel2.isPrivate()) {
                                continue;
                            }
                            Fluids.InternalFluidTank one = channel1.getTesseract().getTank();
                            Fluids.InternalFluidTank two = channel2.getTesseract().getTank();
                            if (one.getFluidAmount() == 0) {
                                continue cycle;
                            }
                            FluidStack stack = one.getFluid();
                            if (two.getFluidAmount() == 0) {
                                two.fill(stack, true);
                                one.drain(stack.amount, true);
                            } else {

                                if (two.getFluid().isFluidEqual(stack)) {
                                    final int canFill = two.getCapacity() - two.getFluidAmount();
                                    final int canDrain = stack.amount;
                                    final int sent = Math.min(canFill, canDrain);
                                    two.fill(new FluidStack(two.getFluid().getFluid(), sent), true);
                                    one.drain(sent, true);
                                }
                            }
                        }
                    }
                }
            }
            cycle:
            for (Channel channel1 : handlerOutput.getListItem()) {
                if (channel1.isActive()) {
                    for (Channel channel2 : handlerInput.getListItem()) {

                        if (!channel2.isActive() || channel1 == channel2) {
                            continue;
                        }
                        if (channel1.isPrivate()) {
                            if (channel2.isPrivate() && channel1.getTesseract().getPlayer().equals(channel2
                                    .getTesseract()
                                    .getPlayer())) {
                                Inventory one = channel1.getTesseract().getSlotItem();
                                Inventory two = channel2.getTesseract().getSlotItem();
                                if (one.isEmpty()) {
                                    continue cycle;
                                }
                                for (int i = 0; i < one.getContents().size(); i++) {
                                    ItemStack stack = one.getContents().get(i);
                                    if (stack.isEmpty()) {
                                        continue;
                                    }
                                    final int countFill = two.addExperimental(stack);
                                    if (countFill == 0) {
                                        one.put(i, ItemStack.EMPTY);
                                    } else {
                                        stack.setCount(countFill);
                                    }

                                }
                            }
                        } else {
                            if (channel2.isPrivate()) {
                                continue;
                            }
                            Inventory one = channel1.getTesseract().getSlotItem();
                            Inventory two = channel2.getTesseract().getSlotItem();
                            if (one.isEmpty()) {
                                continue cycle;
                            }
                            for (int i = 0; i < one.getContents().size(); i++) {
                                ItemStack stack = one.getContents().get(i);
                                if (stack.isEmpty()) {
                                    continue;
                                }
                                final int countFill = two.addExperimental(stack);
                                if (countFill == 0) {
                                    one.put(i, ItemStack.EMPTY);
                                } else {
                                    stack.setCount(countFill);
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    public void onUnload() {
        tesseractMap.clear();
        inputChannels.clear();
        outputChannels.clear();
        publicChannelList.clear();
    }

    public List<Channel> getPublicChannels() {
        return this.publicChannelList;
    }

}
