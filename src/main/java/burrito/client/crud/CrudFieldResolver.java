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

package burrito.client.crud;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.AdminLinkMethodField;
import burrito.client.crud.generic.fields.BBCodeField;
import burrito.client.crud.generic.fields.BooleanField;
import burrito.client.crud.generic.fields.DateField;
import burrito.client.crud.generic.fields.DisplayableMethodField;
import burrito.client.crud.generic.fields.EmbeddedListField;
import burrito.client.crud.generic.fields.ImageField;
import burrito.client.crud.generic.fields.IntegerListField;
import burrito.client.crud.generic.fields.LinkListField;
import burrito.client.crud.generic.fields.LinkedEntityField;
import burrito.client.crud.generic.fields.ListedByEnumField;
import burrito.client.crud.generic.fields.LongListField;
import burrito.client.crud.generic.fields.ManyToManyRelationField;
import burrito.client.crud.generic.fields.ManyToOneRelationField;
import burrito.client.crud.generic.fields.RichTextField;
import burrito.client.crud.generic.fields.StringField;
import burrito.client.crud.generic.fields.StringListField;
import burrito.client.crud.generic.fields.StringSelectionField;
import burrito.client.crud.input.BBCodeInputField;
import burrito.client.crud.input.BooleanCrudInputField;
import burrito.client.crud.input.CrudInputField;
import burrito.client.crud.input.CrudInputFieldImpl;
import burrito.client.crud.input.DateCrudInputField;
import burrito.client.crud.input.EmbeddedListInputField;
import burrito.client.crud.input.ImageCrudInputField;
import burrito.client.crud.input.IntegerInputField;
import burrito.client.crud.input.IntegerListInputField;
import burrito.client.crud.input.LinkListInputField;
import burrito.client.crud.input.LinkedEntityInputField;
import burrito.client.crud.input.ListCrudInputField;
import burrito.client.crud.input.ListedByEnumSelectionListField;
import burrito.client.crud.input.LongListInputField;
import burrito.client.crud.input.ReadOnlyTextCrudInputField;
import burrito.client.crud.input.RichTextInputField;
import burrito.client.crud.input.SelectionListField;
import burrito.client.crud.input.StringListInputField;
import burrito.client.crud.input.StringSelectionListField;
import burrito.client.widgets.inputfield.LongInputField;
import burrito.client.widgets.inputfield.StringInputField;
import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBoxBase;

/**
 * Utility class that knows how to translate a {@link CrudField} (which is a
 * description of an entity field) to a {@link CrudInputField} (which is a class
 * that decides which widget to use for user input)
 * 
 * @author henper
 * 
 */
public class CrudFieldResolver {

	private static List<CrudFieldPluginHandler> pluginHandlers = new ArrayList<CrudFieldPluginHandler>();
	
	public static class RegexpInputFieldValidator implements
			InputFieldValidator {

		private String regexpDescription;
		private String regexpPattern;

		public RegexpInputFieldValidator(String regexpPattern,
				String regexpDescription) {
			this.regexpPattern = regexpPattern;
			this.regexpDescription = regexpDescription;
		}

		public void validate(String value) throws ValidationException {
			if (!value.matches(regexpPattern)) {
				throw new ValidationException(regexpDescription);
			}
		}

	}

	/**
	 * Resolve and create an {@link CrudInputField} from a {@link CrudField}
	 * 
	 * @param field
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static CrudInputField createInputField(CrudField field, CrudServiceAsync service) {
		for (CrudFieldPluginHandler pluginHandler : pluginHandlers) {
			CrudInputField<?> pluggedIn = pluginHandler.process(field);
			if (pluggedIn != null) {
				return pluggedIn;
			}
		}
		// First handle specific cases:
		if (field instanceof DisplayableMethodField || field instanceof AdminLinkMethodField) {
			//this field has no input
			return null;
		}
		
		
		if (field instanceof ManyToOneRelationField) {
			return new SelectionListField((ManyToOneRelationField) field, service);
		}
		if (field instanceof ImageField) {
			return new ImageCrudInputField((ImageField) field);
		}
		if (field instanceof RichTextField) {
			return new RichTextInputField((RichTextField) field);
		}
		if (field instanceof BBCodeField) {
			return new BBCodeInputField((BBCodeField) field);
		}
		if (field instanceof StringSelectionField) {
			return new StringSelectionListField((StringSelectionField) field);
		}
		if (field instanceof ListedByEnumField) {
			return new ListedByEnumSelectionListField((ListedByEnumField) field);
		}
		if (field instanceof LinkListField) {
			return new LinkListInputField((LinkListField) field);
		}
		if (field instanceof StringListField) {
			return new StringListInputField((StringListField) field);
		}
		if (field instanceof LongListField) {
			return new LongListInputField((LongListField)field);
		}
		if (field instanceof IntegerListField) {
			return new IntegerListInputField((IntegerListField)field);
		}
		if (field instanceof LinkedEntityField) {
			return new LinkedEntityInputField((LinkedEntityField) field);
		}
		if (field instanceof EmbeddedListField) {
			return new EmbeddedListInputField((EmbeddedListField) field);
		}
		
		
		

		// Fallback to raw type input:
		if (field.getType() == Date.class) {
			return new DateCrudInputField((DateField) field);
		}
		if (field.getType() == Long.class) {
			return new CrudInputFieldImpl<Long>(field, new LongInputField(field
					.isRequired()), (Long) field.getValue());
		}
		if (field.getType() == Integer.class) {
			return new CrudInputFieldImpl<Integer>(field,
					new IntegerInputField(field.isRequired()),
					(Integer) field.getValue());
		}
		if (field.getType() == String.class) {
			final StringField sf = (StringField) field;
			if (sf.isReadOnly()) {
				return new ReadOnlyTextCrudInputField(sf);
			}
			else {
				StringInputField stringInputField;
				if (sf.isRenderAsTextArea()) {
					stringInputField = new StringInputField(field.isRequired()) {
						@Override
						protected TextBoxBase createField() {
							return new TextArea();
						}
					};
				} else {
					stringInputField = new StringInputField(field.isRequired());
				}
				if (sf.getRegexpPattern() != null) {
					stringInputField
							.addInputFieldValidator(new RegexpInputFieldValidator(
									sf.getRegexpPattern(), sf
											.getRegexpDescription()));
				}
				return new CrudInputFieldImpl<String>(field, stringInputField,
						(String) field.getValue());
			}
		}
		if (field.getType() == Boolean.class) {
			return new BooleanCrudInputField((BooleanField) field);
		}
		if (field.getType() == List.class) {
			return new ListCrudInputField((ManyToManyRelationField) field);
		}
		return null;
	}
	
	
	public static void addPluginHandler(CrudFieldPluginHandler handler) {
		pluginHandlers.add(handler);
	}
	

}
