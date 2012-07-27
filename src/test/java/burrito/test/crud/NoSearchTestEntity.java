package burrito.test.crud;

/**
 * This class is used to test if an entity can avoid to end up in the search index.
 * 
 * @author henper
 *
 */
public class NoSearchTestEntity extends SearchTestEntity {

	@Override
	protected boolean updateSearchIndexOnSave() {
		return false;
	}

}
