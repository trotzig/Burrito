package burrito.test;

import java.util.Collection;
import java.util.concurrent.Future;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.Composite;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.OutputSettings;
import com.google.appengine.api.images.Transform;

public class MockImagesService implements ImagesService {

	@Override
	public Image applyTransform(Transform arg0, Image arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image applyTransform(Transform arg0, Image arg1, OutputEncoding arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image applyTransform(Transform arg0, Image arg1, OutputSettings arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Image> applyTransformAsync(Transform arg0, Image arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Image> applyTransformAsync(Transform arg0, Image arg1,
			OutputEncoding arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Image> applyTransformAsync(Transform arg0, Image arg1,
			OutputSettings arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image composite(Collection<Composite> arg0, int arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image composite(Collection<Composite> arg0, int arg1, int arg2,
			long arg3, OutputEncoding arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image composite(Collection<Composite> arg0, int arg1, int arg2,
			long arg3, OutputSettings arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServingUrl(BlobKey arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServingUrl(BlobKey arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[][] histogram(Image arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
