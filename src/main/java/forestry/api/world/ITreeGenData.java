/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.world;

import com.mojang.authlib.GameProfile;
import forestry.api.arboriculture.ITreeGenome;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface ITreeGenData {

    int getGirth(World world, int x, int y, int z);

    float getHeightModifier();

    boolean canGrow(World world, int x, int y, int z, int expectedGirth, int expectedHeight);

    void setLeaves(World world, GameProfile owner, int x, int y, int z);

    void setLeavesDecorative(World world, GameProfile owner, int x, int y, int z);

    void setLogBlock(World world, int x, int y, int z, ForgeDirection facing);

    boolean allowsFruitBlocks();

    boolean trySpawnFruitBlock(World world, int x, int y, int z);

    ITreeGenome getGenome();
}
