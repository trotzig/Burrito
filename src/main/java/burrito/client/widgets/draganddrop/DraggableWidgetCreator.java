package burrito.client.widgets.draganddrop;

import com.google.gwt.user.client.ui.Widget;

public interface DraggableWidgetCreator<T> {

	Widget createWidget(T modelObj);
	
	
}
