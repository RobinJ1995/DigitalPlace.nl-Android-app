package be.robinj.digitalplace;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by robin on 8/15/14.
 */
public class DigitalPlaceOnItemClickListener implements AdapterView.OnItemClickListener
{
	private Context context;

	public DigitalPlaceOnItemClickListener (Context context)
	{
		this.context = context;
	}

	@Override
	public void onItemClick (AdapterView<?> parent, View view, int position, long id)
	{
		try
		{
			String url = "http://digitalplace.nl/forum/viewtopic.php?" + view.getTag ();

			Intent browserIntent = new Intent (Intent.ACTION_VIEW, Uri.parse (url));
			this.context.startActivity (browserIntent);

			Home.overrideTransition (R.anim.from_x_100, R.anim.to_x_neg_100);
		}
		catch (Exception ex)
		{
			handleException ("onItemClick", ex);
		}
	}

	public void handleException (String where, Exception ex)
	{
		StringBuilder message = new StringBuilder ();
		message.append ("Oeps! Er is iets misgelopen in de method ")
			.append (where)
			.append (" () van de class DigitalPlaceOnItemClickListener!\n\n")
			.append ("Type: ")
			.append (ex.getClass ().getSimpleName ())
			.append ("\n")
			.append ("Bericht: ")
			.append (ex.getMessage ())
			.append ("\n\n")
			.append ("Stack trace:\n")
			.append (ex.getStackTrace ().toString ());

		AlertDialog.Builder messageBox = new AlertDialog.Builder (this.context);
		messageBox.setTitle ("Fout");
		messageBox.setMessage (message);
		messageBox.setCancelable (false);
		messageBox.setNeutralButton ("OK", null);

		messageBox.show ();
	}
}
