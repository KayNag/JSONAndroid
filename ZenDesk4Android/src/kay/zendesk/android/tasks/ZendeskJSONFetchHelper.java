package kay.zendesk.android.tasks;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import android.util.Base64;
import android.util.Log;

/**
 * Helperclass to interact with Zendesk API.
 * 
 * @author Kay Nag
 * 
 */
public class ZendeskJSONFetchHelper {

	private static final String Zendesk = "https://mxssl.zendesk.com/api/v2/views/1076747/tickets.json";
	private static final int HTTP_STATUS_OK = 200;
	private static byte[] buff = new byte[1024];
	private static final String logTag = "ZendeskJSONFetchHelper";

	public static class ApiException extends Exception {
		private static final long serialVersionUID = 1L;

		public ApiException(String msg) {
			super(msg);
		}

		public ApiException(String msg, Throwable thr) {
			super(msg, thr);
		}
	}

	/**
	 * download tickets.
	 * 
	 * @return Array of json strings returned by the API.
	 * @throws ApiException
	 */
	protected static synchronized String downloadFromServer(String... params)
			throws ApiException {
		String retval = null;

		String url = Zendesk;

		Log.d(logTag, "Fetching " + url);

		// create an http client and a request object.
		HttpGet request = new HttpGet(url);
		String credentials = "acooke+techtest@zendesk.com" + ":" + "mobile";
		String base64EncodedCredentials = Base64.encodeToString(
				credentials.getBytes(), Base64.NO_WRAP);
		request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
		HttpClient client = getNewHttpClient();

		try {

			// execute the request
			HttpResponse response = client.execute(request);

			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HTTP_STATUS_OK) {
				// handle error here
				throw new ApiException("Invalid response from zendesk"
						+ status.toString());
			}

			// process the content.
			HttpEntity entity = response.getEntity();
			InputStream ist = entity.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();

			int readCount = 0;
			while ((readCount = ist.read(buff)) != -1) {
				content.write(buff, 0, readCount);
			}
			retval = new String(content.toByteArray());

			// appendLog(retval);

		} catch (Exception e) {
			throw new ApiException("Problem connecting to the server "
					+ e.getMessage(), e);
		}

		return retval;
	}

	public static HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocket(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

}
