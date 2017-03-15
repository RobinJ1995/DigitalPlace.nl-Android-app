package be.robinj.digitalplace;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robin on 8/15/14.
 */
public class DigitalPlace
{
	private final String apiUrl = "http://digitalplace.nl/api/legacy/forum";

	public List<DigitalPlacePost> getTopics () throws Exception
	{
		return this.getApiData ("posts");
	}

	public List<DigitalPlacePost> getPosts () throws Exception
	{
		return this.getApiData ("replies");
	}

	private List<DigitalPlacePost> getApiData (String mode) throws Exception
	{
		List<DigitalPlacePost> data = new ArrayList<DigitalPlacePost> ();

		URL url = new URL (this.apiUrl + "?mode=" + mode);
		InputStream stream = url.openStream ();
		InputStreamReader streamReader = new InputStreamReader (stream);
		BufferedReader reader = new BufferedReader (streamReader);

		String line;

		while ((line = reader.readLine ()) != null)
			data.add (DigitalPlacePost.fromString (line));

		return data;
	}
}
