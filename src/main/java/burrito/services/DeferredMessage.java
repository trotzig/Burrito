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

package burrito.services;

import java.util.Date;

import siena.Generator;
import siena.Id;
import siena.Model;

public class DeferredMessage extends Model {
	
	@Id(Generator.AUTO_INCREMENT)
	private Long id;
	private String message;
	
	//unused, but kept for reference
	private Date created = new Date();
	
	public Long getId() {
		return id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public static DeferredMessage get(Long deferredMessageId) {
		return Model.all(DeferredMessage.class).filter("id", deferredMessageId).get();
	}
	
	public Date getCreated() {
		return created;
	}
	
}
