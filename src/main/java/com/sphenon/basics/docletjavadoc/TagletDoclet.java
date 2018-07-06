package com.sphenon.basics.docletjavadoc;

/****************************************************************************
  Copyright 2001-2018 Sphenon GmbH

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

// Info: http://java.sun.com/j2se/1.5.0/docs/guide/javadoc/taglet/overview.html

/********************************************************************************/
/* [ToDo] Consolidate Redundancies!                                             */
/* TagletDoclet.java, TagleMeta.java, Doclet.java, doclet.dtd, doclet.docl      */
/********************************************************************************/

public class TagletDoclet implements Taglet {
    
    public TagletDoclet() {
    }

    public String getName() {
        return "doclet";
    }
    
    public boolean inField()       { return true; }
    public boolean inConstructor() { return true; }
    public boolean inMethod()      { return true; }
    public boolean inOverview()    { return true; }
    public boolean inPackage()     { return true; }
    public boolean inType()        { return true; }
    public boolean isInlineTag()   { return false; }
    
    static protected void addTaglet(Map tagletMap, TagletDoclet taglet) {
       Taglet t = (Taglet) tagletMap.get(taglet.getName());
       if (t != null) {
           tagletMap.remove(taglet.getName());
       }
       tagletMap.put(taglet.getName(), taglet);
    }

    public static void register(Map tagletMap) {
        addTaglet(tagletMap, new TagletDoclet());
    }

    public Hashtable getMetaData(Tag tag) {
        Hashtable result = new Hashtable();
        for (Tag it : tag.inlineTags()) {
            if (it.name().matches("@(?i:Category|SecurityClass|Language|Audience|Maturity|Intent|Extent|Coverage|Form|Encoding|Aspect)")) {
                result.put(it.name().toLowerCase().substring(1), it.text());
            }
        }
        return result;
    }
    public String formatMeta(Hashtable meta, String name) {
        if (meta.get(name.toLowerCase()) == null) {
            return "";
        } else {
            return "<tr><td><i>" + name + "</i></td><td><i>:&nbsp;" + ((String)meta.get(name.toLowerCase())) + "</i></td></tr>";
        }
    }

    public String toString(Tag tag) {
        Hashtable meta = getMetaData(tag);
        String name = (String) meta.get("category");
        if (name == null) { name = "Reference"; }
        String result = "<dt><b>" + name + ":</b><dd>"
                      + "<table cellpadding=2 cellspacing=0>";

        result += formatMeta(meta, "SecurityClass");
        result += formatMeta(meta, "Language");
        result += formatMeta(meta, "Audience");
        result += formatMeta(meta, "Maturity");
        result += formatMeta(meta, "Intent");
        result += formatMeta(meta, "Extent");
        result += formatMeta(meta, "Coverage");
        result += formatMeta(meta, "Form");
        result += formatMeta(meta, "Encoding");
        result += formatMeta(meta, "Aspect");
        result += formatMeta(meta, "EntitySecurityClass");
        result += formatMeta(meta, "EntityAudience");

        result += "<tr><td colspan=\"3\">";

        for (Tag it : tag.inlineTags()) {
            result += it.text();
        }

        result += "</td></tr></table></dd>\n";
        return result;
    }

    public String toString(Tag[] tags) {
        if (tags.length == 0) {
            return null;
        }
        String result = "";
        for (Tag tag : tags) { 
            result += toString(tag);
            // for (Tag it : tags[i].inlineTags()) {
            //     result += "[" + it.text() + "-" + it.toString() + "-" + it.name() + "-" + it.kind() + "]";
            // }
        }
        return result;
    }
}
