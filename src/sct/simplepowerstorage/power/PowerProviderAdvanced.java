/*
 * SimplePowerStorage
 * Copyright (C) 2013  Ryan Cohen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package sct.simplepowerstorage.power;

import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerProvider;

public class PowerProviderAdvanced extends PowerProvider
{
    public PowerProviderAdvanced()
    {
        powerLoss = 0;
        powerLossRegularity = 72000;

        configure(0, 0);
    }

    public void configure(int maxEnergyReceived, int maxStoredEnergy)
    {
        super.configure(0, 0, maxEnergyReceived, 0, maxStoredEnergy);
    }

    @Override
    public boolean update(IPowerReceptor receptor)
    {
        return false;
    }

    @Override
    public void receiveEnergy(float quantity, ForgeDirection from)
    {
        energyStored += quantity;

        if (energyStored > maxEnergyStored)
        {
            energyStored = maxEnergyStored;
        }
    }
}
