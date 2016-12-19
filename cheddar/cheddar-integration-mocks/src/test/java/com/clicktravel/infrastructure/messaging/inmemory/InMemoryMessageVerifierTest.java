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
package com.clicktravel.infrastructure.messaging.inmemory;

import static com.clicktravel.common.random.Randoms.randomString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.clicktravel.cheddar.event.Event;
import com.clicktravel.cheddar.infrastructure.messaging.TypedMessage;

@SuppressWarnings("unchecked")
public class InMemoryMessageVerifierTest {

    @Test
    public void shouldReturnTrueWhenMessageSent_onMessageTypeSent() {
        // Given
        final InMemoryMessageSender<TypedMessage> mockInMemoryMessageSender = mock(InMemoryMessageSender.class);
        final List<TypedMessage> mockSentMessages = new ArrayList<>();
        final TypedMessage mockSentMessage = mock(TypedMessage.class);
        final String sentMessageType = randomString();
        when(mockSentMessage.getType()).thenReturn(sentMessageType);
        mockSentMessages.add(mockSentMessage);
        when(mockInMemoryMessageSender.getSentMessages()).thenReturn(mockSentMessages);

        // When
        final boolean messageTypeSent = InMemoryMessageVerifier.messageTypeSent(mockInMemoryMessageSender,
                sentMessageType);

        // Then
        assertTrue(messageTypeSent);
    }

    @Test
    public void shouldReturnFalseWhenMessageNotSent_onMessageTypeSent() {
        // Given
        final InMemoryMessageSender<TypedMessage> mockInMemoryMessageSender = mock(InMemoryMessageSender.class);
        final List<TypedMessage> mockSentMessages = new ArrayList<>();
        when(mockInMemoryMessageSender.getSentMessages()).thenReturn(mockSentMessages);
        final String messageType = randomString();

        // When
        final boolean messageTypeSent = InMemoryMessageVerifier.messageTypeSent(mockInMemoryMessageSender, messageType);

        // Then
        assertFalse(messageTypeSent);
    }

    @Test
    public void shouldReturnTrueWhenEventPublished_onEventTypePublished() {
        // Given
        final InMemoryMessagePublisher<TypedMessage> inMemoryMessagePublisher = mock(InMemoryMessagePublisher.class);
        final List<TypedMessage> mockPublishedMessages = new ArrayList<>();
        final StubEvent stubEvent = new StubEvent();
        stubEvent.setEventField(randomString());
        addSerializedEventMessage(mockPublishedMessages, stubEvent);
        when(inMemoryMessagePublisher.getPublishedMessages()).thenReturn(mockPublishedMessages);
        final Class<? extends Event> eventClass = StubEvent.class;

        // When
        final boolean eventTypePublished = InMemoryMessageVerifier.eventTypePublished(inMemoryMessagePublisher,
                eventClass);

        // Then
        assertTrue(eventTypePublished);
    }

    @Test
    public void shouldReturnFalseWhenEventNotPublished_onEventTypePublished() {
        // Given
        final InMemoryMessagePublisher<TypedMessage> inMemoryMessagePublisher = mock(InMemoryMessagePublisher.class);
        final List<TypedMessage> mockPublishedMessages = new ArrayList<>();
        when(inMemoryMessagePublisher.getPublishedMessages()).thenReturn(mockPublishedMessages);
        final Class<? extends Event> eventClass = StubEvent.class;

        // When
        final boolean eventTypePublished = InMemoryMessageVerifier.eventTypePublished(inMemoryMessagePublisher,
                eventClass);

        // Then
        assertFalse(eventTypePublished);
    }

    @Test
    public void shouldReturnTrueWhenEventPublished_onEventPublished() {
        // Given
        final InMemoryMessagePublisher<TypedMessage> inMemoryMessagePublisher = mock(InMemoryMessagePublisher.class);
        final List<TypedMessage> mockPublishedMessages = new ArrayList<>();
        final StubEvent publishedEvent = new StubEvent();
        publishedEvent.setEventField(randomString());
        addSerializedEventMessage(mockPublishedMessages, publishedEvent);
        when(inMemoryMessagePublisher.getPublishedMessages()).thenReturn(mockPublishedMessages);

        // When
        final boolean eventPublished = InMemoryMessageVerifier.eventPublished(inMemoryMessagePublisher, publishedEvent);

        // Then
        assertTrue(eventPublished);
    }

    @Test
    public void shouldReturnFalseWhenEventNotPublished_onEventPublished() {
        // Given
        final InMemoryMessagePublisher<TypedMessage> inMemoryMessagePublisher = mock(InMemoryMessagePublisher.class);
        final List<TypedMessage> mockPublishedMessages = new ArrayList<>();
        when(inMemoryMessagePublisher.getPublishedMessages()).thenReturn(mockPublishedMessages);
        final StubEvent expectedEvent = new StubEvent();
        expectedEvent.setEventField(randomString());

        // When
        final boolean eventPublished = InMemoryMessageVerifier.eventPublished(inMemoryMessagePublisher, expectedEvent);

        // Then
        assertFalse(eventPublished);
    }

    @Test
    public void shouldReturnPublishedEvents() {
        // Given
        final InMemoryMessagePublisher<TypedMessage> inMemoryMessagePublisher = mock(InMemoryMessagePublisher.class);
        final List<TypedMessage> mockPublishedMessages = new ArrayList<>();
        final StubEvent stubEvent = new StubEvent();
        stubEvent.setEventField(randomString());
        addSerializedEventMessage(mockPublishedMessages, stubEvent);
        final StubOtherEvent stubOtherEvent = new StubOtherEvent();
        addSerializedEventMessage(mockPublishedMessages, stubOtherEvent);
        when(inMemoryMessagePublisher.getPublishedMessages()).thenReturn(mockPublishedMessages);

        // When
        final List<Event> publishedEvents = InMemoryMessageVerifier.publishedEvents(inMemoryMessagePublisher,
                StubEvent.class, StubOtherEvent.class);

        // Then
        final List<Event> expectedEvents = Arrays.asList(stubEvent, stubOtherEvent);
        assertEquals(expectedEvents, publishedEvents);
    }

    private void addSerializedEventMessage(final List<TypedMessage> messages, final Event event) {
        final TypedMessage mockPublishedMessage = mock(TypedMessage.class);
        when(mockPublishedMessage.getType()).thenReturn(event.type());
        when(mockPublishedMessage.getPayload()).thenReturn(event.serialize());
        messages.add(mockPublishedMessage);
    }

    @Test
    public void shouldReturnFilteredPublishedEvents() {
        // Given
        final InMemoryMessagePublisher<TypedMessage> inMemoryMessagePublisher = mock(InMemoryMessagePublisher.class);
        final List<TypedMessage> mockPublishedMessages = new ArrayList<>();
        final StubEvent stubEvent = new StubEvent();
        stubEvent.setEventField(randomString());
        addSerializedEventMessage(mockPublishedMessages, stubEvent);
        final StubOtherEvent stubOtherEvent = new StubOtherEvent();
        addSerializedEventMessage(mockPublishedMessages, stubOtherEvent);
        when(inMemoryMessagePublisher.getPublishedMessages()).thenReturn(mockPublishedMessages);

        // When
        final List<Event> publishedEvents = InMemoryMessageVerifier.publishedEvents(inMemoryMessagePublisher,
                StubOtherEvent.class);

        // Then
        final List<Event> expectedEvents = Arrays.asList(stubOtherEvent);
        assertEquals(expectedEvents, publishedEvents);
    }
}
