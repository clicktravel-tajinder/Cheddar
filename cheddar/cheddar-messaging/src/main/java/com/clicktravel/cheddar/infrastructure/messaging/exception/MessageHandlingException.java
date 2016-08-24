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
package com.clicktravel.cheddar.infrastructure.messaging.exception;

public class MessageHandlingException extends MessagingException {

    private static final long serialVersionUID = 5991755885193413206L;

    public MessageHandlingException(final String message) {
        super(message);
    }

    public MessageHandlingException(final Throwable cause) {
        super(cause);
    }

    public MessageHandlingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
