package burrito.client.util;

public class CrudPreviewOpener {

	private final String url;
	private final String data;

	public CrudPreviewOpener(String url, String data) {
		this.url = url;
		this.data = data;
	}

	public native void open() /*-{
		var url = this.@burrito.client.util.CrudPreviewOpener::url;
		var data = this.@burrito.client.util.CrudPreviewOpener::data;

		var popup = $wnd.open('', '_blank');

		var form = popup.document.createElement('form');
		form.setAttribute('method', 'post');
		form.setAttribute('action', url);

		var input = popup.document.createElement('input');
		input.type = 'hidden';
		input.name = 'data';
		input.value = data;

		form.appendChild(input);
		popup.document.body.appendChild(form);

		form.submit();
	}-*/;
}
