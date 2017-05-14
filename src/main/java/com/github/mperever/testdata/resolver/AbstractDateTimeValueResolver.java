package com.github.mperever.testdata.resolver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for resolving resolver date time value.
 *<p/>
 *  Template examples:
 *   ${dynamicValueName} - returns current date time in the specified 'DateTime' format.
 *   ${dynamicValueName:[+][01:02:03.004]} - current date time + 1 hour 2 minutes 3 seconds 4 mills
 *
 * @author mperever
 *
 */
public abstract class AbstractDateTimeValueResolver extends ValueResolver
{
    private static final Logger logger = LoggerFactory.getLogger( AbstractDateTimeValueResolver.class );

    protected abstract String getDateTimeFormat();

    @Override
    public String resolve( final String source )
    {
        final String dynamicDateTimeFormat = this.getDateTimeFormat();
        final String dynamicValueName = this.getResolverName();

        // Replace resolver date time value to today date time.
        final String todayDynamicDateTime = START_DYNAMIC_VALUE
                + dynamicValueName
                + END_DYNAMIC_VALUE;

        String resolvedSource = source;
        if ( source.contains( todayDynamicDateTime ) )
        {
            resolvedSource = source.replace(
                    todayDynamicDateTime,
                    new SimpleDateFormat( dynamicDateTimeFormat )
                            .format( Calendar.getInstance()
                            .getTime() ) );
        }

        // Fast way to identify whether source contains shifted resolver date time value
        final String startDynamicShiftedDateTime = START_DYNAMIC_VALUE + dynamicValueName + ":";
        if ( !resolvedSource.contains( startDynamicShiftedDateTime ) )
        {
            return resolvedSource;
        }

        // Try to shift current date time
        final String dateTimeOffsetTemplate = START_DYNAMIC_VALUE_REGEX
                + dynamicValueName
                + ":"
                + DateTimeShifter.OFFSET_TEMPLATE_REGEX
                + END_DYNAMIC_VALUE_REGEX;

        final Matcher matcher = Pattern.compile( dateTimeOffsetTemplate, Pattern.DOTALL ).matcher( resolvedSource );
        final Date nowDate = Calendar.getInstance().getTime();
        while ( matcher.find() )
        {
            final String dateToReplace = matcher.group();
            try
            {
                final DateTimeShifter dtShifter = new DateTimeShifter(
                        matcher.group( "direction" ),
                        matcher.group( "date" ) );
                final long shiftedTime = dtShifter.shiftDateTime( nowDate );
                resolvedSource = resolvedSource.replace( dateToReplace, new SimpleDateFormat( dynamicDateTimeFormat )
                        .format( new Date( shiftedTime ) ) );
            } catch ( ParseException e )
            {
                logger.error( e.getMessage(), e );
            }
        }
        return resolvedSource;
    }
}