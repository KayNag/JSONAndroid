package kay.zendesk.android;

import java.util.ArrayList;

import kay.zendesk.android.adapters.ZendeskDataAdapter;
import kay.zendesk.android.data.ZendeskData;
import kay.zendesk.android.tasks.ZendeskAPITask;
import kay.zendesk.android.tasks.ZendeskIconTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fetch & display tickets from the zendesk
 * 
 * @author Kay Nag
 * 
 */
public class ZendeskFace extends Activity {

	private ArrayList<ZendeskData> ticketsdata;
	private ListView ticket_list;
	private LayoutInflater layoutInflator;

	private ZendeskIconTask imgFetcher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		this.ticket_list = (ListView) findViewById(R.id.ticket_list);
		this.imgFetcher = new ZendeskIconTask(this);
		this.layoutInflator = LayoutInflater.from(this);

		ZendeskAPITask lfmTask = new ZendeskAPITask(ZendeskFace.this);
		try {

			lfmTask.execute();
		} catch (Exception e) {
			lfmTask.cancel(true);
			alert(getResources().getString(R.string.no_ticket));
		}

		// Restore any already fetched data on orientation change.

	}

	/**
	 * alerter.
	 * 
	 * @param msg
	 *            - the message to toast.
	 */

	public void alert(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * Bundle to hold refs to row items views.
	 * 
	 */

	public static class MyViewHolder {
		public TextView subject, ticket_no, ticket_status, ticket_description;
		public ImageView icon;
		public ZendeskData ticketsdata;
	}

	public void settickets(ArrayList<ZendeskData> ticketsdata) {
		this.ticketsdata = ticketsdata;
		this.ticket_list.setAdapter(new ZendeskDataAdapter(this,
				this.imgFetcher, this.layoutInflator, this.ticketsdata));
	}

}