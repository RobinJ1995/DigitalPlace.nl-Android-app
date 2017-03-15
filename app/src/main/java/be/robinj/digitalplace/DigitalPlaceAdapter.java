package be.robinj.digitalplace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by robin on 8/15/14.
 */
public class DigitalPlaceAdapter extends ArrayAdapter<DigitalPlacePost>
{
	public DigitalPlaceAdapter (Context context, List<DigitalPlacePost> posts)
	{
		super (context, R.layout.dplistview, posts);
	}

	@Override
	public View getView (int position, View view, ViewGroup parent)
	{
		DigitalPlacePost post = getItem (position);

		if (view == null)
			view = LayoutInflater.from (getContext ()).inflate (R.layout.dplistview, parent, false);

		TextView tvTitle = (TextView) view.findViewById (R.id.tvTitle);
		TextView tvForum = (TextView) view.findViewById (R.id.tvForum);
		TextView tvUser = (TextView) view.findViewById (R.id.tvUser);

		tvTitle.setText (post.getTitle ());
		tvForum.setText (post.getForum ());
		tvUser.setText (post.getUser ());

		view.setTag (post.getUrl ());

		return view;
	}
}
