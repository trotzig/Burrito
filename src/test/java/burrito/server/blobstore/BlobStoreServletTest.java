package burrito.server.blobstore;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.junit.Test;

import burrito.test.MockBlobstoreService;
import burrito.test.MockControllerRequest;
import burrito.test.MockControllerResponse;
import burrito.test.MockImagesService;
import burrito.test.TestBase;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.images.ImagesService;

public class BlobStoreServletTest extends TestBase {

	//param data
	private String blobKeyId = "minblob-key";
	private String sizeParam;
	private String queryString;
	private String imageServingUrl;
	private BlobKey blobKey;
	
	//result data
	private String locationResult;
	private BlobKey servedBlobKeyResult; 
	
	//services
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private ImagesService imagesService;
	private BlobstoreService blobstoreService;
	
	@Override
	public void setUp() {
		super.setUp();

		blobKey = new BlobKey(blobKeyId);
		
		req = new MockControllerRequest("uri", null) {
			@Override
			public String getParameter(String name) {
				if ("key".equals(name)) {
					return blobKeyId;
					
				} else if ("s".equals(name)) {
					return sizeParam;
				}
				Assert.assertFalse("unknown key: " + name, true);
				return "";
			}
			
			@Override
			public String getQueryString() {
				return queryString;
			}
		};
		
		resp = new MockControllerResponse(){
			@Override
			public void sendRedirect(String location2) throws IOException {
				locationResult = location2;
			}
		};
		
		imagesService = new MockImagesService() {
			@Override
			public String getServingUrl(BlobKey arg0) {
				Assert.assertEquals(blobKey, arg0);
				return imageServingUrl;
			}
		};	
		
		blobstoreService = new MockBlobstoreService() {
			@Override
			public void serve(BlobKey blob, HttpServletResponse arg1)
					throws IOException {
				servedBlobKeyResult = blob;
			}
		};
	}
	
	@Test
	public void testServeImageSizeParam() throws Exception {
		sizeParam = "200";
		imageServingUrl = "http://0.0.0.0:8089/_ah/img/key=SHpWfKkdfe3-dqlZRDmNaA";
		
		BlobStoreServlet servlet = new BlobStoreServlet(imagesService, blobstoreService);
		servlet.doGet(req, resp);
		
		Assert.assertEquals("http://localhost:8089/_ah/img/key=SHpWfKkdfe3-dqlZRDmNaA=s200", locationResult);
	}
	
	@Test
	public void testServeImageWithNoSize() throws Exception {
		sizeParam = null;
		queryString = "";
		imageServingUrl = "http://0.0.0.0:8089/_ah/img/key=SHpWfKkdfe3-dqlZRDmNaA";
		
		BlobStoreServlet servlet = new BlobStoreServlet(imagesService, blobstoreService);
		servlet.doGet(req, resp);
		
		Assert.assertEquals(blobKey, servedBlobKeyResult);
	}
	
	@Test
	public void testServeImageWithStrangeSizeParam() throws Exception {
		
		sizeParam = null;
		queryString = "key=SHpWfKkdfe3-dqlZRDmNaA=s200";
		imageServingUrl = "http://0.0.0.0:8089/_ah/img/key=SHpWfKkdfe3-dqlZRDmNaA";
		
		BlobStoreServlet servlet = new BlobStoreServlet(imagesService, blobstoreService);
		servlet.doGet(req, resp);
		
		Assert.assertEquals("http://localhost:8089/_ah/img/key=SHpWfKkdfe3-dqlZRDmNaA=s200", locationResult);
	}
}
