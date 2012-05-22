package burrito.client.crud.input;

import burrito.client.crud.generic.CrudField;
import burrito.client.widgets.inputfield.InputField;
import burrito.client.widgets.validation.HasValidators;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget that can lazily load {@link InputField} widgets
 * 
 * @author henper
 *
 */
public class CrudInputFieldWrapper extends SimplePanel implements HasChangeHandlers {

	private String fieldName;
	private Widget widget;
	private CrudInputField<?> inputField;

	public CrudInputFieldWrapper() {
		setWidget(new Label("Missing registration call. If you see this message, you are missing a call to CrudEditFormHelper.register()."));
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void flagAsMissingField() {
		setWidget(new Label("No such field: \"" + fieldName + "\". Please check the field name reference (fieldName=\"...\"). Did you misspell?"));
	}

	public void init(CrudInputField<?> input) {
		this.inputField = input;
		widget = input.getDisplayWidget();
		setWidget(widget);
	}

	public Widget getWidget() {
		return widget;
	}
	
	public boolean validate() {
		if (widget instanceof HasValidators) {
			return ((HasValidators) widget).validate();
		}
		return true;
	}

	public Object getValue() {
		if (widget instanceof InputField) {
			return ((InputField<?>) widget).getValue();
		}
		return inputField.getValue();
	}

	public CrudField getCrudField() {
		return inputField.getCrudField();
	}

	/**
	 * Adds a change handler to this wrapper. Will proxy to underlying widget if the widget iimplements {@link HasChangeHandlers}. 
	 * Otherwise a {@link UnsupportedOperationException} is thrown. 
	 */
	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		if (widget instanceof HasChangeHandlers) {
			return ((HasChangeHandlers) widget).addChangeHandler(handler);
		}
		throw new UnsupportedOperationException("Field of type " + widget.getClass().getName() 
				+ " does not implement " + HasChangeHandlers.class.getName());
	}

	/**
	 * Focuses the underlying widget
	 */
	public void highlight() {
		if (widget instanceof HasValidators) {
			((HasValidators) widget).highlight();
		}
	}
	
	
	
	
	
}
