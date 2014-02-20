package kay.zendesk.android.adapters;

import java.util.ArrayList;

import kay.zendesk.android.R;
import kay.zendesk.android.ZendeskFace;
import kay.zendesk.android.ZendeskFace.MyViewHolder;
import kay.zendesk.android.data.ZendeskData;
import kay.zendesk.android.tasks.ZendeskIconTask;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter for tickets to fill in the rows in list view
 * 
 * @author Kay Nag
 * 
 */
public class ZendeskDataAdapter extends BaseAdapter implements OnClickListener {

	private static final String debugTag = "ZendeskDataAdapter";
	private ZendeskFace activity;
	private ZendeskIconTask imgFetcher;
	private LayoutInflater layoutInflater;
	private ArrayList<ZendeskData> ticketsdata;

	public ZendeskDataAdapter(ZendeskFace a, ZendeskIconTask i,
			LayoutInflater l, ArrayList<ZendeskData> data) {
		this.activity = a;
		this.imgFetcher = i;
		this.layoutInflater = l;
		this.ticketsdata = data;
		int aas = data.size();
	}

	@Override
	public int getCount() {
		return this.ticketsdata.size();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		MyViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.ticketdetail, parent,
					false);
			holder = new MyViewHolder();
			holder.subject = (TextView) convertView.findViewById(R.id.subject);
			holder.ticket_no = (TextView) convertView
					.findViewById(R.id.ticket_no);
			holder.ticket_status = (TextView) convertView
					.findViewById(R.id.ticket_status);
			holder.ticket_description = (TextView) convertView
					.findViewById(R.id.ticket_description);
			holder.icon = (ImageView) convertView.findViewById(R.id.album_icon);

			convertView.setTag(holder);
		} else {
			holder = (MyViewHolder) convertView.getTag();
		}

		convertView.setOnClickListener(this);

		ZendeskData position = ticketsdata.get(pos);
		holder.ticketsdata = position;
		if (position.getSubject() != null) {
			holder.subject.setText("Subject : " + position.getSubject());

		} else {
			holder.subject.setText("Subject : No Subject");
		}

		holder.ticket_no.setText("Ticket Number : " + position.getTicketno());
		holder.ticket_status.setText("Ticket Status : "
				+ position.getTicketstatus());
		holder.ticket_description.setText("Description : "
				+ position.getDescription());

		if (position.getImageUrl() != null) {
			holder.icon.setTag(position.getImageUrl());
			Drawable dr = imgFetcher.loadImage(this, holder.icon);
			if (dr != null) {
				holder.icon.setImageDrawable(dr);
			}
		} else {
			holder.icon.setImageResource(R.drawable.filler_icon);
		}

		return convertView;
	}

	@Override
	public void onClick(View v) {

		Log.d(debugTag, "OnClick pressed.");

	}

}