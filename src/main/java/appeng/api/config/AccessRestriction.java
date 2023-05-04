package appeng.api.config;

public enum AccessRestriction {
    NO_ACCESS(0),
    READ(1),
    WRITE(2),
    READ_WRITE(3);

    private final int permissionBit;

    AccessRestriction(int v) {
        this.permissionBit = v;
    }

    public boolean hasPermission(AccessRestriction ar) {
        return (this.permissionBit & ar.permissionBit) == ar.permissionBit;
    }

    public AccessRestriction restrictPermissions(AccessRestriction ar) {
        return this.getPermByBit(this.permissionBit & ar.permissionBit);
    }

    private AccessRestriction getPermByBit(int bit) {
        switch (bit) {
            case 0:
            default:
                return NO_ACCESS;
            case 1:
                return READ;
            case 2:
                return WRITE;
            case 3:
                return READ_WRITE;
        }
    }

    public AccessRestriction addPermissions(AccessRestriction ar) {
        return this.getPermByBit(this.permissionBit | ar.permissionBit);
    }

    public AccessRestriction removePermissions(AccessRestriction ar) {
        return this.getPermByBit(this.permissionBit & ~ar.permissionBit);
    }
}
