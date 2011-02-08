package burrito.client.widgets.inputfield;

import java.util.ArrayList;
import java.util.List;

import burrito.client.widgets.validation.HasValidators;
import burrito.client.widgets.validation.InputFieldValidator;
import burrito.client.widgets.validation.ValidationException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import burrito.client.widgets.messages.CommonMessages;

public abstract class ListInputField<T> extends Composite 
	implements HasChangeHandlers, HasValidators {

	private CommonMessages messages = GWT.create(CommonMessages.class);

	private VerticalPanel wrapper = new VerticalPanel();
	private List<InputFieldValidator> validators = new ArrayList<InputFieldValidator>();
	private TextBox field = new TextBox();
	private Button addButton;
	private Label validationError;
	private Label requiredStar;
	private FlowPanel inputWrapper;
	private FlowPanel listWrapper;
	private boolean required;
	private boolean unique;
	protected List<T> model;

	public ListInputField(boolean required, boolean unique) {
		this.required = required;
		this.unique = unique;
		inputWrapper = new FlowPanel();
		inputWrapper.add(field);
		addButton = new Button(messages.add());
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(validateInputField()) {
					model.add(parseValue(field.getText()));
					onChange(model);
					field.setText("");
					updateModelObjects();
				}
			}

		});
		if(required) {
			requiredStar = new Label("*");
			inputWrapper.add(requiredStar);
		}
		inputWrapper.add(addButton);
		validationError = new Label("");
		inputWrapper.add(validationError);
		wrapper.add(inputWrapper);
		
		listWrapper = new FlowPanel();
		listWrapper.addStyleName("k5-ListInputField-listwrapper");
		wrapper.add(listWrapper);
		initWidget(wrapper);
		wrapper.addStyleName("k5-ListInputField");
	}
	
	private void updateModelObjects() {
		listWrapper.clear();
		if (model == null) {
			return;
		}
		for(T modelObj : model) {
			listWrapper.add(createObjectWidget(modelObj));
		}
	}

	private Widget createObjectWidget(T modelObj) {
		final T value = modelObj;
		FlowPanel widget = new FlowPanel();
		Label itemLabel = new Label(getDisplayString(modelObj));
		itemLabel.addStyleName("k5-ListInputField-itemlabel");
		widget.add(itemLabel);
		PushButton removeButton = new PushButton(new Image(GWT.getModuleBaseURL() + "remove.png"), new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				remove(value);
			}
		});
		removeButton.addStyleName("k5-ListInputField-remove");
		widget.add(removeButton);
		widget.addStyleName("k5-ListInputField-listitem");
		return widget;
	}

	protected void remove(T value) {
		model.remove(value);
		updateModelObjects();
	}

	@Override
	public void addInputFieldValidator(InputFieldValidator validator) {
		validators.add(validator);
	}

	@Override
	public boolean validate() {
		if (required && (model == null || model.isEmpty())) {
			Window.alert("Minst ett v�rde m�ste l�ggas till");
			return false;
		}
		return true;
	}

	private boolean validateInputField() {
		setValidationError(null);
		if(!field.isEnabled()) {
			return true;
		}
		if(model == null) {
			return true;
		}
		String text = field.getText();
		if(!required && (text == null || text.isEmpty())) {
			return true;
		}
		if(unique && model.contains(parseValue(field.getText()))) {
			return false;
		}
		try {
			for(InputFieldValidator validator: validators) {
				validator.validate(text);
			}
			return true;
		} catch (ValidationException e){
			setValidationError(e.getValidationMessage());
			setFocus(true);
			selectAll();
		}
		return false;

	}

	@Override
	public void setValidationError(String validationMessage) {
		if(validationMessage == null) {
			validationError.setText(null);
		} else {
			validationError.setText(validationMessage);
		}
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return field.addChangeHandler(handler);
	}
	
	/**
	 * Sets focus to the field
	 * 
	 * @param focus
	 */
	public void setFocus(boolean focus) {
		field.setFocus(focus);
	}
	
	/**
	 * Selects all text contained in the text box
	 */
	public void selectAll() {
		field.selectAll();
	}
	
	public List<T> getValue() {
		return model;
	}
	
	public void setValue(List<T> model) {
		this.model = model;
		onChange(this.model);
		updateModelObjects();
	}
	
	protected void onChange(List<T> model2) {
		// do nothing
	}

	/**
	 * Parses the value of T from a String
	 * 
	 * @param text
	 * @return
	 */
	protected abstract T parseValue(String text);
	
	/**
	 * Gets a String representation of the value of T
	 * 
	 * @param object
	 * @return
	 */
	protected abstract String getDisplayString(T value);
}
