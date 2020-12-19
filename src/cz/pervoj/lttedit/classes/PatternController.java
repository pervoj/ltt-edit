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
public class PatternController {
    private String author;
    private ArrayList<PatternModel> patterns;
    private boolean saved;
    
    public PatternController(String author) {
        patterns = new ArrayList<>();
        
        this.author = author;
        this.saved = true;
    }
    
    public void add(PatternModel pattern) {
        patterns.add(pattern);
    }
    
    public PatternModel getOne(int index) {
        return patterns.get(index);
    }
    
    public void changeOne(int index, PatternModel pattern) {
        patterns.set(index, pattern);
        saved = false;
    }
    
    public void setCode(int index, String code) {
        PatternModel element = getOne(index);
        element.setCode(code);
        changeOne(index, element);
    }
    
    public void setText(int index, String text) {
        PatternModel element = getOne(index);
        element.setText(text);
        changeOne(index, element);
    }
    
    public void setNotes(int index, String[] notes) {
        PatternModel element = getOne(index);
        element.setNotes(notes);
        changeOne(index, element);
    }
    
    public Object[][] getModelArray() {
        Object[][] model = new Object[patterns.size()][2];
        for (int i = 0; i < patterns.size(); i++) {
            model[i][0] = patterns.get(i).getCode();
            model[i][1] = patterns.get(i).getText();
        }
        
        return model;
    }
    
    public String getNotesString(int index) {
        String notes = "";
        for (String note : patterns.get(index).getNotes()) {
            notes += note;
            notes += "\n";
        }
        return notes;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
        saved = false;
    }
    
    public boolean isSaved() {
        return saved;
    }
    
    public void setSaved(boolean saved) {
        this.saved = saved;
    }
    
    public ArrayList<PatternModel> getAll() {
        return patterns;
    }
}
