package burrito.client.widgets.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import burrito.client.widgets.selection.SelectionList;
import burrito.client.widgets.selection.SelectionListLabelCreator;

public class IntegerIntervalSelectionList extends SelectionList<Integer> {

	private int min;
	private int max;
	private boolean reverse;
	
	public IntegerIntervalSelectionList(boolean required){
		super(required);
	}
	
	public IntegerIntervalSelectionList(int min, int max, boolean required) {
		super(required);
		this.min = min;
		this.max = max;
		render();
	}
	
	public IntegerIntervalSelectionList(int min, int max, String nullSelectLabel, boolean required){
		super(required);
		this.min = min;
		this.max = max;
		setNullSelectLabel(nullSelectLabel);
		render();
	}
	
	public void render() {
		List<Integer> integers = new ArrayList<Integer>();
		for (int i = min; i <= max; i++) {
			integers.add(Integer.valueOf(i));
		}
		if(reverse){
			Collections.reverse(integers);
		}
		setModel(integers);
		setLabelCreator(new SelectionListLabelCreator<Integer>() {
			
			@Override
			public String createLabel(Integer obj) {
				return String.valueOf(obj);
			}
		});
		super.render();
	}
	

	public void setMin(int min){
		this.min = min;
	}
	
	public void setMax(int max){
		this.max = max;
	}
	
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
	
}
