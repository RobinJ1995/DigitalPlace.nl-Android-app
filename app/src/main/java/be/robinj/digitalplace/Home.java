package be.robinj.digitalplace;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;


public class Home extends Activity
	implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private DpFragment fragment;
	private static Home context;

	public static Context getContext ()
	{
		return Home.context;
	}

	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		try
		{
			super.onCreate (savedInstanceState);
			setContentView (R.layout.activity_home);

			mNavigationDrawerFragment = (NavigationDrawerFragment)
				getFragmentManager ().findFragmentById (R.id.navigation_drawer);
			mTitle = getTitle ();

			// Set up the drawer.
			mNavigationDrawerFragment.setUp (
				R.id.navigation_drawer,
				(DrawerLayout) findViewById (R.id.drawer_layout));

			Home.context = this;
		}
		catch (Exception ex)
		{
			handleException ("onCreate", ex);
		}
	}

	@Override
	public void onNavigationDrawerItemSelected (int position)
	{
		try
		{
			// update the main content by replacing fragments
			this.fragment = DpFragment.newInstance (this, position + 1);
			FragmentManager fragmentManager = getFragmentManager ();
			fragmentManager.beginTransaction ()
				.replace (R.id.container, this.fragment)
				.commit ();
		}
		catch (Exception ex)
		{
			handleException ("onNavigationDrawerItemSelected", ex);
		}
	}

	public void onSectionAttached (int number)
	{
		try
		{
			switch (number)
			{
				case 1:
					this.mTitle = getString (R.string.title_topics);
					break;
				case 2:
					this.mTitle = getString (R.string.title_posts);
					break;
			}
		}
		catch (Exception ex)
		{
			handleException ("onSectionAttached", ex);
		}
	}

	public void restoreActionBar ()
	{
		try
		{
			ActionBar actionBar = getActionBar ();
			actionBar.setNavigationMode (ActionBar.NAVIGATION_MODE_STANDARD);
			actionBar.setDisplayShowTitleEnabled (true);
			actionBar.setTitle (mTitle);
		}
		catch (Exception ex)
		{
			handleException ("restoreActionBar", ex);
		}
	}


	@Override
	public boolean onCreateOptionsMenu (Menu menu)
	{
		boolean returnValue = false;

		try
		{
			if (! mNavigationDrawerFragment.isDrawerOpen ())
			{
				// Only show items in the action bar relevant to this screen
				// if the drawer is not showing. Otherwise, let the drawer
				// decide what to show in the action bar.
				getMenuInflater ().inflate (R.menu.home, menu);
				restoreActionBar ();
				return true;
			}

			returnValue = super.onCreateOptionsMenu (menu);
		}
		catch (Exception ex)
		{
			handleException ("onCreateOptionsMenu", ex);
		}

		return returnValue;
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		boolean returnValue = false;

		try
		{
			int id = item.getItemId ();

			if (id == R.id.action_about)
			{
				Intent intent = new Intent (this, About.class);
				this.startActivity (intent);

				this.overridePendingTransition (R.anim.from_x_100, R.anim.to_x_neg_100);

				return true;
			}
			else if (id == R.id.action_refresh)
			{
				this.fragment.refresh ();

				return true;
			}
			else if (id == R.id.action_website)
			{
				Intent browserIntent = new Intent (Intent.ACTION_VIEW, Uri.parse ("http://www.digitalplace.nl/"));
				this.startActivity (browserIntent);

				this.overridePendingTransition (R.anim.from_x_100, R.anim.to_x_neg_100);

				return true;
			}
			else if (id == R.id.action_irc)
			{
				Intent intent = new Intent (this, IrcActivity.class);
				this.startActivity (intent);

				this.overridePendingTransition (R.anim.from_x_100, R.anim.to_x_neg_100);

				return true;
			}

			returnValue = super.onOptionsItemSelected (item);
		}
		catch (Exception ex)
		{
			handleException ("onOptionItemSelected", ex);
		}

		return returnValue;
	}

	@Override
	public void onResume ()
	{
		super.onResume ();

		this.overridePendingTransition (R.anim.from_x_neg_100, R.anim.to_x_100);
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
			.append (" () van de class Home!\n\n")
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

	public static void overrideTransition (int enter, int exit)
	{
		Home.context.overridePendingTransition (enter, exit);
	}

	public static class DpFragment extends Fragment
	{
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private DigitalPlace dp = new DigitalPlace ();
		private View view; // this.getView () returns NULL, essentially making it useless //
		private int sectionNumber;

		public DpFragment ()
		{
		}

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static DpFragment newInstance (Context context, int sectionNumber)
		{
			DpFragment fragment = new DpFragment ();
			Bundle args = new Bundle ();
			args.putInt (ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments (args);
			return fragment;
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
				.append (" () van de class DpFragment!\n\n")
				.append ("Type: ")
				.append (ex.getClass ().getSimpleName ())
				.append ("\n")
				.append ("Bericht: ")
				.append (ex.getMessage ())
				.append ("\n\n")
				.append ("UnknownHostException".equals (ex.getClass ().getSimpleName ()) ? "Waarschijnlijk ben je niet verbonden met het internet.\n\n" : "")
				.append ("Stack trace:\n")
				.append (stackTrace.toString ());

			AlertDialog.Builder dlg = new AlertDialog.Builder (Home.getContext ());
			dlg.setTitle ("Fout");
			dlg.setMessage (message.toString ());
			dlg.setCancelable (true);
			dlg.setNeutralButton ("OK", null);

			dlg.show ();
		}

		@Override
		public View onCreateView (LayoutInflater inflater, ViewGroup container,
		                          Bundle savedInstanceState)
		{
			View rootView = inflater.inflate (R.layout.fragment_home, container, false);
			try
			{
				this.view = rootView;

				this.refresh (this.sectionNumber == 2);
			}
			catch (Exception ex)
			{
				this.handleException ("onCreateView", ex);
			}

			return rootView;
		}

		@Override
		public void onAttach (Activity activity)
		{
			try
			{
				super.onAttach (activity);

				this.sectionNumber = this.getArguments ().getInt (ARG_SECTION_NUMBER);
				((Home) activity).onSectionAttached (
					this.sectionNumber);
			}
			catch (Exception ex)
			{
				this.handleException ("onAttach", ex);
			}
		}

		@Override
		public boolean onOptionsItemSelected (MenuItem item)
		{
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			boolean returnValue = false;

			try
			{
				int id = item.getItemId ();

				returnValue = super.onOptionsItemSelected (item);
			}
			catch (Exception ex)
			{
				handleException ("onOptionItemSelected", ex);
			}

			return returnValue;
		}

		public void refresh () throws Exception
		{
			this.refresh (this.sectionNumber == 2);
		}

		private void refresh (final boolean allPosts) throws Exception
		{
			final Activity activity = this.getActivity ();
			final View view = this.view;
			final DigitalPlace dp = this.dp;
			final DpFragment me = this;

			Thread thread = new Thread
			(
				new Runnable ()
				{
					@Override
					public void run ()
					{
						try
						{
							List<DigitalPlacePost> posts = (allPosts ? dp.getPosts () : dp.getTopics ());
							final DigitalPlaceAdapter adapter = new DigitalPlaceAdapter (activity, posts);

							activity.runOnUiThread
							(
								new Runnable ()
								{
									@Override
									public void run ()
									{
										ListView listView = (ListView) view.findViewById (R.id.listView);
										listView.setOnItemClickListener (new DigitalPlaceOnItemClickListener (activity));
										listView.setAdapter (adapter);
									}
								}
							);
						}
						catch (final Exception ex)
						{
							activity.runOnUiThread
							(
								new Runnable ()
								{
									@Override
									public void run ()
									{
										me.handleException ("refresh", ex);
									}
								}
							);
						}
					}
				}
			);
			thread.start ();
		}
	}
}
