package com.github.mperever.testdata.resolver;

/**
 * Class to resolve current date time value.
 *<p/>
 * Example of use:
 *   ${datetime} - returns current date time in 'DateTime' format of RDF
 *   ${datetime:[+][01:02:03.004]} - current date time + 1 hour 2 minutes 3 seconds 4 milliseconds
 *
 * @author mperever
 *
 */
public class DateTimeValueResolver extends AbstractDateTimeValueResolver
{
    @Override
    public String getResolverName()
    {
        return "datetime";
    }

    @Override
    protected String getDateTimeFormat()
    {
        return "yyyy-MM-dd HH:mm:ss.SSS";
    }
}