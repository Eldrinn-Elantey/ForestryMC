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
package forestry.core.genetics.mutations;

import forestry.api.apiculture.IBeeHousing;
import forestry.api.core.IClimateProvider;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutationCondition;
import forestry.core.utils.BlockUtil;
import forestry.core.utils.StringUtil;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class MutationConditionRequiresResourceOreDict implements IMutationCondition {

    private final int oreDictId;
    private final String displayName;

    public MutationConditionRequiresResourceOreDict(String oreDictName) {
        this.oreDictId = OreDictionary.getOreID(oreDictName);

        ArrayList<ItemStack> ores = OreDictionary.getOres(oreDictName);
        if (ores != null && 0 < ores.size()) {
            this.displayName = ores.get(0).getDisplayName();
        } else {
            this.displayName = oreDictName;
        }
    }

    @Override
    public float getChance(
            World world,
            int x,
            int y,
            int z,
            IAllele allele0,
            IAllele allele1,
            IGenome genome0,
            IGenome genome1,
            IClimateProvider climate) {
        /*
        		Block block;
        		int meta;
        		TileEntity tile;
        		int i = 1;
        		do {
        			block = world.getBlock(x, y - i, z);
        			meta = world.getBlockMetadata(x, y - i, z);
        			tile = world.getTileEntity(x, y-i, z);
        			i++;
        		} while (tile instanceof IBeeHousing);
        */
        ItemStack stk = BlockUtil.getItemStackFromBlockBelow(
                world, x, y, z, (TileEntity tile) -> (tile instanceof IBeeHousing));
        int[] oreIds = OreDictionary.getOreIDs(stk);
        for (int oreId : oreIds) if (oreId == this.oreDictId) return 1f;
        return 0f;
    }

    @Override
    public String getDescription() {
        return StringUtil.localizeAndFormat("mutation.condition.resource", displayName);
    }
}
