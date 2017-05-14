package com.github.mperever.testdata.resolver;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to resolve format string value from the specified context.
 *<p/>
 * Example of use:
 *   ${{@literal @}name} - returns the value of formatted string for specified name
 *   ${{@literal @}name:[arg1][argN]} - returns the value of formatted string for specified name and arguments
 *
 * @author mperever
 *
 */
public class FormatStringResolver extends ValueResolver
{
    private static final String ARGUMENTS_TEMPLATE_REGEX = ":(?<args>(\\[([^\\]]+)\\])+)?";
    private final Map<String, String> formatStrings;

    //TODO: try split responsibility (evaluate and parse)

    public FormatStringResolver( final Map<String, String> formatStrings )
    {
        this.formatStrings = formatStrings;
    }

    @Override
    public String getResolverName()
    {
        return "@";
    }

    @Override
    public String resolve( final String source )
    {
        final String startDynamicString = START_DYNAMIC_VALUE + this.getResolverName();
        if ( !source.contains( startDynamicString ) || !source.contains( END_DYNAMIC_VALUE ) )
        {
            return source;
        }
        if ( formatStrings.isEmpty() )
        {
            return source;
        }

        String resolvedSource = source;
        for ( Map.Entry<String, String> formatStringItem : formatStrings.entrySet() )
        {
            final String stringName = formatStringItem.getKey();
            final String stringValue = formatStringItem.getValue();
            final String startConcreteDynamicString = startDynamicString + stringName;
            // process ${@name} resolver value
            resolvedSource = resolvedSource.replace( startConcreteDynamicString + END_DYNAMIC_VALUE, stringValue );

            // process ${@name:[arg1][argN]} resolver value
            if ( !resolvedSource.contains( startConcreteDynamicString + ":" ) )
            {
                continue;
            }
            final String formatStringRegexTemplate = START_DYNAMIC_VALUE_REGEX
                    + this.getResolverName()
                    + stringName
                    + ARGUMENTS_TEMPLATE_REGEX
                    + END_DYNAMIC_VALUE_REGEX;

            final Matcher matcher = Pattern.compile( formatStringRegexTemplate, Pattern.DOTALL )
                    .matcher( resolvedSource );

            while ( matcher.find() )
            {
                final String stringToReplace = matcher.group();
                final String strArgs = matcher.group( "args" );
                final String[] args = readFormatStringArguments( strArgs );
                //noinspection ConfusingArgumentToVarargsMethod
                resolvedSource = resolvedSource.replace( stringToReplace, String.format( stringValue, args ) );
            }
        }
        return resolvedSource;
    }

    private static String[] readFormatStringArguments( String strArgs )
    {
        if ( strArgs == null || strArgs.isEmpty() )
        {
            return new String[] {};
        }
        final String[] args = strArgs.split( "\\]" );
        for ( int i = 0; i < args.length; i++ )
        {
            if ( args[i].length() <= 1 )
            {
                continue;
            }
            args[i] = args[i].substring( 1 );
        }
        return args;
    }
}