package burrito.client.crud.widgets;

import java.util.ArrayList;
import java.util.List;



import burrito.client.crud.CrudEntityEdit;
import burrito.client.crud.CrudService;
import burrito.client.crud.CrudServiceAsync;
import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.ImageField;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.crud.labels.CrudMessages;
import burrito.client.widgets.panels.table.BatchAction;
import burrito.client.widgets.panels.table.CellRenderer;
import burrito.client.widgets.panels.table.Header;
import burrito.client.widgets.panels.table.ItemCollection;
import burrito.client.widgets.panels.table.PageMetaData;
import burrito.client.widgets.panels.table.RowEditHandler;
import burrito.client.widgets.panels.table.RowOrderHandler;
import burrito.client.widgets.panels.table.Table;
import burrito.client.widgets.validation.HasValidators;
import burrito.client.widgets.validation.InputFieldValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import burrito.client.widgets.form.EditForm;

public class EmbeddedListWidget extends Composite implements HasValidators {

	private VerticalPanel wrapper = new VerticalPanel();
	private SimplePanel tablePlaceHolder = new SimplePanel();
	private Label validationError = new Label();
	private boolean required;
	private List<CrudEntityDescription> currentValue;
	private DialogBox editPopup = new DialogBox(false);
	private CrudServiceAsync service = GWT.create(CrudService.class);
	private String embeddedClassName;
	private CrudMessages labels = GWT.create(CrudMessages.class);
	private String embeddedClassNameUnderscore;

	public EmbeddedListWidget(String embeddedClassName, boolean required) {
		this.embeddedClassNameUnderscore = embeddedClassName.replace('.', '_');
		this.embeddedClassName = embeddedClassName;
		this.required = required;
		validationError.addStyleName("validationError");
		validationError.setVisible(false);
		wrapper.add(validationError);
		wrapper.add(tablePlaceHolder);
		tablePlaceHolder.setWidget(new Label(labels
				.noEmbeddedItemsAdded(CrudLabelHelper
						.getString(embeddedClassNameUnderscore + "_plural"))));
		Anchor add = new Anchor(CrudLabelHelper
				.getString(embeddedClassNameUnderscore + "_new"));
		add.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				showPopup();
			}
		});
		wrapper.add(add);
		initWidget(wrapper);
	}

	protected void showPopup() {
		service.describeEmbeddedObject(embeddedClassName,
				new AsyncCallback<CrudEntityDescription>() {

					public void onSuccess(CrudEntityDescription result) {
						final CrudEntityEdit panel = new CrudEntityEdit(result) {
							@Override
							public void doSave(SaveCallback saveCallback) {
								saveCallback.success();
							}
						};
						panel
								.setSaveCancelListener(new EditForm.SaveCancelListener() {

									public void onSave() {
										if (currentValue == null) {
											currentValue = new ArrayList<CrudEntityDescription>();
										}
										currentValue.add(panel.getValue());
										editPopup.hide();
										reloadTable();
									}

									public void onPartialSave(String warning) {
										throw new UnsupportedOperationException();
									}

									public void onCancel() {
										editPopup.hide();
									}
								});
						editPopup.setWidget(panel);
						editPopup
								.setText(CrudLabelHelper
										.getString(embeddedClassNameUnderscore
												+ "_new"));
						editPopup.center();
						editPopup.show();
					}

					public void onFailure(Throwable caught) {
						throw new RuntimeException(
								"Failed to describe embedded entity", caught);
					}
				});
	}

	public void addInputFieldValidator(InputFieldValidator validator) {
		throw new UnsupportedOperationException(
				"This class does not support addInputFieldValidator");
	}

	public void setValidationError(String validationText) {
		validationError.setVisible(false);
		validationError.setText(null);
		if (validationText != null) {
			validationError.setText(validationText);
			validationError.setVisible(true);
		}
	}

	public boolean validate() {
		setValidationError(null);
		List<CrudEntityDescription> value = getValue();
		if (!required) {
			return true;
		}
		if (value == null || value.isEmpty()) {
			setValidationError(labels.embeddedItemAtLeastOne(CrudLabelHelper
					.getString(embeddedClassNameUnderscore + "_singular")));
			return false;
		}
		return true;
	}

	public List<CrudEntityDescription> getValue() {
		return currentValue;
	}

	public void setValue(List<CrudEntityDescription> value) {
		this.currentValue = value;
		reloadTable();
	}

	private void reloadTable() {
		// Check if value is empty
		if (currentValue == null || currentValue.isEmpty()) {
			tablePlaceHolder.setWidget(new Label(
					labels
							.noEmbeddedItemsAdded(CrudLabelHelper
									.getString(embeddedClassNameUnderscore
											+ "_plural"))));
			return;
		}

		tablePlaceHolder.setWidget(new Label(labels.loading()));

		// Fetch the first row to figure out how to create the table
		CrudEntityDescription nr1 = currentValue.get(0);

		Table<CrudEntityDescription> table = new Table<CrudEntityDescription>(
				nr1.getFields().size(), true, true, true) {

			@Override
			protected void doLoad(PageMetaData<String> p,
					final Table.LoadedCallback<CrudEntityDescription> callback) {
				// nevermind the page info, display all values
				ItemCollection<CrudEntityDescription> coll = new ItemCollection<CrudEntityDescription>(
						currentValue, false, p.getPage(), p.getItemsPerPage());
				callback.onReady(coll);
			}
		};
		for (CrudField field : nr1.getFields()) {
			String header = CrudLabelHelper.getString(nr1.getEntityName()
					.replace('.', '_')
					+ "_" + field.getName(), field.getName());
			table.addHeader(new Header(header));
		}
		for (int i = 0; i < nr1.getFields().size(); i++) {
			final int indexInside = i;
			table.addCellRenderer(new CellRenderer<CrudEntityDescription>() {

				public Widget render(CrudEntityDescription modelObj) {
					CrudField cf = modelObj.getFields().get(indexInside);
					if (cf.getValue() == null) {
						return new Label();
					}
					if (cf instanceof ImageField) {
						ImageField imgField = (ImageField) cf;
						Image image = new Image(
								imgField.getValue().toString().replace(
										"/images/view/", "/images/view/thumb_"));
						image.setWidth("100px");
						return image;
					}
					return new Label(String.valueOf(cf.getValue()));
				}
			});
		}
		table.addBatchAction(new BatchAction<CrudEntityDescription>(labels
				.delete(), labels.deleteEmbeddedItem()) {

			public void performAction(List<CrudEntityDescription> selected,
					AsyncCallback<Void> callback) {
				currentValue.removeAll(selected);
				reloadTable();
			}

			public String getSuccessText(List<CrudEntityDescription> removed) {
				return labels.embeddedItemsDeleted(CrudLabelHelper
						.getString(embeddedClassNameUnderscore + "_plural"),
						removed.size());
			}
		});
		table.setRowOrderHandler(new RowOrderHandler<CrudEntityDescription>() {

			public void onRowsReordered(List<CrudEntityDescription> newOrder) {
				currentValue = newOrder;
			}
		});
		table.setRowEditHandler(new RowEditHandler<CrudEntityDescription>() {

			public void onRowEditClicked(CrudEntityDescription obj) {
				final CrudEntityEdit panel = new CrudEntityEdit(obj) {
					@Override
					public void doSave(SaveCallback saveCallback) {
						saveCallback.success();
					}
				};
				panel.setSaveCancelListener(new EditForm.SaveCancelListener() {

					public void onSave() {
						// values are saved in the original description
						panel.getValue();
						editPopup.hide();
						reloadTable();
					}

					public void onPartialSave(String warning) {
						throw new UnsupportedOperationException();
					}

					public void onCancel() {
						editPopup.hide();
					}
				});
				editPopup.setWidget(panel);
				editPopup.setText(CrudLabelHelper
						.getString(embeddedClassNameUnderscore + "_editing"));
				editPopup.center();
				editPopup.show();
			}
		});
		table.render();
		tablePlaceHolder.setWidget(table);

	}

}
