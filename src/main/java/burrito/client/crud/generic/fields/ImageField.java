package burrito.client.crud.generic.fields;

import burrito.client.crud.generic.CrudField;

/**
 * A crud field representing an image. The way this {@link CrudField} is forced
 * is by using the following annotation on a {@link String} field in the entity:
 * <code>
 * <pre>@Image(width = 640, height = 360) public String image; </pre>
 * </code>
 * 
 * @author henper
 * 
 */
@SuppressWarnings("serial")
public class ImageField extends StringField {

	int width;
	int height;

	public ImageField(String string, int width, int height) {
		super(string);
		this.width = width;
		this.height = height;
	}

	public ImageField() {
		// default constructor
	}

	/**
	 * Gets the allowed width of the image
	 * 
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the allowed height of the image
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
