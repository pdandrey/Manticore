package com.ncgeek.android.manticore.adapters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ncgeek.manticore.util.Logger;

public final class CharacterFileInfo {
	private String _name;
	private int _level;
	private File _file;
	private boolean _isValid;
	private String _race;
	private String _class;
	
	public static final Comparator<CharacterFileInfo> NAME_COMPARE = new Comparator<CharacterFileInfo>() {
		@Override
		public int compare(CharacterFileInfo cfi1, CharacterFileInfo cfi2) {
			return cfi1.getName().compareTo(cfi2.getName());
		}
	};
	
	public CharacterFileInfo(File file) {
		_name = null;
		_level = -1;
		_file = file;
		_isValid = false;
		_race = null;
		_class = null;
		
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF8");
			BufferedReader br = new BufferedReader(isr, 1024);
			String line;
			boolean d20Found = false;
			
			Pattern nameLine = Pattern.compile("\\<name\\>\\s*(.*?)\\s*\\</name\\>");
			Pattern levelLine = Pattern.compile("\\<Level\\>\\s*(.*?)\\s*\\</Level\\>");
			Pattern raceLine = Pattern.compile("\\<RulesElement name=\"(.*?)\" type=\"Race\"");
			Pattern classLine = Pattern.compile("\\<RulesElement name=\"(.*?)\" type=\"Class\"");
			
			while((line = br.readLine()) != null) {
				if(line.length() > 0 && (int)line.charAt(0) == 65279) {
					if(line.length() == 1)
						line = "";
					else
						line = line.substring(1);
				}
					
				line = line.trim();
				if(line.length() == 0)
					continue;
				
				if(!d20Found) {
					if(!line.startsWith("<D20Character")) {
						_isValid = false;
						break;
					} else {
						d20Found = true;
						continue;
					}
				}
				
				if(_name == null) {
					Matcher nameMatcher = nameLine.matcher(line);
					if(nameMatcher.find()) {
						_name = nameMatcher.group(1).trim();
						continue;
					}
				}
				
				if(_level == -1) {
					Matcher levelMatcher = levelLine.matcher(line);
					if(levelMatcher.find()) {
						_level = Integer.parseInt(levelMatcher.group(1).trim());
						continue;
					}
				}
				
				if(_race == null) {
					Matcher raceMatcher = raceLine.matcher(line);
					if(raceMatcher.find()) {
						_race = raceMatcher.group(1).trim();
						continue;
					}
				}
				
				if(_class == null) {
					Matcher classMatcher = classLine.matcher(line);
					if(classMatcher.find()) {
						_class = classMatcher.group(1).trim();
						continue;
					}
				}
				
				if(_name != null && _level != -1 && _race != null && _class != null) {
					_isValid = true;
					break;
				}
			}
			
			br.close();
			isr.close();
			fis.close();
		} catch(Exception ex) {
			Logger.error("CharacterFileInfo", "Error parsing " + file.getName(), ex);
		}
	}
	
	public String getName() { return _name; }
	public File getFile() { return _file; }
	public int getLevel() { return _level; }
	public boolean isValid() { return _isValid; }
	public String getRace() { return _race; }
	public String getCharacterClass() { return _class; }
}
