package be.robinj.digitalplace;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by robin on 8/15/14.
 */
public class DigitalPlacePost
{
	private String forum;
	private String user;
	private String title;
	private String url;

	public DigitalPlacePost (String forum, String user, String title, String url)
	{
		this.forum = forum;
		this.user = user;
		this.title = title;
		this.url = url;
	}

	private DigitalPlacePost ()
	{
	}

	public static DigitalPlacePost fromString (String line) throws Exception
	{
		//REGEX// \[([^\]]+)\]\s(\<([^\>]+)\>)?\s?(.+)\:\shttp(s)?\:\/\/(www\.)?digitalplace\.nl\/forum\/\?topic\=(.+) //

		DigitalPlacePost post = new DigitalPlacePost ();

		Pattern regex = Pattern.compile ("\\[([^\\]]+)\\]\\s(\\<([^\\>]+)\\>)?\\s?(.+)\\:\\shttp(s)?\\:\\/\\/(www\\.)?digitalplace\\.nl\\/forum\\/\\?topic\\=(.+)", 0);
		Matcher matcher = regex.matcher (line);
		matcher.matches ();

		post.setForum (matcher.group (1));
		post.setUser (matcher.group (3));
		post.setTitle (matcher.group (4).replace ("[you]", "Gebruiker"));
		post.setUrl (matcher.group (7));

		String[] matches = new String[matcher.groupCount ()];
		for (int i = 0; i < matcher.groupCount (); i++)
			matches[i] = matcher.group (i + 1);

		return post;
	}

	public String getForum ()
	{
		return forum;
	}

	public void setForum (String forum)
	{
		this.forum = forum;
	}

	public String getUser ()
	{
		return user;
	}

	public void setUser (String user)
	{
		this.user = user;
	}

	public String getTitle ()
	{
		return title;
	}

	public void setTitle (String title)
	{
		this.title = title;
	}

	public String getUrl ()
	{
		return url;
	}

	public void setUrl (String url)
	{
		this.url = url;
	}
}
