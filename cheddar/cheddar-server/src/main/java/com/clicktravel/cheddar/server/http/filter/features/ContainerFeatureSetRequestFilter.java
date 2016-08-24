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
package com.clicktravel.cheddar.server.http.filter.features;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.clicktravel.cheddar.features.FeaturesContext;
import com.clicktravel.cheddar.features.FeaturesContextHolder;

/**
 * Intercept each HTTP request to extract the feature set header and set its value within
 * {@link com.clicktravel.cheddar.features.FeaturesContextHolder}
 */
@Provider
@Priority(Priorities.USER)
public class ContainerFeatureSetRequestFilter implements ContainerRequestFilter {

    private static final String FEATURE_SET_ID_HEADER = "Feature-Set-Id";

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        final MultivaluedMap<String, String> headersMap = requestContext.getHeaders();
        FeaturesContextHolder.clear();
        if (headersMap.containsKey(FEATURE_SET_ID_HEADER)) {
            for (final String featureSetId : headersMap.get(FEATURE_SET_ID_HEADER)) {
                if (!featureSetId.isEmpty()) {
                    FeaturesContextHolder.set(new FeaturesContext(featureSetId));
                }
            }
        }
    }

}
