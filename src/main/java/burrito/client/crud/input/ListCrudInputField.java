package burrito.client.crud.input;

import java.util.List;



import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.ManyToManyRelationField;
import burrito.client.crud.widgets.MultiValueListBox;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.kanal5.play.client.widgets.form.EditForm;

@SuppressWarnings("rawtypes")
public class ListCrudInputField implements CrudInputField {
	
	
	private class MultiValueListBoxWrapper extends Composite implements HasChangeHandlers {
		private VerticalPanel wrapper = new VerticalPanel();
		
		public MultiValueListBoxWrapper() {
			wrapper.add(listBox);
			wrapper.add(new RelatedEntityEditAnchor(field.getRelatedEntityName(), new EditForm.SaveCancelListener() {
				
				public void onSave() {
					listBox.load();
				}
				
				public void onPartialSave(String warning) {
					//do nothing
				}
				
				public void onCancel() {
					//do nothing
				}
			}));
			initWidget(wrapper);
		}

		public HandlerRegistration addChangeHandler(ChangeHandler handler) {
			return listBox.addChangeHandler(handler);
		}
	}
	private ManyToManyRelationField field;
	private MultiValueListBox listBox;
	private MultiValueListBoxWrapper wrapper;
	
	@SuppressWarnings("unchecked")
	public ListCrudInputField(ManyToManyRelationField field) {
		this.field = field;
		this.listBox = new MultiValueListBox(field.getRelatedEntityName());
		listBox.setSelectedValues((List<Long>) field.getValue());
		wrapper = new MultiValueListBoxWrapper();
	}

	public CrudField getCrudField() {
		field.setValue(listBox.getSelectedValues());
		return field;
	}

	public Widget getDisplayWidget() {
		return wrapper;
	}

	public Object getValue() {
		return listBox.getSelectedValues();
	}

	@SuppressWarnings("unchecked")
	public void load(Object value) {
		listBox.setSelectedValues((List<Long>) value);
	}

}
