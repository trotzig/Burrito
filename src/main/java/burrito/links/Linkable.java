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

/**
 * Interface used on entities that has a unique url to a section of the page
 * where it is hosted
 * 
 * @author henper
 * 
 */
public interface Linkable {

	/**
	 * Gets the unique URL to where this entity is located on the site.
	 * 
	 * @return
	 */
	String getUrl();

}
