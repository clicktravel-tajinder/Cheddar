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
package com.clicktravel.cheddar.server.http.filter.security;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.clicktravel.cheddar.request.context.AgentSecurityContext;
import com.clicktravel.cheddar.request.context.BasicSecurityContext;
import com.clicktravel.cheddar.request.context.SecurityContextHolder;
import com.clicktravel.common.random.Randoms;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SecurityContextHolder.class })
public class ContainerSecurityRequestFilterTest {

    private static final String CLICK_PLATFORM_AUTHORIZATION_SCHEME = "clickplatform";
    private static final String CLICK_PLATFORM_AGENT_AUTHORIZATION_HEADER = "Agent-Authorization";

    @Test
    public void shouldSetPrincipal_withPrincipalHeader() throws Exception {
        // Given
        mockStatic(SecurityContextHolder.class);
        final String principal = Randoms.randomString();
        final ContainerRequestContext mockContainerRequestContext = mock(ContainerRequestContext.class);
        final MultivaluedMap<String, String> headersMap = new MultivaluedHashMap<>();
        headersMap.add(HttpHeaders.AUTHORIZATION, CLICK_PLATFORM_AUTHORIZATION_SCHEME + " " + principal);
        when(mockContainerRequestContext.getHeaders()).thenReturn(headersMap);
        final ContainerSecurityRequestFilter containerSecurityRequestFilter = new ContainerSecurityRequestFilter();

        // When
        containerSecurityRequestFilter.filter(mockContainerRequestContext);

        // Then
        final ArgumentCaptor<BasicSecurityContext> securityContextCaptor = ArgumentCaptor
                .forClass(BasicSecurityContext.class);
        verifyStatic();
        SecurityContextHolder.set(securityContextCaptor.capture());
        assertThat(securityContextCaptor.getValue().principal(), is(principal));
    }

    @Test
    public void shouldSetPrincipal_withPrincipalHeaderAndAgentHeader() throws Exception {
        // Given
        mockStatic(SecurityContextHolder.class);
        final String principal = Randoms.randomString();
        final String agent = Randoms.randomString();
        final ContainerRequestContext mockContainerRequestContext = mock(ContainerRequestContext.class);
        final MultivaluedMap<String, String> headersMap = new MultivaluedHashMap<>();
        headersMap.add(HttpHeaders.AUTHORIZATION, CLICK_PLATFORM_AUTHORIZATION_SCHEME + " " + principal);
        headersMap.add(CLICK_PLATFORM_AGENT_AUTHORIZATION_HEADER, CLICK_PLATFORM_AUTHORIZATION_SCHEME + " " + agent);
        when(mockContainerRequestContext.getHeaders()).thenReturn(headersMap);
        final ContainerSecurityRequestFilter containerSecurityRequestFilter = new ContainerSecurityRequestFilter();

        // When
        containerSecurityRequestFilter.filter(mockContainerRequestContext);

        // Then
        final ArgumentCaptor<AgentSecurityContext> securityContextCaptor = ArgumentCaptor
                .forClass(AgentSecurityContext.class);
        verifyStatic();
        SecurityContextHolder.set(securityContextCaptor.capture());
        assertThat(securityContextCaptor.getValue().principal(), is(principal));
        assertThat(securityContextCaptor.getValue().agent(), is(agent));
    }

    @Test
    public void shouldNotSetPrincipal_withGeneralAuthorizationHeader() throws Exception {
        // Given
        mockStatic(SecurityContextHolder.class);
        final String authorizationHeader = Randoms.randomString();
        final ContainerRequestContext mockContainerRequestContext = mock(ContainerRequestContext.class);
        final MultivaluedMap<String, String> headersMap = new MultivaluedHashMap<>();
        headersMap.add(HttpHeaders.AUTHORIZATION, authorizationHeader);
        when(mockContainerRequestContext.getHeaders()).thenReturn(headersMap);
        final ContainerSecurityRequestFilter containerSecurityRequestFilter = new ContainerSecurityRequestFilter();

        // When
        containerSecurityRequestFilter.filter(mockContainerRequestContext);

        // Then
        verifyStatic();
        SecurityContextHolder.clear();
    }
}
