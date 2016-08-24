/*
 * Copyright 2014 Click Travel Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.clicktravel.cheddar.system.event.application.lifecycle;

import com.clicktravel.cheddar.system.event.AbstractSystemEvent;

/**
 * System event to indicate a complete group of instances containing a 'new' application version have been deployed and
 * are ready to participate in a blue-green deployment.
 * <p>
 * This event is broadcast to all instances running the 'old' application version.
 */
public class NewAppInstancesDeployedEvent extends AbstractSystemEvent {

    @Override
    public String toString() {
        return "NewAppInstancesDeployedEvent [getTargetApplicationName()=" + getTargetApplicationName()
                + ", getTargetApplicationVersion()=" + getTargetApplicationVersion() + "]";
    }

}
