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
package forestry.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public abstract class ItemStackUtil {

    public static final ItemStack[] EMPTY_STACK_ARRAY = new ItemStack[0];

    /**
     * Compares item id, damage and NBT. Accepts wildcard damage.
     */
    public static boolean isIdenticalItem(ItemStack lhs, ItemStack rhs) {
        if (lhs == rhs) {
            return true;
        }

        if (lhs == null || rhs == null) {
            return false;
        }

        if (lhs.getItem() != rhs.getItem()) {
            return false;
        }

        if (lhs.getItemDamage() != OreDictionary.WILDCARD_VALUE) {
            if (lhs.getItemDamage() != rhs.getItemDamage()) {
                return false;
            }
        }

        return ItemStack.areItemStackTagsEqual(lhs, rhs);
    }

    /**
     * Merges the giving stack into the receiving stack as far as possible
     */
    public static void mergeStacks(ItemStack giver, ItemStack receptor) {
        if (receptor.stackSize >= 64) {
            return;
        }

        if (!receptor.isItemEqual(giver)) {
            return;
        }

        if (giver.stackSize <= (receptor.getMaxStackSize() - receptor.stackSize)) {
            receptor.stackSize += giver.stackSize;
            giver.stackSize = 0;
            return;
        }

        ItemStack temp = giver.splitStack(receptor.getMaxStackSize() - receptor.stackSize);
        receptor.stackSize += temp.stackSize;
        temp.stackSize = 0;
    }

    /**
     * Creates a split stack of the specified amount, preserving NBT data,
     * without decreasing the source stack.
     */
    public static ItemStack createSplitStack(ItemStack stack, int amount) {
        ItemStack split = new ItemStack(stack.getItem(), amount, stack.getItemDamage());
        if (stack.getTagCompound() != null) {
            NBTTagCompound nbttagcompound =
                    (NBTTagCompound) stack.getTagCompound().copy();
            split.setTagCompound(nbttagcompound);
        }
        return split;
    }

    /**
     */
    public static ItemStack[] condenseStacks(ItemStack[] stacks) {
        List<ItemStack> condensed = new ArrayList<>();

        for (ItemStack stack : stacks) {
            if (stack == null) {
                continue;
            }
            if (stack.stackSize <= 0) {
                continue;
            }

            boolean matched = false;
            for (ItemStack cached : condensed) {
                if ((cached.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(cached, stack))) {
                    cached.stackSize += stack.stackSize;
                    matched = true;
                }
            }

            if (!matched) {
                ItemStack cached = stack.copy();
                condensed.add(cached);
            }
        }

        return condensed.toArray(new ItemStack[condensed.size()]);
    }

    public static boolean containsItemStack(Iterable<ItemStack> list, ItemStack itemStack) {
        for (ItemStack listStack : list) {
            if (isIdenticalItem(listStack, itemStack)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Counts how many full sets are contained in the passed stock
     */
    public static int containsSets(ItemStack[] set, ItemStack[] stock) {
        return containsSets(set, stock, false, false);
    }

    /**
     * Counts how many full sets are contained in the passed stock
     */
    public static int containsSets(ItemStack[] set, ItemStack[] stock, boolean oreDictionary, boolean craftingTools) {
        return containsSets(set, stock, oreDictionary, craftingTools, false);
    }

    /**
     * Counts how many full sets are contained in the passed stock
     */
    public static int containsSets(
            ItemStack[] set, ItemStack[] stock, boolean oreDictionary, boolean craftingTools, boolean matchTags) {
        int totalSets = 0;

        ItemStack[] condensedRequired = ItemStackUtil.condenseStacks(set);
        ItemStack[] condensedOffered = ItemStackUtil.condenseStacks(stock);

        for (ItemStack req : condensedRequired) {

            int reqCount = 0;
            for (ItemStack offer : condensedOffered) {
                if (isCraftingEquivalent(req, offer, oreDictionary, craftingTools)
                        && (!matchTags || ItemStack.areItemStackTagsEqual(req, offer))) {
                    int stackCount = (int) Math.floor((float) offer.stackSize / (float) req.stackSize);
                    reqCount = Math.max(reqCount, stackCount);
                }
            }

            if (reqCount == 0) {
                return 0;
            } else if (totalSets == 0) {
                totalSets = reqCount;
            } else if (totalSets > reqCount) {
                totalSets = reqCount;
            }
        }

        return totalSets;
    }

    public static boolean equalSets(ItemStack[] set1, ItemStack[] set2) {
        if (set1 == set2) {
            return true;
        }

        if (set1 == null || set2 == null) {
            return false;
        }

        if (set1.length != set2.length) {
            return false;
        }

        for (int i = 0; i < set1.length; i++) {
            if (!isIdenticalItem(set1[i], set2[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compare two item stacks for crafting equivalency without oreDictionary or craftingTools
     */
    public static boolean isCraftingEquivalent(ItemStack base, ItemStack comparison) {
        if (base == null || comparison == null) {
            return false;
        }

        if (base.getItem() != comparison.getItem()) {
            return false;
        }

        if (base.getItemDamage() != OreDictionary.WILDCARD_VALUE) {
            if (base.getItemDamage() != comparison.getItemDamage()) {
                return false;
            }
        }

        // When the base stackTagCompound is null or empty, treat it as a wildcard for crafting
        if (base.stackTagCompound == null || base.stackTagCompound.hasNoTags()) {
            return true;
        } else {
            return ItemStack.areItemStackTagsEqual(base, comparison);
        }
    }

    /**
     * Compare two item stacks for crafting equivalency.
     */
    public static boolean isCraftingEquivalent(
            ItemStack base, ItemStack comparison, boolean oreDictionary, boolean craftingTools) {
        if (isCraftingEquivalent(base, comparison)) {
            return true;
        }

        if (base == null || comparison == null) {
            return false;
        }

        if (craftingTools && isCraftingToolEquivalent(base, comparison)) {
            return true;
        }

        if (base.hasTagCompound() && !base.stackTagCompound.hasNoTags()) {
            if (!ItemStack.areItemStacksEqual(base, comparison)) {
                return false;
            }
        }

        if (oreDictionary) {
            int[] idsBase = OreDictionary.getOreIDs(base);
            Arrays.sort(idsBase);
            int[] idsComp = OreDictionary.getOreIDs(comparison);
            Arrays.sort(idsComp);

            // check if the sorted arrays "idsBase" and "idsComp" have any ID in common.
            int iBase = 0;
            int iComp = 0;
            while (iBase < idsBase.length && iComp < idsComp.length) {
                if (idsBase[iBase] < idsComp[iComp]) {
                    iBase++;
                } else if (idsBase[iBase] > idsComp[iComp]) {
                    iComp++;
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isCraftingToolEquivalent(ItemStack base, ItemStack comparison) {
        if (base == null || comparison == null) {
            return false;
        }

        Item baseItem = base.getItem();
        if (baseItem.doesContainerItemLeaveCraftingGrid(base)) {
            return false;
        }

        if (baseItem != comparison.getItem()) {
            return false;
        }

        if (base.stackTagCompound == null || base.stackTagCompound.hasNoTags()) {
            // tool uses meta for damage
            return true;
        } else {
            // tool uses NBT for damage
            if (base.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                return true;
            }
            return base.getItemDamage() == comparison.getItemDamage();
        }
    }

    public static void dropItemStackAsEntity(ItemStack items, World world, double x, double y, double z) {
        dropItemStackAsEntity(items, world, x, y, z, 10);
    }

    public static void dropItemStackAsEntity(
            ItemStack items, World world, double x, double y, double z, int delayForPickup) {
        if (items.stackSize <= 0 || world.isRemote) {
            return;
        }

        float f1 = 0.7F;
        double d = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
        double d1 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
        double d2 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
        EntityItem entityitem = new EntityItem(world, x + d, y + d1, z + d2, items);
        entityitem.delayBeforeCanPickup = delayForPickup;

        world.spawnEntityInWorld(entityitem);
    }

    public static ItemStack copyWithRandomSize(ItemStack template, int max, Random rand) {
        int size = rand.nextInt(max);
        ItemStack created = template.copy();
        created.stackSize = size <= 0 ? 1 : Math.min(size, created.getMaxStackSize());
        return created;
    }

    public static Block getBlock(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof ItemBlock) {
            return ((ItemBlock) item).field_150939_a;
        } else {
            return null;
        }
    }

    public static boolean equals(Block block, ItemStack stack) {
        return block == getBlock(stack);
    }

    public static boolean equals(Block block, int meta, ItemStack stack) {
        return block == getBlock(stack) && meta == stack.getItemDamage();
    }

    public static List<ItemStack> parseItemStackStrings(String[] itemStackStrings, int missingMetaValue) {
        List<Stack> stacks = Stack.parseStackStrings(itemStackStrings, missingMetaValue);
        return getItemStacks(stacks);
    }

    public static List<ItemStack> parseItemStackStrings(String itemStackStrings, int missingMetaValue) {
        List<Stack> stacks = Stack.parseStackStrings(itemStackStrings, missingMetaValue);
        return getItemStacks(stacks);
    }

    private static List<ItemStack> getItemStacks(List<Stack> stacks) {
        List<ItemStack> itemStacks = new ArrayList<>(stacks.size());
        for (Stack stack : stacks) {
            Item item = stack.getItem();
            if (item != null) {
                int meta = stack.getMeta();
                ItemStack itemStack = new ItemStack(item, 1, meta);
                itemStacks.add(itemStack);
            }
        }
        return itemStacks;
    }
}
