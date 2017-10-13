/*
 * Copyright (c) 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.car.hvac.controllers;

import com.android.car.hvac.HvacController;
import com.android.car.hvac.ui.TemperatureBarOverlay;

/**
 * A controller that handles temperature updates for the driver and passenger.
 */
public class TemperatureController {
    private final TemperatureBarOverlay mDriverTempBarExpanded;
    private final TemperatureBarOverlay mPassengerTempBarExpanded;
    private final TemperatureBarOverlay mDriverTempBarCollapsed;
    private final TemperatureBarOverlay mPassengerTempBarCollapsed;
    private final HvacController mHvacController;

    //TODO: builder pattern for clarity
    public TemperatureController(TemperatureBarOverlay passengerTemperatureBarExpanded,
            TemperatureBarOverlay driverTemperatureBarExpanded,
            TemperatureBarOverlay passengerTemperatureBarCollapsed,
            TemperatureBarOverlay driverTemperatureBarCollapsed,
            HvacController controller) {
        mDriverTempBarExpanded = driverTemperatureBarExpanded;
        mPassengerTempBarExpanded = passengerTemperatureBarExpanded;
        mPassengerTempBarCollapsed = passengerTemperatureBarCollapsed;
        mDriverTempBarCollapsed = driverTemperatureBarCollapsed;
        mHvacController = controller;

        mHvacController.registerCallback(mCallback);
        mDriverTempBarExpanded.setTemperatureChangeListener(mDriverTempClickListener);
        mPassengerTempBarExpanded.setTemperatureChangeListener(mPassengerTempClickListener);

        mDriverTempBarExpanded.setTemperature(mHvacController.getDriverTemperature());
        mDriverTempBarCollapsed.setTemperature(mHvacController.getDriverTemperature());
        mPassengerTempBarExpanded.setTemperature(mHvacController.getPassengerTemperature());
        mPassengerTempBarCollapsed.setTemperature(mHvacController.getPassengerTemperature());
    }

    private final HvacController.Callback mCallback = new HvacController.Callback() {
        @Override
        public void onPassengerTemperatureChange(float temp) {
            mPassengerTempBarExpanded.setTemperature((int) temp);
            mPassengerTempBarCollapsed.setTemperature((int) temp);
        }

        @Override
        public void onDriverTemperatureChange(float temp) {
            mDriverTempBarExpanded.setTemperature((int) temp);
            mDriverTempBarCollapsed.setTemperature((int) temp);
        }
    };

    private final TemperatureBarOverlay.TemperatureAdjustClickListener mPassengerTempClickListener =
            new TemperatureBarOverlay.TemperatureAdjustClickListener() {
                @Override
                public void onTemperatureChanged(int temperature) {
                    mHvacController.setPassengerTemperature(temperature);
                    mPassengerTempBarCollapsed.setTemperature(temperature);
                }
            };

    private final TemperatureBarOverlay.TemperatureAdjustClickListener mDriverTempClickListener =
            new TemperatureBarOverlay.TemperatureAdjustClickListener() {
                @Override
                public void onTemperatureChanged(int temperature) {
                    mHvacController.setDriverTemperature(temperature);
                    mDriverTempBarCollapsed.setTemperature(temperature);
                }
            };
}
