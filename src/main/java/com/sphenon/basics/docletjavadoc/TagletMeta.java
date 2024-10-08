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

// Info: http://java.sun.com/j2se/1.5.0/docs/guide/javadoc/taglet/overview.html

public class TagletMeta implements Taglet {
    
    public TagletMeta(String name) {
        this.name = name;
    }

    protected String name;

    public String getName() {
        return this.name;
    }
    
    public boolean inField()       { return true; }
    public boolean inConstructor() { return true; }
    public boolean inMethod()      { return true; }
    public boolean inOverview()    { return true; }
    public boolean inPackage()     { return true; }
    public boolean inType()        { return true; }
    public boolean isInlineTag()   { return true; }
    
    static protected void addTaglet(Map tagletMap, TagletMeta taglet) {
       Taglet t = (Taglet) tagletMap.get(taglet.getName());
       if (t != null) {
           tagletMap.remove(taglet.getName());
       }
       tagletMap.put(taglet.getName(), taglet);
    }

    public static void register(Map tagletMap) {
    }

    public String toString(Tag tag) {
        return "<font color=\"#800000\"><b>" + this.name + ":</b> " + tag.text() + "</font>";
    }
    
    public String toString(Tag[] tags) {
        return null;
    }
}
