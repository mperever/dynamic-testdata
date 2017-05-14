package com.github.mperever.testdata.resolver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to shift a date to the specified offset.
 *
 * @author mperever
 *
 */
public class DateTimeShifter
{
    public static final String FORWARD_OFFSET = "+";
    public static final String BACK_OFFSET = "-";
    public static final String OFFSET_FORMAT = "HH:mm:ss.SSS";
    public static final String OFFSET_TEMPLATE_REGEX =
            "\\[(?<direction>\\+|\\-)\\]\\[(?<date>\\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\]";

    private final String offsetDirection;
    private final Date offsetDate;

    /**
     * Constructor with parameters for date shifting.
     *
     * @param offsetDirection Shifting direction: "+" or "-"
     * @param offsetDate The string value of date time that is used for shifting (HH:mm:ss.SSS)
     * @throws ParseException if offsetDate has wrong format
     */
    public DateTimeShifter( String offsetDirection, String offsetDate ) throws ParseException
    {
        if ( offsetDirection != null
                && !FORWARD_OFFSET.equals( offsetDirection )
                && !BACK_OFFSET.equals( offsetDirection ) )
        {
            throw new IllegalArgumentException(
                    String.format( "The parameter 'offsetDirection' can be only '%1$s' or '%2$s'",
                            FORWARD_OFFSET, BACK_OFFSET ) );
        }
        if ( offsetDate == null || offsetDate.isEmpty() )
        {
            throw new IllegalArgumentException( "The parameter 'offsetDate' is not specified." );
        }
        this.offsetDirection = offsetDirection;
        final SimpleDateFormat dateOffsetFormat = new SimpleDateFormat( OFFSET_FORMAT );
        dateOffsetFormat.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
        this.offsetDate = dateOffsetFormat.parse( offsetDate );
    }

    /**
     * Shift the specified date.
     *
     * @param dateToShift The date to shift
     * @return Returns shifted date time in milliseconds
     */
    public long shiftDateTime( final Date dateToShift )
    {
        if ( dateToShift == null )
        {
            throw new IllegalArgumentException( "The parameter 'dateToShift' is null." );
        }
        // Offset date regarding to the offset direction
        long offsetResult = 0;
        if ( FORWARD_OFFSET.equals( this.offsetDirection ) )
        {
            offsetResult = dateToShift.getTime() + this.offsetDate.getTime();
        }
        if ( BACK_OFFSET.equals( this.offsetDirection ) )
        {
            offsetResult = dateToShift.getTime() - this.offsetDate.getTime();
        }
        return offsetResult;
    }

    /**
     * Create shifter from parsable string.
     *<p/>
     * The Parsable string examples:
     *  [+][01:02:03.004] - Forward offset to 1 hour 2 minutes 3 seconds 4 milliseconds
     *  [-][01:02:03.004] - Back offset to 1 hour 2 minutes 3 seconds 4 milliseconds
     *
     * @param offsetString parsable string
     * @return The shifter object
     * @throws ParseException if offsetString has wrong format or data.
     */
    public static DateTimeShifter fromParsableString( String offsetString ) throws ParseException
    {
        final Matcher matcher =
                Pattern.compile( OFFSET_TEMPLATE_REGEX, Pattern.DOTALL ).matcher( offsetString );
        if ( !matcher.find() )
        {
            throw new ParseException( "Could not find 'offsetDirection' and 'offsetDate' in string: "
                    + offsetString, 0 );
        }
        return new DateTimeShifter( matcher.group( "direction" ), matcher.group( "date" ) );
    }
}