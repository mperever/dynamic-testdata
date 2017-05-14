package com.github.mperever.testdata.resolver;

/**
 * Class to resolve current date time value.
 *<p/>
 * Example of use:
 *   ${date} - returns current date time in 'Date' format of RDF
 *   ${date:[-][01:02:03.004]} - current date time - 1 hour 2 minutes 3 seconds 4 milliseconds
 *   ${date:[+][01:02:03.004]} - current date time + 1 hour 2 minutes 3 seconds 4 milliseconds
 *
 * @author mperever
 *
 */
public class DateValueResolver extends AbstractDateTimeValueResolver
{
    @Override
    public String getResolverName()
    {
        return "date";
    }

    @Override
    protected String getDateTimeFormat()
    {
        return "yyyy-MM-dd";
    }
}