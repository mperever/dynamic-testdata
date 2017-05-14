package com.github.mperever.testdata.resolver;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to generate random number value.
 *<p/>
 * Example of use:
 *   ${rnd:[1][10]} - returns random number between 1 and 10
 *
 * @author mperever
 *
 */
public class RandomValueResolver extends ValueResolver
{
    //TODO: try split responsibility (evaluate and parse)

    private final Random random = new Random();
    private static final String RANGE_TEMPLATE_REGEX = ":\\[(?<min>\\d+)\\]\\[(?<max>\\d+)\\]?";

    @Override
    public String getResolverName()
    {
        return "rnd";
    }

    @Override
    public String resolve( final String source )
    {
        final String startDynamicRnd = START_DYNAMIC_VALUE + this.getResolverName() + ":";
        if ( !source.contains( startDynamicRnd ) )
        {
            return source;
        }
        final String rndRegexTemplate = START_DYNAMIC_VALUE_REGEX
                + this.getResolverName()
                + RANGE_TEMPLATE_REGEX
                + END_DYNAMIC_VALUE_REGEX;

        final Matcher matcher = Pattern.compile( rndRegexTemplate, Pattern.DOTALL ).matcher( source );

        String resolvedSource = source;
        while ( matcher.find() )
        {
            final String stringToReplace = matcher.group();
            final int min = Integer.parseInt( matcher.group( "min" ) );
            final int max = Integer.parseInt( matcher.group( "max" ) );
            final int rndNumberValue = this.random.nextInt( max - min + 1 ) + min;
            int startReplacePos = resolvedSource.indexOf( stringToReplace );
            if ( startReplacePos != -1 )
            {
                resolvedSource = resolvedSource.substring( 0, startReplacePos )
                        + rndNumberValue
                        + resolvedSource.substring( startReplacePos + stringToReplace.length() );
            }
        }
        return resolvedSource;
    }
}