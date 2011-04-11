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

package burrito.sitelet;

/**
 * Common interface used for all sitelets
 * 
 * @author henper
 * 
 */
public interface Sitelet {

	/**
	 * Gets a date when this sitelet should be updated next time. Return
	 * <code>null</code> if the sitelet does not need to be auto refreshed.
	 * (Useful in cases when the only action that can change the ouput is from
	 * changing the sitelets settings)
	 * 
	 * @return
	 */
	public AutoRefresh getNextAutoRefresh();

	/**
	 * Gets a string describing the content of the sitelet instance. Tryto keep
	 * this value short. ~100 characters should be enough.
	 * <br><br>
	 * Example (for an <i>RssFeedSitelet</i>): <br>
	 * <code>"RSS feed from " + rssFeedUrl</code><br>
	 * <br>
	 * Another example (for a <i>UserProfileSitelet</i>):<br>
	 * <code>return "User profile for " + userName;</code><br> 
	 * 
	 * @return
	 */
	public String describe();
}
