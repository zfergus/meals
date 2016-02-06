package com.zfergus2.meals;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

/**
 * Downloads a XML file asynchronously.
 * @author Zachary Ferguson
 */
public class DownloadXmlTask extends AsyncTask<String, Void, Calendar>
{
	/**
	 * Load the urls[0] and read the date from it.
	 * @param urls List of urls. Only 0th element used.
	 * @return Returns date read from url.
	 */
	@Override
	protected Calendar doInBackground(String... urls)
	{
		try
		{
			InputStream stream = this.downloadUrl(urls[0]);
			DateXMLParser dateXMLParser = new DateXMLParser(stream);

			if(dateXMLParser.getParsedDate() == null)
			{
				throw new Exception("Invalid date parsed from XML.");
			}
			else
			{
				return dateXMLParser.getParsedDate();
			}
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
			return Meals.END_DATE;
		}
	}

	/**
	 * Sets the default end date to the result.
	 * @param result The resulting date of doInBackground().
	 */
	@Override
	protected void onPostExecute(Calendar result)
	{
		MainActivity.defaultEndDate = result;
	}

	/**
	 * Connect to the given url and get an InputStream.
	 * @param urlString String of the URL to connect to.
	 * @return Returns a stream of the URL's contents.
	 * @throws IOException If unable to connect to the URL.
	 */
	private InputStream downloadUrl(String urlString) throws IOException
	{
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// Starts the query
		conn.connect();
		return conn.getInputStream();
	}
}
