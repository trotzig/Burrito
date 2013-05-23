package burrito.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.ByteRange;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.appengine.api.blobstore.UploadOptions;

public class MockBlobstoreService implements BlobstoreService {

	@Override
	public String createUploadUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(BlobKey... arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] fetchData(BlobKey arg0, long arg1, long arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteRange getByteRange(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, BlobKey> getUploadedBlobs(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void serve(BlobKey arg0, HttpServletResponse arg1)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serve(BlobKey arg0, ByteRange arg1, HttpServletResponse arg2)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serve(BlobKey arg0, String arg1, HttpServletResponse arg2)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BlobKey createGsBlobKey(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createUploadUrl(String arg0, UploadOptions arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<BlobKey>> getUploads(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<BlobInfo>> getBlobInfos(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<FileInfo>> getFileInfos(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
