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

package burrito.links;

import java.util.ArrayList;
import java.util.List;

import siena.Model;
import burrito.client.crud.widgets.LinkedEntityWidgetPopup;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Parser that knows how to deal with json {@link Link} objects
 * 
 * @author henper
 * 
 */
public class JsonLinkParser {

	/**
	 * This method will attempt to parse the input string as JSON, and
	 * return a {@link Link} object. If the referenced entity can not be
	 * found, the return value is null.
	 * 
	 * If the string is not a valid JSON representation, the returned
	 * {@link Link} object will instead contain the input string in it's
	 * url field.
	 * 
	 * If the input value is null, the method returns null.
	 * 
	 * In other words, the input should be either a valid link JSON
	 * string, or a URL, or null.
	 * 
	 * @param value
	 * @return
	 */
	public Link parse(String value) {
		if (value == null) {
			return null;
		}

		Link link = new Link();
		String url = null;

		try {
			Gson gson = new Gson();
			LinkJsonOverlay overlay = gson.fromJson(value, LinkJsonOverlay.class);

			if (overlay.absoluteUrl != null) {
				url = overlay.absoluteUrl;
			}
			else {
				try {
					Class<?> clazz = Class.forName(overlay.typeClassName);
					Linkable linkable = (Linkable) Model.all(clazz).filter("id", overlay.typeId).get();

					if (linkable != null) {
						url = linkable.getUrl();
					}
				}
				catch (ClassNotFoundException e) {
					// url remains null
				}
			}

			link.setTypeName(overlay.typeClassName);
			link.setId(overlay.typeId);
			link.setText(overlay.linkText);
		}
		catch (JsonParseException e) {
			url = value;

			link.setTypeName(LinkedEntityWidgetPopup.TYPE_ABSOLUTE_URL);
			link.setId(-1L);
			link.setText(value);
		}

		if (url == null) {
			return null;
		}

		link.setUrl(url);

		return link;
	}

	/**
	 * Same as parse(String). Exists for historical reasons.
	 * 
	 * @param value
	 * @return
	 */
	public Link parseFailSafe(String value) {
		return parse(value);
	}

	/**
	 * Creates a list of {@link Link}s from a list of strings.
	 * 
	 * Uses parse() internally for each input string. If parse() returns
	 * null for an input string, that {@link Link} is then not included
	 * in the output list. Thus, the length of the output list
	 * can be smaller than the length of the input list.
	 * 
	 * @param values
	 * @return
	 */
	public List<Link> parse(List<String> values) {
		List<Link> result = new ArrayList<Link>(values.size());
		for (String value : values) {
			Link link = parseFailSafe(value);
			if (link != null) {
				result.add(link);
			}
		}
		return result;
	}

	/**
	 * Same a parse(List<String>). Exists for historical reasons.
	 * 
	 * @param values
	 * @return
	 */
	public List<Link> parseFailSafe(List<String> values) {
		return parse(values);
	}

}
