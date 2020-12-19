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
public class TranslationParser {
    public static TranslationController parseFile(File original, File translation, String lang, String code, String author) throws Exception {
        TranslationController translations;
        ArrayList<String> elements = new ArrayList<>();
        ArrayList<String> comments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(original), StandardCharsets.UTF_8))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (s.startsWith(";.")) {
                    comments.add(s.substring(2, s.length()));
                } else if (!s.startsWith(";.") && !s.startsWith(";")) {
                    elements.add(s);
                }
            }
        }
        
        translations = new TranslationController(lang, code, author, original.getName());
        
        for (int i = 0; i < elements.size(); i++) {
            String[] element = elements.get(i).split("=");
            
            String singlecode = element[0];
            String singleoriginal = element[1];
            
            ArrayList<String> singlecommentslist = new ArrayList<>();
            for (int j = 0; j < comments.size(); j++) {
                String[] singlecomment = comments.get(j).split(":");
                if (singlecomment[0].equals(singlecode)) {
                    singlecommentslist.add(singlecomment[1]);
                }
            }
            String[] singlecomments = new String[singlecommentslist.size()];
            for (int j = 0; j < singlecommentslist.size(); j++) {
                singlecomments[j] = singlecommentslist.get(j);
            }
            
            TranslationModel translationelement = new TranslationModel(singlecode, singleoriginal, "", singlecomments);
            translations.add(translationelement);
        }
        
        return translations;
    }
    
    public static TranslationController parseFile(File translation) throws Exception {
        TranslationController translations;
        String path = translation.getParent();
        String lang = "";
        String code = "";
        String author = "";
        String pattern = "";
        String patternpath = "";
        ArrayList<String> elements = new ArrayList<>();
        ArrayList<String> comments = new ArrayList<>();
        ArrayList<String> pelements = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(translation), StandardCharsets.UTF_8))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (s.startsWith("; Language:")) {
                    lang = s.split(":")[1];
                } else if (s.startsWith("; Code:")) {
                    code = s.split(":")[1];
                } else if (s.startsWith("; Author:")) {
                    author = s.split(":")[1];
                } else if (s.startsWith("; Pattern:")) {
                    pattern = s.split(":")[1];
                    patternpath = path + File.separator + pattern;
                } else if (!s.startsWith(";") && !s.equals("")) {
                    elements.add(s);
                }
            }
        }
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(patternpath), StandardCharsets.UTF_8))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (s.startsWith(";.")){
                    comments.add(s.substring(2, s.length()));
                } else if (!s.startsWith(";") && !s.equals("")) {
                    pelements.add(s);
                }
            }
        }
        
        translations = new TranslationController(lang, code, author, pattern);

        for (int i = 0; i < pelements.size(); i++) {
            String[] element = pelements.get(i).split("=");
            
            String singlecode = element[0];
            
            String singlepattern = "";
            if (element.length >= 2) {
                singlepattern = element[1];
            }
            
            String singletranslation = "";
            for (int j = 0; j < elements.size(); j++) {
                String[] translationarray = elements.get(j).split("=");
                if (translationarray[0].equals(singlecode)) {
                    if (translationarray.length >= 2) {
                        singletranslation = translationarray[1];
                    }
                }
            }

            ArrayList<String> singlecommentslist = new ArrayList<>();
            for (int j = 0; j < comments.size(); j++) {
                String[] singlecomment = comments.get(j).split(":");
                if (singlecomment[0].equals(singlecode)) {
                    if (singlecomment.length >= 2) {
                        singlecommentslist.add(singlecomment[1]);
                    }
                }
            }
            String[] singlecomments = new String[singlecommentslist.size()];
            for (int j = 0; j < singlecommentslist.size(); j++) {
                singlecomments[j] = singlecommentslist.get(j);
            }

            TranslationModel translationelement = new TranslationModel(singlecode, singlepattern, singletranslation, singlecomments);
            translations.add(translationelement);
        }
        
        return translations;
    }
    
    public static TranslationController saveFile(TranslationController translations, File file) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            if (!translations.getLang().equals("")) {
                bw.write("; Language:" + translations.getLang());
                bw.newLine();
            }
            if (!translations.getCode().equals("")) {
                bw.write("; Code:" + translations.getCode());
                bw.newLine();
            }
            if (!translations.getAuthor().equals("")) {
                bw.write("; Author:" + translations.getAuthor());
                bw.newLine();
            }
            if (!translations.getPattern().equals("")) {
                bw.write("; Pattern:" + translations.getPattern());
                bw.newLine();
            }
            
            for (int i = 0; i < translations.getAll().size(); i++) {
                TranslationModel translation = translations.getOne(i);
                String[] notes = translation.getNotes();
                for (String note : notes) {
                    bw.write(";." + translation.getCode() + ":" + note);
                    bw.newLine();
                }
                bw.write(translation.getCode() + "=" + translation.getTranslation());
                bw.newLine();
            }
            
            bw.flush();
            
            translations.setSaved(true);
        }
        
        return translations;
    }
}
