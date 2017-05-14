package com.github.mperever.testdata;

import org.junit.Assert;
import org.junit.Test;

/**
 * Represents unit tests for {@link DynamicValueResolver} class.
 *
 * @author mperever
 *
 */
public class DynamicValueResolverTests
{
    @Test
    public void registerAndUnregister_test()
    {
        final DynamicValueResolver resolver = new DynamicValueResolver();

        // Register test
        final boolean isRegisterSuccess = resolver.register( new SampleValueResolver() );
        Assert.assertTrue( "Sample value register has not been registered.",
                isRegisterSuccess );

        final boolean isSameRegisterSuccess = resolver.register( new SampleValueResolver() );
        Assert.assertFalse( "Sample value register has been registered once again.",
                isSameRegisterSuccess );

        // Unregister
        final boolean isUnRegisterSuccess = resolver.unregister( SampleValueResolver.class );
        Assert.assertTrue( "Sample value register has not been unregistered.",
                isUnRegisterSuccess );

        final boolean isSameUnRegisterSuccess = resolver.unregister( SampleValueResolver.class );
        Assert.assertFalse( "Sample value register has been unregistered once again.",
                isSameUnRegisterSuccess );
    }

    @Test
    public void resolve_test()
    {
        final DynamicValueResolver resolver = new DynamicValueResolver();
        final SampleValueResolver valueResolver = new SampleValueResolver();
        resolver.register( valueResolver );

        final String resolverName = new SampleValueResolver().getResolverName();
        final String resolvedText = resolver.resolve( "${" + resolverName + "}" );
        Assert.assertEquals( "Resolved value is incorrect",
                SampleValueResolver.SAMPLE_VALUE, resolvedText );
    }

    // TODO: Create unit tests for standard resolvers: UUID, DateTime, FormatString and etc.
}