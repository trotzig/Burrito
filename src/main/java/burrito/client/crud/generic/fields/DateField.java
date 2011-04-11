/**
 * Copyright 2011 Henric Persson (henric.persson@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package burrito.client.crud.generic.fields;

import java.util.Date;

import burrito.client.crud.generic.CrudField;




/**
 * A {@link CrudField} representing a {@link Date}
 * 
 * @author henper
 * 
 */
@SuppressWarnings("serial")
public class DateField extends CrudField {

	private Date date;
	private boolean readOnly;

	public DateField() {
		// default constructor
	}

	public DateField(Date date) {
		this(date, false);
	}

	public DateField(Date date, boolean readOnly) {
		super();
		this.date = date;
		this.readOnly = readOnly;
	}

	@Override
	public Class<?> getType() {
		return Date.class;
	}

	@Override
	public Object getValue() {
		return date;
	}

	@Override
	public void setValue(Object value) {
		this.date = (Date) value;
	}

	public boolean isReadOnly() {
		return readOnly;
	}
}
