package kay.zendesk.android.tasks;

import java.util.ArrayList;

import kay.zendesk.android.R;
import kay.zendesk.android.ZendeskFace;
import kay.zendesk.android.data.ZendeskData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * AsyncTask for fetching tickets from Zendesk (very important part of app,do
 * changes carefully )
 * 
 * @author Kay Nag
 */
public class ZendeskAPITask extends AsyncTask<String, Integer, String> {
	private ProgressDialog progDialog;
	private Context context;
	private ZendeskFace activity;
	private static final String debugTag = "ZendeskAPITask";

	/**
	 * Construct a task
	 * 
	 * @param activity
	 */
	public ZendeskAPITask(ZendeskFace activity) {
		super();
		this.activity = activity;
		this.context = this.activity.getApplicationContext();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progDialog = ProgressDialog.show(this.activity, "Search", this.context
				.getResources().getString(R.string.looking_for_tickets), true,
				false);
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			Log.d(debugTag, "Background:" + Thread.currentThread().getName());
			String result = ZendeskJSONFetchHelper.downloadFromServer(params);
			return result;
		} catch (Exception e) {
			return new String();
		}
	}

	@Override
	protected void onPostExecute(String result) {

		ArrayList<ZendeskData> ticketsdata = new ArrayList<ZendeskData>();

		progDialog.dismiss();
		if (result.length() == 0) {
			this.activity.alert("Unable to find tickets. Try again later.");
			return;
		}

		try {
			JSONObject respObj = new JSONObject(result);
			// JSONObject ticketobj = respObj.getJSONObject("ticket");
			JSONArray tickets = respObj.getJSONArray("tickets");
			for (int i = 0; i < tickets.length(); i++) {
				JSONObject ticketsdetail = tickets.getJSONObject(i);
				String subject = ticketsdetail.getString("subject");
				String ticketdescription = ticketsdetail
						.getString("description");
				String ticketno = ticketsdetail.getString("id");
				String ticketstatus = ticketsdetail.getString("status");
				String imageUrl;
				try {
					JSONArray imageUrls = ticketsdetail.getJSONArray("image");
					imageUrl = null;
					for (int j = 0; j < imageUrls.length(); j++) {
						JSONObject imageObj = imageUrls.getJSONObject(j);
						imageUrl = imageObj.getString("#text");
						if (imageObj.getString("size").equals("medium")) {
							break;
						}
					}
				} catch (Exception e) {
					imageUrl = null;
				}

				ticketsdata.add(new ZendeskData(subject, ticketno, imageUrl,
						ticketstatus, ticketdescription));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.activity.settickets(ticketsdata);

	}
}