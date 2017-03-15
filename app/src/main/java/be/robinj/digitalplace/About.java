package be.robinj.digitalplace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class About extends Activity
{
	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_about);

		try
		{
			Home.overrideTransition (R.anim.from_x_100, R.anim.to_x_neg_100);

			PackageInfo pkgInfo = this.getPackageManager ().getPackageInfo (this.getPackageName (), 0);

			TextView tvVersion = (TextView) this.findViewById (R.id.tvVersion);
			tvVersion.setText ("v" + pkgInfo.versionName);
		}
		catch (Exception ex)
		{
			Log.d ("ERROR", "Fout bij ophalen van versienummer");
		}
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater ().inflate (R.menu.about, menu);
		return true;
	}

	public void handleException (String where, Exception ex)
	{
		StringBuilder message = new StringBuilder ();
		StringBuilder stackTrace = new StringBuilder ();

		for (StackTraceElement ste : ex.getStackTrace ())
			stackTrace.append (ste.toString ())
				.append ("\n");

		message.append ("Oeps! Er is iets misgelopen in de method ")
			.append (where)
			.append (" () van de class About!\n\n")
			.append ("Type: ")
			.append (ex.getClass ().getSimpleName ())
			.append ("\n")
			.append ("Bericht: ")
			.append (ex.getMessage ())
			.append ("\n\n")
			.append ("UnknownHostException".equals (ex.getClass ().getSimpleName ()) ? "Waarschijnlijk ben je niet verbonden met het internet.\n\n" : "")
			.append ("Stack trace:\n")
			.append (stackTrace.toString ());

		AlertDialog.Builder dlg = new AlertDialog.Builder (this);
		dlg.setTitle ("Fout");
		dlg.setMessage (message.toString ());
		dlg.setCancelable (false);
		dlg.setNeutralButton ("OK", null);

		dlg.show ();
	}

	public void btnDp_clicked (View view)
	{
		try
		{
			Intent browserIntent = new Intent (Intent.ACTION_VIEW, Uri.parse ("http://www.digitalplace.nl/"));
			this.startActivity (browserIntent);
		}
		catch (Exception ex)
		{
			handleException ("btnDp_clicked", ex);
		}
	}

	public void btnDev_clicked (View view)
	{
		try
		{
			Intent browserIntent = new Intent (Intent.ACTION_VIEW, Uri.parse ("http://www.robinj.be/"));
			this.startActivity (browserIntent);
		}
		catch (Exception ex)
		{
			handleException ("btnDev_clicked", ex);
		}
	}
}
