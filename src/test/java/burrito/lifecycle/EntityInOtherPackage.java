package burrito.lifecycle;

import java.util.Date;

import siena.core.lifecycle.PreInsert;
import siena.core.lifecycle.PreSave;
import siena.core.lifecycle.PreUpdate;
import burrito.BurritoModel;

public class EntityInOtherPackage extends BurritoModel {

	private Date lastModified;
	
	
	public Date getLastModified() {
		return lastModified;
	}
	
	
	@PreSave
	@PreInsert
	@PreUpdate
	public void updateLastModifiedDate() {
		lastModified = new Date();
	}
	
}
