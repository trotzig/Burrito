package burrito.test.crud;

import java.util.List;

import burrito.BurritoModel;
import burrito.annotations.IndexedByEnum;

public class IndexedByEnumEntity extends BurritoModel {

	public enum TEST {
		TEST1,
		TEST2
	}
	
	@IndexedByEnum(type = TEST.class)
	private List<String> indexString;

	
	
	public void setIndexString(List<String> indexString) {
		this.indexString = indexString;
	}
	public List<String> getIndexString() {
		return indexString;
	}
	
}
