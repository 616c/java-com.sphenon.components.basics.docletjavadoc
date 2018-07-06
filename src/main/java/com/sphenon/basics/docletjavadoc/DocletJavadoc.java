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

import com.sphenon.basics.context.*;
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.debug.*;
import com.sphenon.basics.message.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.engines.generator.*;
import com.sphenon.engines.generator.classes.*;

import com.sun.javadoc.*;

import java.io.File;

// Info: http://java.sun.com/j2se/1.5.0/docs/guide/javadoc/taglet/overview.html

/**
   A Javadoc Doclet which produces Sphenon Doclets as output.

   Sphenon Doclets are snippets of DocBook Code combined with
   semantically classifying meta data.
  
   @doclet {@Category QuickReference} {@Audience AnonymousAlcoholics} {@SecurityClass classified}
  
   This is the superquick reference.
 */
public class DocletJavadoc {

    /**
       This method starts it all.

       @param root The root of the documented syntax tree
       @return true for success, false otherwise
    */
    public static boolean start(RootDoc root) {
        CallContext context = RootContext.getRootContext();

        if (Configuration.isInitialised(context) == false) {
            String configurationname    = readOption(root.options(), "--configuration-name");
            if (configurationname != null && configurationname.length() != 0) {
                RootConfiguration.setConfigurationName(configurationname);
            }

            String configurationvariant = readOption(root.options(), "--configuration-variant");
            if (configurationvariant != null && configurationvariant.length() != 0) {
                RootConfiguration.setExplicitConfigurationVariant(configurationvariant);
            }

            String properties = readOption(root.options(), "--properties");
            if (properties != null && properties.length() != 0) {
                for (String property : properties.split(",")) {
                    RootConfiguration.setPropertyOverride(property);
                }
            }
        }

        if (readOption(root.options(), "--debugger-wait") != null) {
            System.err.println("Press RETURN to continue...");
            try {
                String s = (new java.io.BufferedReader(new java.io.InputStreamReader(System.in))).readLine();
            } catch (java.io.IOException ioe) {
            }
        }

        Configuration.initialise(context);
        DocletJavadocPackageInitialiser.initialise(context);

        String folder = readOption(root.options(), "-d");
        File fo = new File(folder);
        fo.mkdirs();
        File fi = new File(fo, "classes.list");

        GeneratorOutputToFile gotf = new GeneratorOutputToFile(context, fi.getAbsolutePath());

        try {
            GeneratorRegistry.get(context).getGenerator(context, "com.sphenon.basics.docletjavadoc.templates.Doclet_Doc").generate(context, gotf, root);
        } catch (Throwable t) {
            Dumper.dump(context, "Exception", t);
            System.err.println("DocletJavadoc failed");
            return false;
        }

        // ClassDoc[] classes()
        // PackageDoc packageNamed(String name)

        return true;
    }

    // writeContents(root.classes(), tagName);
    //
    // private static void writeContents(ClassDoc[] classes, String tagName) {
    //     for (int i=0; i < classes.length; i++) {
    //         boolean classNamePrinted = false;
    //         MethodDoc[] methods = classes[i].methods();
    //         for (int j=0; j < methods.length; j++) {
    //             Tag[] tags = methods[j].tags(tagName);
    //             if (tags.length > 0) {
    //                 if (!classNamePrinted) {
    //                     System.out.println("\n" + classes[i].name() + "\n");
    //                     classNamePrinted = true;
    //                 }
    //                 System.out.println(methods[j].name());
    //                 for (int k=0; k < tags.length; k++) {
    //                     System.out.println("   " + tags[k].name() + ": " + tags[k].text());
    //                 }
    //             } 
    //         }
    //     }
    // }

    private static String readOption(String[][] options, String name) {
        String tagName = null;
        for (String[] opt : options) {
            if (opt[0].equals(name)) { return name.equals("--debugger-wait") ? "" : opt[1]; }
        }
        return null;
    }

    public static int optionLength(String option) {
        if (option.equals("-d")) {
            return 2;
        } else if (option.equals("--configuration-name")) {
            return 2;
        } else if (option.equals("--configuration-variant")) {
            return 2;
        } else if (option.equals("--properties")) {
            return 2;
        } else if (option.equals("--debugger-wait")) {
            return 1;
        }
        return 0;
    }

    public static boolean validOptions(String options[][], DocErrorReporter reporter) {
	boolean foundTagOption = false;
        for (String[] opt : options) {
            if (opt[0].equals("-d")) {
                if (foundTagOption) {
                    reporter.printError("Only one -d option allowed.");
                    return false;
                } else { 
                    foundTagOption = true;
                }
            } 
        }
        if (!foundTagOption) {
            reporter.printError("Usage: javadoc -d folder ...");
        }
        return foundTagOption;
    }
}
