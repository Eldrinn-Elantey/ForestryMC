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

import forestry.core.gui.IContainerSocketed;
import forestry.core.network.DataInputStreamForestry;
import forestry.core.network.IForestryPacketServer;
import forestry.core.network.PacketIdServer;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class PacketSolderingIronClick extends PacketSlotClick implements IForestryPacketServer {

    public PacketSolderingIronClick() {}

    public PacketSolderingIronClick(TileEntity tile, int slot) {
        super(tile, slot);
    }

    @Override
    public void onPacketData(DataInputStreamForestry data, EntityPlayerMP player) throws IOException {
        if (!(player.openContainer instanceof IContainerSocketed)) {
            return;
        }
        ItemStack itemstack = player.inventory.getItemStack();

        ((IContainerSocketed) player.openContainer).handleSolderingIronClickServer(getSlot(), player, itemstack);
    }

    @Override
    public PacketIdServer getPacketId() {
        return PacketIdServer.SOLDERING_IRON_CLICK;
    }
}
