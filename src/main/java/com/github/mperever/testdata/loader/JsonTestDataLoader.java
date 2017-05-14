package com.github.mperever.testdata.loader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

/**
 * Represents base class to load test data entry from json.
 *
 * @author mperever
 *
 */
public abstract class JsonTestDataLoader<T>
{
    private JsonObject jsonDocument;
    private boolean isJsonDocumentLoaded;

    public boolean isJsonLoaded()
    {
        return this.isJsonDocumentLoaded;
    }

    /**
     * Load and parse json from the string.
     *
     * @param jsonContent The value of json to be loaded.
     */
    public void loadJson( final String jsonContent )
    {
        if ( jsonContent == null || jsonContent.isEmpty() )
        {
            throw new IllegalArgumentException( "The parameter 'jsonContent' is not specified" );
        }
        try ( final StringReader stringReader = new StringReader( jsonContent );
              final JsonReader jsonReader = Json.createReader( stringReader ) )
        {
            this.jsonDocument = jsonReader.readObject();
        }
        this.isJsonDocumentLoaded = true;
    }

    /**
     *  Gets test data from the loaded json document for specified test name.
     *
     * @param testName The value of test name
     * @return List of test data entry
     */
    public List<T> getTestData( final String testName )
    {
        if ( this.jsonDocument == null )
        {
            throw new IllegalStateException(
                    "Json has not been loaded. Please use method loadJson to load json document." );
        }
        // Load test data for test from loaded json document
        final List<T> testDataList = new ArrayList<>();
        final JsonArray testDataJson = this.jsonDocument.getJsonArray( testName );
        for ( JsonValue testDataEntryObject : testDataJson )
        {
            if ( !( testDataEntryObject instanceof JsonObject ) )
            {
                continue;
            }
            final T testData = readTestData( ( JsonObject ) testDataEntryObject );
            if ( testData != null )
            {
                testDataList.add( testData );
            }
        }
        return testDataList;
    }

    /**
     * Read and convert test data from json object to test data entry.
     *
     * @param testDataObject The value of json test data object to be converted
     * @return The test data entry.
     */
    protected abstract T readTestData( final JsonObject testDataObject );
}