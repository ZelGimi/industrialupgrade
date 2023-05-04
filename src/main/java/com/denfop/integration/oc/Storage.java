package com.denfop.integration.oc;

import com.denfop.api.IStorage;
import li.cil.oc.api.Network;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;

public class Storage extends AbstractManagedEnvironment implements ManagedPeripheral, NamedBlock {

    private final IStorage storage;

    public Storage(IStorage storage) {
        this.storage = storage;
        this.setNode(Network.newNode(this, Visibility.Network).withComponent("eu_storage", Visibility.Network).create());
    }

    public String[] methods() {
        return new String[]{"getEUCapacity", "getRFCapacity", "getTier", "getRedstoneMode", "shouldEmitRedstone", "shouldEmitEnergy", "getEUStored", "getRFStored", "getOutput"};
    }

    public Object[] invoke(String method, Context context, Arguments args) {
        switch (method) {
            case "getEUCapacity":
                return new Object[]{storage.getEUCapacity()};
            case "getRFCapacity":
                return new Object[]{storage.getRFCapacity()};
            case "getTier":
                return new Object[]{storage.getTier()};
            case "getRedstoneMode":
                return new Object[]{storage.getRedstoneMode()};
            case "shouldEmitRedstone":
                return new Object[]{storage.shouldEmitRedstone()};
            case "shouldEmitEnergy":
                return new Object[]{storage.shouldEmitEnergy()};
            case "getEUStored":
                return new Object[]{storage.getEUStored()};
            case "getRFStored":
                return new Object[]{storage.getRFStored()};
            case "getOutput":
                return new Object[]{storage.getOutput()};
            default:
                return new Object[]{};
        }

    }

    public String preferredName() {
        return "eu_storage";
    }

    public int priority() {
        return 5;
    }

}
