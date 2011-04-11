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

package burrito.client.widgets;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Extended textarea that grows based on the number of line breaks in the text.
 * Similar to what Facebook is using.
 * 
 * @author henper
 * 
 */
public class ExpandingTextArea extends TextArea {

	private int minimumHeight = 70;  
	private int lineHeight = 14;
	
	public ExpandingTextArea() {
		addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					updateHeight(true);
				}
			}
		});
		addKeyUpHandler(new KeyUpHandler() {
		
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE
						|| event.getNativeKeyCode() == KeyCodes.KEY_DELETE) {
					updateHeight(false);
				}
			}
		});
	}

	protected void updateHeight(boolean newLineEntered) {
		int height = lineHeight; //start with one line of text
		if (newLineEntered) {
			height += lineHeight;
		}
		String text = getText();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '\n') {
				height += lineHeight;
			}
		}
		if (height < minimumHeight) {
			height = minimumHeight;
		}
		setHeight(height + "px");
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		updateHeight(false);
	}

}
