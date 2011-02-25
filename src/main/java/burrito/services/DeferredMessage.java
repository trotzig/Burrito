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
