package com.github.mperever.testdata;

import com.github.mperever.testdata.resolver.ValueResolver;

/**
 * Sample value resolver for {@link DynamicValueResolver} unit testing.
 *
 * @author mperever
 */
class SampleValueResolver extends ValueResolver
{
    static final String SAMPLE_VALUE = "_";

    @Override
    public String getResolverName()
    {
        return "test";
    }

    @Override
    public String resolve( String source )
    {
        return source.replace( START_DYNAMIC_VALUE
                + this.getResolverName()
                + END_DYNAMIC_VALUE, SAMPLE_VALUE );
    }
}