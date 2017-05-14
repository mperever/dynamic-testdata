package com.github.mperever.testdata.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to generate random UUID.
 *<p/>
 * Example of use:
 *   ${uuid} - returns random uuid
 *   ${uuid:[name]} - returns random uuid and assigns the value of uuid to the specified alias.
 * <p/>
 * For example of using UUID with name:
 *   "${uuid:[dataId]} is equal ${uuid:[dataId]}", two uuid will have the same value
 *
 * @author mperever
 *
 */
public class UUIDValueResolver extends ValueResolver
{
    //TODO: split responsibility (evaluate and parse)

    private final Map<String, String> uuids = new HashMap<>();
    private static final String NAME_TEMPLATE_REGEX = ":\\[(?<name>[^\\]]+)\\]?";

    @Override
    public String getResolverName()
    {
        return "uuid";
    }

    @Override
    public String resolve( final String source )
    {
        final String startUUID = START_DYNAMIC_VALUE + this.getResolverName();
        if ( !source.contains( startUUID ) )
        {
            return source;
        }

        // Process ${uuid}
        final String uuidWithoutParams = startUUID + END_DYNAMIC_VALUE;
        int startReplacePos = source.indexOf( uuidWithoutParams );

        String resolvedSource = source;
        while ( startReplacePos != -1 )
        {
            resolvedSource = resolvedSource.substring( 0, startReplacePos )
                    + UUID.randomUUID()
                    + resolvedSource.substring( startReplacePos + uuidWithoutParams.length() );
            startReplacePos = resolvedSource.indexOf( uuidWithoutParams );
        }

        // Process ${uuid:[name]}
        if ( !resolvedSource.contains( startUUID + ":" ) )
        {
            return resolvedSource;
        }
        final String uuidRegexTemplate = START_DYNAMIC_VALUE_REGEX
                + this.getResolverName()
                + NAME_TEMPLATE_REGEX
                + END_DYNAMIC_VALUE_REGEX;

        final Matcher matcher = Pattern.compile( uuidRegexTemplate, Pattern.DOTALL ).matcher( resolvedSource );
        while ( matcher.find() )
        {
            final String stringToReplace = matcher.group();
            final String name = matcher.group( "name" );

            resolvedSource = resolvedSource.replace( stringToReplace, this.getUUIDByName( name ) );
        }
        return resolvedSource;
    }

    private String getUUIDByName( final String source )
    {
        if ( !this.uuids.containsKey( source ) )
        {
            this.uuids.put( source, UUID.randomUUID().toString() );
        }
        return this.uuids.get( source );
    }
}