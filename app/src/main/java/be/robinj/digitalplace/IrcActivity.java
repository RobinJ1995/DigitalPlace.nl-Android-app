package be.robinj.digitalplace;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class IrcActivity extends Activity
{
	private Irc irc;

	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_irc);
	}


	@Override
	public boolean onCreateOptionsMenu (Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater ().inflate (R.menu.irc, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		try
		{
			int id = item.getItemId ();

			if (id == android.R.id.home)
			{
				this.onBackPressed ();

				return true;
			}
		}
		catch (Exception ex)
		{
			this.handleException ("onOptionsItemSelected", ex);
		}

		return super.onOptionsItemSelected (item);

	}

	@Override
	public void onBackPressed ()
	{
		try
		{
			if (this.irc != null)
				this.irc.disconnect ();

			super.onBackPressed ();
		}
		catch (Exception ex)
		{
			this.handleException ("onBackPressed", ex);
		}
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
			.append (" () van de class IrcActivity!\n\n")
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
		dlg.setCancelable (true);
		dlg.setNeutralButton ("OK", null);

		dlg.show ();
	}

	public void btnConnect_clicked (View view)
	{
		try
		{
			ProgressBar pbConnecting = (ProgressBar) this.findViewById (R.id.pbConnecting);
			EditText etMessage = (EditText) this.findViewById (R.id.etMessage);
			Button btnSay = (Button) this.findViewById (R.id.btnSay);

			view.setVisibility (View.GONE);
			pbConnecting.setVisibility (View.VISIBLE);
			etMessage.setVisibility (View.VISIBLE);
			btnSay.setVisibility (View.VISIBLE);

			this.irc = new Irc (this);

			final IrcActivity me = this;
			final Irc irc = this.irc;

			Runnable runnable = new Runnable ()
			{
				@Override
				public void run ()
				{
					try
					{
						irc.connect ();
					}
					catch (Exception ex)
					{
						me.handleException ("btnConnect_clicked", ex);
					}
				}
			};
			Thread thread = new Thread (runnable);
			thread.start ();
		}
		catch (Exception ex)
		{
			this.handleException ("btnConnect_clicked", ex);
		}
	}

	public void btnSay_clicked (View view)
	{
		try
		{
			EditText etMessage = (EditText) this.findViewById (R.id.etMessage);
			String message = etMessage.getText ().toString ();

			if (! message.isEmpty ())
				this.irc.sendMessage (message);

			etMessage.setText ("");
		}
		catch (Exception ex)
		{
			this.handleException ("btnSay_clicked", ex);
		}
	}

	public void connectionEstablished ()
	{
		final IrcActivity me = this;

		Runnable runnable = new Runnable ()
		{
			@Override
			public void run ()
			{
				try
				{
					ProgressBar pbConnecting = (ProgressBar) me.findViewById (R.id.pbConnecting);
					EditText etMessage = (EditText) me.findViewById (R.id.etMessage);
					Button btnSay = (Button) me.findViewById (R.id.btnSay);
					TextView tvLog = (TextView) me.findViewById (R.id.tvLog);

					pbConnecting.setVisibility (View.GONE);
					etMessage.setEnabled (true);
					btnSay.setEnabled (true);
					tvLog.setText (Html.fromHtml ("<b>Verbonden met server</b>"));
				}
				catch (Exception ex)
				{
					me.handleException ("connectionEstablished", ex);
				}
			}
		};

		this.runOnUiThread (runnable);
	}

	public void messageReceived (final String user, final String message)
	{
		this.messageReceived (user, message, false);
	}

	public void messageReceived (final String user, final String message, final boolean fromSelf)
	{
		final IrcActivity me = this;

		Runnable runnable = new Runnable ()
		{
			@Override
			public void run ()
			{
				try
				{
					TextView tvLog = (TextView) me.findViewById (R.id.tvLog);

					StringBuilder str = new StringBuilder (tvLog.getText ())
						.append ("\n<")
						.append (fromSelf ? Html.fromHtml ("<b>" + user + "</b>") : user)
						.append ("> ")
						.append (message);

					tvLog.setText (str.toString ());
				}
				catch (Exception ex)
				{
					me.handleException ("messageReceived", ex);
				}
			}
		};

		this.runOnUiThread (runnable);
	}
}
