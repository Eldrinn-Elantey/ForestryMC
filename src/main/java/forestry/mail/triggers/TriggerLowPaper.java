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
package forestry.mail.triggers;

import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.IStatementParameter;
import forestry.core.triggers.Trigger;
import forestry.mail.tiles.TileTrader;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TriggerLowPaper extends Trigger {

    private final int threshold;

    public TriggerLowPaper(String tag, int threshold) {
        super(tag, "lowPaper");
        this.threshold = threshold;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " < " + threshold;
    }

    @Override
    public boolean isTriggerActive(
            TileEntity tile, ForgeDirection side, IStatementContainer source, IStatementParameter[] parameters) {

        if (!(tile instanceof TileTrader)) {
            return false;
        }

        return !((TileTrader) tile).hasPaperMin(threshold);
    }
}
