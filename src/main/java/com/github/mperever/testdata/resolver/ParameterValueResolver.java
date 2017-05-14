package com.github.mperever.testdata.resolver;

import java.util.Map;

/**
 * Class to resolve parameter value from the specified context.
 *<p/>
 * Example of use:
 *   ${#parameter} - returns the value of parameter, for example: ${#domain}
 *
 * @author mperever
 *
 */
public class ParameterValueResolver extends ValueResolver
{
    private final Map<String, String> parameters;

    //TODO: try split responsibility (evaluate and parse)

    public ParameterValueResolver( final Map<String, String> parameters )
    {
        this.parameters = parameters;
    }

    @Override
    public String getResolverName()
    {
        return "#";
    }

    @Override
    public String resolve( final String source )
    {
        final String startDynamicParameter = START_DYNAMIC_VALUE + this.getResolverName();
        if ( !source.contains( startDynamicParameter ) || !source.contains( END_DYNAMIC_VALUE ) )
        {
            return source;
        }
        if ( this.parameters.isEmpty() )
        {
            return source;
        }

        String resolvedSource = source;
        for ( Map.Entry<String, String> parameterItem : this.parameters.entrySet() )
        {
            final String paramName = parameterItem.getKey();
            final String parameterValue = parameterItem.getValue();

            resolvedSource = resolvedSource.replace( startDynamicParameter
                    + paramName
                    + END_DYNAMIC_VALUE, parameterValue );
        }
        return resolvedSource;
    }
}