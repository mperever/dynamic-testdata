package com.github.mperever.testdata;

import com.github.mperever.testdata.resolver.DateTimeValueResolver;
import com.github.mperever.testdata.resolver.DateValueResolver;
import com.github.mperever.testdata.resolver.RandomValueResolver;
import com.github.mperever.testdata.resolver.UUIDValueResolver;
import com.github.mperever.testdata.resolver.ValueResolver;

import java.util.HashSet;
import java.util.Set;

/**
 * A central class to resolve resolver values in string (test data).
 *<p/>
 * The list of default resolver values:
 *   ${uuid} - returns random UUID
 *   ${uuid:[name]} - returns random uuid and assigns the value of uuid to the specified alias.
 *   ${date} - returns current date time in 'Date' format of RDF
 *   ${date:[-][01:02:03.004]} - current date time - 1 hour 2 minutes 3 seconds 4 milliseconds
 *   ${date:[+][01:02:03.004]} - current date time + 1 hour 2 minutes 3 seconds 4 milliseconds
 *   ${datetime} - returns current date time in 'DateTime' format of RDF
 *   ${datetime:[+][01:02:03.004]} - current date time - 1 hour 2 minutes 3 seconds 4 milliseconds
 *
 * @author mperever
 *
 */
public class DynamicValueResolver
{
    private final Set<ValueResolver> resolvers = new HashSet<>();

    /**
     * Constructs the resolver value resolver with default resolvers.
     *<p/>
     *  The default resolvers:
     *     {@link UUIDValueResolver}
     *     {@link RandomValueResolver}
     *     {@link DateValueResolver}
     *     {@link DateTimeValueResolver}
     */
    public DynamicValueResolver()
    {
        this.resolvers.add( new UUIDValueResolver() );
        this.resolvers.add( new RandomValueResolver() );
        this.resolvers.add( new DateValueResolver() );
        this.resolvers.add( new DateTimeValueResolver() );
    }

    /**
     * Register a new value resolver.
     *
     * @param resolver The instance of value resolver
     * @return <tt>true</tt> if 'value resolver' is registered.
     */
    public boolean register( final ValueResolver resolver )
    {
        return resolver != null && this.resolvers.add( resolver );
    }

    /**
     * Unregister the specified class of value register.
     *
     * @param resolverClass the class of value register
     * @return <tt>true</tt> if operation is success
     */
    public boolean unregister( final Class resolverClass )
    {
        final ValueResolver resolver = this.getResolverByClass( resolverClass );
        return resolver != null && this.resolvers.remove( resolver );
    }

    /**
     * Resolve/replace resolver values in the specified source string.
     * Registered 'value resolvers' are used for resolving.
     *
     * @param source The string with dynamice values
     * @return Resolved string
     */
    public String resolve( final String source )
    {
        if ( source == null )
        {
            return null;
        }
        String resolvedSource = source;
        for ( ValueResolver resolver : this.resolvers )
        {
            resolvedSource = resolver.resolve( resolvedSource );
        }
        return resolvedSource;
    }

    protected ValueResolver getResolverByClass( final Class type )
    {
        for ( ValueResolver resolver : this.resolvers )
        {
            if ( resolver.getClass().equals( type ) )
            {
                return resolver;
            }
        }
        return null;
    }
}