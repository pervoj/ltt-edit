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

import java.util.ArrayList;

/**
 *
 * @author Vojtěch Perník <v.pernik@centrum.cz>
 */
public class TranslationController {
    private String lang;
    private String code;
    private String author;
    private String pattern;
    private ArrayList<TranslationModel> translations;
    private boolean saved;
    
    public TranslationController(String lang, String code, String author, String pattern) {
        translations = new ArrayList<>();
        
        this.lang = lang;
        this.code = code;
        this.author = author;
        this.pattern = pattern;
        this.saved = true;
    }
    
    public void add(TranslationModel translation) {
        translations.add(translation);
    }
    
    public TranslationModel getOne(int index) {
        return translations.get(index);
    }
    
    public void changeOne(int index, TranslationModel translation) {
        translations.set(index, translation);
        saved = false;
    }
    
    public void translateOne(int index, String translation) {
        TranslationModel element = getOne(index);
        element.setTranslation(translation);
        changeOne(index, element);
    }
    
    public Object[][] getModelArray() {
        Object[][] model = new Object[translations.size()][2];
        for (int i = 0; i < translations.size(); i++) {
            model[i][0] = translations.get(i).getOriginal();
            model[i][1] = translations.get(i).getTranslation();
        }
        
        return model;
    }
    
    public String getNotesString(int index) {
        String notes = "";
        for (String note : translations.get(index).getNotes()) {
            notes += note;
            notes += "\n";
        }
        return notes;
    }

    public String getLang() {
        return lang;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
        saved = false;
    }

    public String getPattern() {
        return pattern;
    }
    
    public boolean isSaved() {
        return saved;
    }
    
    public void setSaved(boolean saved) {
        this.saved = saved;
    }
    
    public ArrayList<TranslationModel> getAll() {
        return translations;
    }
}
