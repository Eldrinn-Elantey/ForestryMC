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
package forestry.factory.triggers;

import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.IStatementParameter;
import forestry.core.tiles.TileEngine;
import forestry.core.tiles.TilePowered;
import forestry.core.triggers.Trigger;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TriggerLowFuel extends Trigger {

    private float threshold = 0.25F;

    public TriggerLowFuel(String tag, float threshold) {
        super(tag, "lowFuel");
        this.threshold = threshold;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " < " + threshold * 100 + "%";
    }

    /**
     * Return true if the tile given in parameter activates the trigger, given the parameters.
     */
    @Override
    public boolean isTriggerActive(
            TileEntity tile, ForgeDirection side, IStatementContainer source, IStatementParameter[] parameters) {

        if (tile instanceof TilePowered) {
            return !((TilePowered) tile).hasFuelMin(threshold);
        }

        if (tile instanceof TileEngine) {
            TileEngine tileEngine = (TileEngine) tile;
            return !tileEngine.hasFuelMin(threshold);
        }

        return false;
    }
}
