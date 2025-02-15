/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.network.packets;

import forestry.core.access.IAccessHandler;
import forestry.core.access.IRestrictedAccess;
import forestry.core.network.DataInputStreamForestry;
import forestry.core.network.DataOutputStreamForestry;
import forestry.core.network.IForestryPacketClient;
import forestry.core.network.PacketIdClient;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class PacketAccessUpdate extends PacketCoordinates implements IForestryPacketClient {
    private IAccessHandler accessHandler;

    public PacketAccessUpdate() {}

    public PacketAccessUpdate(IRestrictedAccess restrictedAccess, TileEntity tile) {
        super(tile);
        accessHandler = restrictedAccess.getAccessHandler();
    }

    @Override
    protected void writeData(DataOutputStreamForestry data) throws IOException {
        super.writeData(data);
        accessHandler.writeData(data);
    }

    @Override
    public void onPacketData(DataInputStreamForestry data, EntityPlayer player) throws IOException {
        TileEntity tile = getTarget(player.worldObj);

        if (tile instanceof IRestrictedAccess) {
            IRestrictedAccess restrictedAccessTile = (IRestrictedAccess) tile;
            restrictedAccessTile.getAccessHandler().readData(data);
        }
    }

    @Override
    public PacketIdClient getPacketId() {
        return PacketIdClient.ACCESS_UPDATE;
    }
}
