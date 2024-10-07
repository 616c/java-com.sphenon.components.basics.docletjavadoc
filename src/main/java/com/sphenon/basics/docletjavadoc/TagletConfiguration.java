package com.sphenon.basics.docletjavadoc;

/****************************************************************************
  Copyright 2001-2024 Sphenon GmbH

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations
  under the License.
*****************************************************************************/

import com.sun.tools.doclets.Taglet;
import com.sun.javadoc.*;
import java.util.Map;
import java.util.Hashtable;

/**
   For documenting properties, options and similar influencing values

   Examples:

   @configuration JavaProperty:my.path.work   this example property would be required to be set to true,
                                              otherwise the software does not work
   @configuration JavaProperty:my.path.crash  this example property would be required to be set to false,
                                              otherwise the software would crash
*/
public class TagletConfiguration implements Taglet {
    
    public TagletConfiguration() {
    }

    public String getName() {
        return "configuration";
    }
    
    public boolean inField()       { return true; }
    public boolean inConstructor() { return true; }
    public boolean inMethod()      { return true; }
    public boolean inOverview()    { return true; }
    public boolean inPackage()     { return true; }
    public boolean inType()        { return true; }
    public boolean isInlineTag()   { return false; }
    
    static protected void addTaglet(Map tagletMap, TagletConfiguration taglet) {
       Taglet t = (Taglet) tagletMap.get(taglet.getName());
       if (t != null) {
           tagletMap.remove(taglet.getName());
       }
       tagletMap.put(taglet.getName(), taglet);
    }

    public static void register(Map tagletMap) {
        addTaglet(tagletMap, new TagletConfiguration());
    }

    public String toString(Tag tag) {
        return "<div><b>Configuration:</b>" + tag.text() + "</div>";
    }
    
    public String toString(Tag[] tags) {
        if (tags.length == 0) {
            return null;
        }
        String result = "<div><b>Configuration:</b>\n  <ul>\n";
        for (Tag tag : tags) { 
            result += "<li>" + tag.text() + "</li>\n";
        }
        result += "  </ul>\n</div>\n";
        return result;
    }
}
