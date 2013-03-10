/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elasticspring.messaging.support.converter;

import org.elasticspring.messaging.StringMessage;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Agim Emruli
 * @since 1.0
 */
public class NotificationMessageConverterTest {

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	@Test
	public void testWriteMessageNotSupported() throws Exception {
		this.expectedException.expect(UnsupportedOperationException.class);
		new NotificationMessageConverter().toMessage("test");
	}

	@Test
	public void testReadMessage() throws Exception {
		String message = "{ \"Type\" : \"Notification\"," +
				" \"Message\" : \"Hello World!\"}";
		Object result = new NotificationMessageConverter().fromMessage(new StringMessage(message));
		Assert.assertEquals("Hello World!", result);
	}

	@Test
	public void testNoTypeSupplied() throws Exception {
		this.expectedException.expect(MessageConversionException.class);
		this.expectedException.expectMessage("does not contain a Type attribute");
		String message = "{ \"Message\" : \"Hello World!\"}";
		new NotificationMessageConverter().fromMessage(new StringMessage(message));
	}

	@Test
	public void testWrongTypeSupplied() throws Exception {
		this.expectedException.expect(MessageConversionException.class);
		this.expectedException.expectMessage("does is not a valid notification");
		String message = "{ \"Type\" : \"Subscription\"," +
				" \"Message\" : \"Hello World!\"}";
		new NotificationMessageConverter().fromMessage(new StringMessage(message));
	}

	@Test
	public void testNoMessageAvailableSupplied() throws Exception {
		this.expectedException.expect(MessageConversionException.class);
		this.expectedException.expectMessage("does not contain a message");
		String message = "{ \"Type\" : \"Notification\"," +
				" \"Subject\" : \"Hello World!\"}";
		new NotificationMessageConverter().fromMessage(new StringMessage(message));
	}

	@Test
	public void testNoValidJson() throws Exception {
		this.expectedException.expect(MessageConversionException.class);
		this.expectedException.expectMessage("Error reading payload");
		String message = "foo";
		new NotificationMessageConverter().fromMessage(new StringMessage(message));
	}
}
