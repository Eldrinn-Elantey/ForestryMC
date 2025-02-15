/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.multiblock;

public interface IMultiblockLogicAlveary extends IMultiblockLogic {
    /**
     * @return the multiblock controller for this logic
     */
    @Override
    IAlvearyController getController();
}
