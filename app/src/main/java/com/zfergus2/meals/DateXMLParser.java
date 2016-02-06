package com.zfergus2.meals;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Parses the date in an XML file.
 * @author Zachary Ferguson
 */
public class DateXMLParser
{
	/**
	 * The date parsed from the XML.
	 */
	private Calendar parsedDate;

	/**
	 * Namespace of the xml.
	 */
	private static String ns = null;

	/**
	 * Access the date read from the XML.
	 * @return Returns a Calendar object of the date read.
	 */
	public Calendar getParsedDate()
	{
		return this.parsedDate;
	}

	/**
	 * Creates a DateXMLParser that parses the given InputStream.
	 * @param in The XML InputStream to parse.
	 * @throws XmlPullParserException If parsing goes wrong.
	 * @throws IOException If stream inaccessible.
	 */
	public DateXMLParser(InputStream in)
		throws XmlPullParserException, IOException
	{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			this.parsedDate = parseDate(parser);
		}
		catch(Exception e)
		{
			this.parsedDate = null;
			throw e;
		}
		finally
		{
			in.close();
		}
	}

	/**
	 * Parses the XML to extract the first date object.
	 * @param parser XML to parse.
	 * @return Returns a Calendar object of the date parsed.
	 * @throws XmlPullParserException If parsing goes wrong.
	 * @throws IOException If stream inaccessible.
	 */
	private static Calendar parseDate(XmlPullParser parser)
		throws XmlPullParserException, IOException
	{
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG)
			{
				continue;
			}
			String name = parser.getName();

			// Starts by looking for the entry tag
			if (name.equals("date"))
			{
				return readDate(parser);
			}
			else
			{
				skip(parser);
			}
		}

		throw new IOException("Unable to find date tag in the given XML.");
	}

	/**
	 * Reads the date between the current "date" tag.
	 * @param parser XML to read the date from.
	 * @return Returns a Calendar object of the date read.
	 * @throws XmlPullParserException If parsing goes wrong.
	 * @throws IOException If stream inaccessible.
	 */
	private static Calendar readDate(XmlPullParser parser)
		throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "date");
		int year = Integer.MIN_VALUE, month = -1, day_of_month = -1;

		while(parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG)
			{
				continue;
			}

			String name = parser.getName();
			if(name.equals("year"))
			{
				//parse year
				parser.require(XmlPullParser.START_TAG, ns, "year");
				year = Integer.parseInt(readText(parser));
				parser.require(XmlPullParser.END_TAG, ns, "year");
			}
			else if(name.equals("month"))
			{
				//parse month
				parser.require(XmlPullParser.START_TAG, ns, "month");
				month = Integer.parseInt(readText(parser));
				parser.require(XmlPullParser.END_TAG, ns, "month");
			}
			else if(name.equals("day_of_month"))
			{
				//parse day of month
				parser.require(XmlPullParser.START_TAG, ns, "day_of_month");
				day_of_month = Integer.parseInt(readText(parser));
				parser.require(XmlPullParser.END_TAG, ns, "day_of_month");
			}
			else
			{
				skip(parser);
			}
		}

		if(year == Integer.MIN_VALUE || month < 0 || day_of_month <= 0 )
		{
			throw new IOException("Invalid date parsed from XML.");
		}

		return new GregorianCalendar(year, month, day_of_month);
	}

	/**
	 * Read the text between the current tags.
	 * @param parser XML to read from.
	 * @return Return the text between the current tag.
	 * @throws XmlPullParserException If parsing goes wrong.
	 * @throws IOException If stream inaccessible.
	 */
	private static String readText(XmlPullParser parser)
		throws  XmlPullParserException, IOException
	{
		String result = "";
		if(parser.next() == XmlPullParser.TEXT)
		{
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	/**
	 * Burn through the current tag from start to end.
	 * @param parser XML Parser to burn the next tag.
	 * @throws XmlPullParserException If parsing goes wrong.
	 * @throws IOException If stream inaccessible.
	 */
	private static void skip(XmlPullParser parser)
		throws XmlPullParserException, IOException
	{
		if (parser.getEventType() != XmlPullParser.START_TAG)
		{
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0)
		{
			switch (parser.next())
			{
				case XmlPullParser.END_TAG:
					depth--;
					break;
				case XmlPullParser.START_TAG:
					depth++;
					break;
			}
		}
	}
}
