/*
 * Copyright 2015 Demandware Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demandware.appsec.secure.manipulation.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.demandware.appsec.secure.manipulation.impl.DefaultManipulationType;
import com.demandware.appsec.secure.manipulation.impl.JSONManipulator;
import com.demandware.appsec.secure.manipulation.impl.ManipulatorFactory;

public class JSONManipulatorTest
{
    private final JSONManipulator json = (JSONManipulator) ManipulatorFactory
        .getManipulator( DefaultManipulationType.JSON_VALUE_MANIPULATOR );

    /**
     * Test that large Unicode characters are encoded properly
     */
    @Test
    public void testUnicode()
    {
        Character c = '\u2222';
        assertEquals( "\\u2222", this.json.getCorrectCharacter( c ) );
    }

    /**
     * Test that string escaping works correctly
     */
    @Test
    public void testEscape()
    {
        List<SimpleEntry<Character, String>> escapes =
            Arrays.asList( new SimpleEntry<Character, String>( '\b', "\\b" ), new SimpleEntry<Character, String>( '\t',
                "\\t" ), new SimpleEntry<Character, String>( '\n', "\\n" ), new SimpleEntry<Character, String>( '\f',
                "\\f" ), new SimpleEntry<Character, String>( '\r', "\\r" ), new SimpleEntry<Character, String>( '"',
                "\\\"" ), new SimpleEntry<Character, String>( '\\', "\\\\" ), new SimpleEntry<Character, String>( '/',
                "\\/" ) );

        for ( SimpleEntry<Character, String> escape : escapes )
        {
            Character orig = escape.getKey();
            String expect = escape.getValue();
            String actual = this.json.getCorrectCharacter( orig );
            assertEquals( expect, actual );
        }
    }

    /**
     * Total Sanity Test to make sure test code doesn't explode
     */
    @Test
    public void testNoExceptions()
    {
        try
        {
            for ( int i = 0; i < Character.MAX_CODE_POINT; i++ )
            {
                this.json.getCorrectCharacter( (char) i );
            }
        }
        catch ( Exception e )
        {
            fail( "Exception throw in testNoExceptions - " + e.getMessage() );
        }

    }

}
