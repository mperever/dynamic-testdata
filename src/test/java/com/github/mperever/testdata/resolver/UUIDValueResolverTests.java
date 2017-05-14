package com.github.mperever.testdata.resolver;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

/**
 * Represents unit tests for {@link UUIDValueResolver} class.
 *
 * @author mperever
 *
 */
public class UUIDValueResolverTests
{
    private final UUIDValueResolver resolver = new UUIDValueResolver();

    @Test
    public void resolveWithoutNameNotSame_test()
    {
        final String source = ValueResolver.START_DYNAMIC_VALUE
                + this.resolver.getResolverName()
                + ValueResolver.END_DYNAMIC_VALUE;

        final String firstResult = this.resolver.resolve( source );

        boolean isUUID = isUUID( firstResult );
        Assert.assertTrue( "The result string is not UUID", isUUID );

        final String secondResult = this.resolver.resolve( source );

        Assert.assertNotEquals( "UUIDs are the same.", firstResult, secondResult );
    }

    @Test
    public void resolveWithNameSame_test()
    {
        final String source = ValueResolver.START_DYNAMIC_VALUE
                + resolver.getResolverName()
                + ":[test]"
                + ValueResolver.END_DYNAMIC_VALUE;

        final String firstResult = this.resolver.resolve( source );

        boolean isUUID = isUUID( firstResult );
        Assert.assertTrue( "The result string is not UUID", isUUID );

        final String secondResult = this.resolver.resolve( source );

        Assert.assertEquals( "UUIDs are not the same.", firstResult, secondResult );
    }

    @Test
    public void resolveWithNameTheNotSame_test()
    {
        final String sourceFirst = ValueResolver.START_DYNAMIC_VALUE
                + resolver.getResolverName()
                + ":[test1]"
                + ValueResolver.END_DYNAMIC_VALUE;

        final String sourceSecond = ValueResolver.START_DYNAMIC_VALUE
                + resolver.getResolverName()
                + ":[test2]"
                + ValueResolver.END_DYNAMIC_VALUE;

        final String firstResult = this.resolver.resolve( sourceFirst );
        final String secondResult = this.resolver.resolve( sourceSecond );

        Assert.assertNotEquals( "UUIDs are the same.", firstResult, secondResult );
    }

    private static boolean isUUID( final String uuid )
    {
        try
        {
            UUID.fromString( uuid );
            return true;
        } catch ( IllegalArgumentException ex )
        {
            return false;
        }
    }
}