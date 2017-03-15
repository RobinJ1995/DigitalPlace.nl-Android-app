package be.robinj.digitalplace;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;

/**
 * Created by robin on 8/18/14.
 */
public class Irc
{
	private PircBotX pbx;
	private IrcActivity activity;
	private final String channel = "#DPF";

	public Irc (IrcActivity activity)
	{
		this.activity = activity;

		Configuration.Builder config = new Configuration.Builder ();
		config.setName ("appUser");
		config.setLogin ("dpandroid");
		config.setRealName ("DigitalPlace.nl Android App");
		config.setVersion ("DigitalPlace.nl Android App");
		config.setAutoNickChange (true);
		config.addAutoJoinChannel (this.channel);
		config.setAutoReconnect (false);
		config.setServer ("irc.digitalplace.nl", 6667);
		config.setAutoSplitMessage (true);
		config.addListener (new IrcListener (activity));

		this.pbx = new PircBotX (config.buildConfiguration ());
	}

	public void connect () throws IOException, IrcException
	{
		this.pbx.startBot ();
	}

	public void disconnect ()
	{
		if (this.pbx.isConnected ())
			this.pbx.sendIRC ().quitServer ("Gebruiker heeft verbinding verbroken");
	}

	public void sendMessage (String message)
	{
		this.pbx.sendIRC ().message (this.channel, message);

		this.activity.messageReceived (this.getNick (), message, true);
	}

	public String getNick ()
	{
		return this.pbx.getNick ();
	}
}
