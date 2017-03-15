package be.robinj.digitalplace;

import android.util.Log;

import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Created by robin on 8/18/14.
 */
public class IrcListener extends ListenerAdapter
{
	private IrcActivity activity;

	public IrcListener (IrcActivity activity)
	{
		super ();

		this.activity = activity;
	}

	@Override
	public void onMessage (MessageEvent event)
	{
		this.activity.messageReceived (event.getUser ().getNick (), event.getMessage ());
	}

	@Override
	public void onConnect (ConnectEvent event)
	{
		this.activity.connectionEstablished ();
	}
}
