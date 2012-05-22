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

package burrito.client.crud.widgets;

import java.util.ArrayList;
import java.util.List;

import burrito.client.crud.CrudService;
import burrito.client.crud.CrudServiceAsync;
import burrito.client.crud.labels.CrudLabelHelper;
import burrito.client.crud.labels.CrudMessages;
import burrito.client.dto.LinkJavaScriptObject;
import burrito.client.util.LinkJavaScriptObjectFactory;
import burrito.client.widgets.inputfield.StringInputField;
import burrito.client.widgets.inputfield.URLInputfield;
import burrito.client.widgets.layout.VerticalSpacer;
import burrito.client.widgets.selection.SelectionList;
import burrito.client.widgets.selection.SelectionListLabelCreator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LinkedEntityWidgetPopup extends DialogBox {
	public static interface SaveHandler {
		void saved(String json);
	}

	public static final String TYPE_ABSOLUTE_URL = "link_absolute";

	private VerticalPanel wrapper = new VerticalPanel();
	private CrudServiceAsync service = GWT.create(CrudService.class);
	private SelectionList<String> type = new SelectionList<String>(false);
	private SimplePanel valuesWrapper = new SimplePanel();
	private RelationSelectionList relationSelectionList;
	private StringInputField linkText = new StringInputField(true);
	private URLInputfield url = new URLInputfield(true);

	private CrudMessages labels = GWT.create(CrudMessages.class);

	private Long typeIdWaitingToBeSet;

	private VerticalPanel urlWrapper;

	public LinkedEntityWidgetPopup(final SaveHandler saveHandler) {
		super(false, true);
		setText(labels.createNewLink());
		type.setLabelCreator(new SelectionListLabelCreator<String>() {

			@Override
			public String createLabel(String typeClassName) {
				return CrudLabelHelper.getString(typeClassName
						.replace('.', '_') + "_singular");
			}
		});
		type.setNullSelectLabel(labels.chooseLinkType());
		type.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				handleTypeChange();
			}
		});
		wrapper.add(new Label(labels.linkText()));
		wrapper.add(linkText);
		wrapper.add(new Label(labels.selectLinkTo()));
		wrapper.add(type);
		wrapper.add(valuesWrapper);

		urlWrapper = new VerticalPanel();
		urlWrapper.setVisible(false);
		urlWrapper.add(new Label(labels.writeOrPasteLink()));
		urlWrapper.add(url);
		wrapper.add(urlWrapper);

		HorizontalPanel hp = new HorizontalPanel();

		hp.add(new Button(labels.save(), new ClickHandler() {

			public void onClick(ClickEvent event) {
				doSave(saveHandler);
			}
		}));
		hp.add(new Button(labels.cancel(), new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeFromParent();
			}
		}));
		wrapper.add(new VerticalSpacer(10));
		wrapper.add(hp);
		setWidget(wrapper);
		addStyleName("k5-LinkedEntityWidget");

		init();
	}

	protected void doSave(SaveHandler saveHandler) {
		if (urlWrapper.isVisible() && linkText.validate() && url.validate()) {
			saved(saveHandler);
		} else if (linkText.validate() && type.validate()) {
			saved(saveHandler);
		}
	}

	private void saved(SaveHandler saveHandler) {
		saveHandler.saved(toJson());
		removeFromParent();
	}

	protected void handleTypeChange() {
		valuesWrapper.clear();
		urlWrapper.setVisible(false);
		relationSelectionList = null;
		if (type.getValue() == null) {
			return;
		}
		String typeClassName = type.getValue();
		if (TYPE_ABSOLUTE_URL.equals(typeClassName)) {
			urlWrapper.setVisible(true);
			return;
		}
		relationSelectionList = new RelationSelectionList(false, typeClassName);
		relationSelectionList.setValue(typeIdWaitingToBeSet);
		typeIdWaitingToBeSet = null;
		VerticalPanel vp = new VerticalPanel();
		vp.add(new Label(labels.selectLinkToEntity(CrudLabelHelper.getString(typeClassName.replace('.', '_') + "_singular"))));
		vp.add(relationSelectionList);
		valuesWrapper.setWidget(vp);

	}

	private void init() {
		service.getLinkableTypes(new AsyncCallback<List<String>>() {

			public void onSuccess(List<String> result) {
				loadTypes(result);
			}

			public void onFailure(Throwable caught) {
				throw new RuntimeException("Failed to get linkable types",
						caught);
			}
		});
	}

	private void loadTypes(List<String> result) {
		List<String> extendedModel = new ArrayList<String>();
		extendedModel.add(TYPE_ABSOLUTE_URL);
		extendedModel.addAll(result);
		type.setModel(extendedModel);
		type.render();
	}

	public String toJson() {
		String typeValue = type.getValue();

		if (typeValue == null) {
			return null;
		}

		String linkText = this.linkText.getValue();

		if (linkText == null || linkText.isEmpty()) {
			return null;
		}

		LinkJavaScriptObject link = LinkJavaScriptObjectFactory.fromJson("{\"typeClassName\": \"\", \"typeId\": -1, \"absoluteUrl\": null, \"linkText\": null}");
		link.setLinkText(linkText);

		if (relationSelectionList != null) {
			link.setTypeClassName(typeValue);
			link.setTypeId(relationSelectionList.getValue());
		}
		else {
			String absoluteUrl = url.getValue();

			if (absoluteUrl == null) {
				return null;
			}

			link.setTypeClassName(TYPE_ABSOLUTE_URL);
			link.setAbsoluteUrl(absoluteUrl);
		}

		return new JSONObject(link).toString();
	}

	private final native LinkedEntityJsonOverlay asLinkedEntity(String json) /*-{
																				return eval("json=" + json);
																				}-*/;

	public void fromJson(String json) {
		if (json == null || json.isEmpty()) {
			type.setValue(null);
			return;
		}
		try {
			LinkedEntityJsonOverlay linked = asLinkedEntity(json);
			if (linked.getTypeId() > 0) {
				typeIdWaitingToBeSet = Long.valueOf(linked.getTypeId());
			}
			linkText.setValue(linked.getLinkText());
			type.setValue(linked.getTypeClassName());
			url.setValue(linked.getAbsoluteLink());
		} catch (Exception e) {
			GWT.log("Failed to parse json: " + json, e);
		}
		handleTypeChange();
	}

	public void setLinkText(String text) {
		linkText.setValue(text);
	}
}
