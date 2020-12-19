/*
 * Copyright (C) 2020 Vojtěch Perník <v.pernik@centrum.cz>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.pervoj.lttedit.classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 *
 * @author Vojtěch Perník <v.pernik@centrum.cz>
 */
public class PatternParser {
    public static PatternController getEmpty(String author) throws Exception {
        PatternController patterns = new PatternController(author);
        return patterns;
    }
    
    public static PatternController parseFile(File pattern) throws Exception {
        PatternController patterns;
        String author = "";
        ArrayList<String> elements = new ArrayList<>();
        ArrayList<String> notes = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pattern), StandardCharsets.UTF_8))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (s.startsWith("; Author:")) {
                    author = s.split(":")[0];
                } else if (s.startsWith(";.")) {
                    notes.add(s.substring(2, s.length()));
                } else if (!s.startsWith(";")) {
                    elements.add(s);
                }
            }
        }
        
        patterns = new PatternController(author);
        
        for (int i = 0; i < elements.size(); i++) {
            String[] element = elements.get(i).split("=");
            
            String singlecode = element[0];
            
            String singletext = "";
            if (element.length >= 2) {
                singletext = element[1];
            }
            
            ArrayList<String> singlenoteslist = new ArrayList<>();
            for (int j = 0; j < notes.size(); j++) {
                String[] singlenote = notes.get(j).split(":");
                if (singlenote[0].equals(singlecode)) {
                    if (singlenote.length >= 2) {
                        singlenoteslist.add(singlenote[1]);
                    }
                }
            }
            String[] singlenotes = new String[singlenoteslist.size()];
            for (int j = 0; j < singlenoteslist.size(); j++) {
                singlenotes[j] = singlenoteslist.get(j);
            }
            
            PatternModel patternelement = new PatternModel(singlecode, singletext, singlenotes);
            patterns.add(patternelement);
        }
        
        return patterns;
    }
    
    public static PatternController saveFile(PatternController patterns, File file) throws Exception  {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            if (!patterns.getAuthor().equals("")) {
                bw.write("; Author:" + patterns.getAuthor());
                bw.newLine();
            }
            
            for (int i = 0; i < patterns.getAll().size(); i++) {
                PatternModel pattern = patterns.getOne(i);
                String[] notes = pattern.getNotes();
                for (String note : notes) {
                    bw.write(";." + pattern.getCode() + ":" + note);
                    bw.newLine();
                }
                bw.write(pattern.getCode() + "=" + pattern.getText());
                bw.newLine();
            }
            
            bw.flush();
            
            patterns.setSaved(true);
        }
        
        return patterns;
    }
}
