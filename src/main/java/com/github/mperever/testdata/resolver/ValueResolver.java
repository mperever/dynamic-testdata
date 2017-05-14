package com.github.mperever.testdata.resolver;

/**
 * A common interface for resolver value resolving.
 *
 * @author mperever
 *
 */
public abstract class ValueResolver
{
    protected static final String START_DYNAMIC_VALUE = "${";
    protected static final String END_DYNAMIC_VALUE = "}";
    protected static final String START_DYNAMIC_VALUE_REGEX = "\\$\\{";
    protected static final String END_DYNAMIC_VALUE_REGEX = "\\}";

    /**
     * Returns the name of resolver value that is used by resolver.
     *
     * @return resolver value name
     */
    public abstract String getResolverName();

    /**
     * Replace resolver values with concrete one.
     *
     * @param source The string with resolver values to resolve/replace
     * @return String with resolved resolver values
     */
    public abstract String resolve( final String source );

    @Override
    public int hashCode()
    {
        return this.getResolverName().hashCode();
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( this.getClass().equals( obj.getClass() ) )
        {
            return true;
        }
        return false;
    }
}