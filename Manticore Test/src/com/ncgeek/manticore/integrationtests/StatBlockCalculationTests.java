package com.ncgeek.manticore.integrationtests;


import static org.junit.Assert.*;

import com.ncgeek.manticore.character.PlayerCharacter;
import com.ncgeek.manticore.character.stats.Addition;
import com.ncgeek.manticore.character.stats.Stat;

public class StatBlockCalculationTests extends BaseIntegrationTest {

	@Override
	protected void test(PlayerCharacter pc) {
		java.util.HashMap<String,Integer> map = new java.util.HashMap<String, Integer>();
		
		for(Stat s : pc.getStats().getStats()) {
			int abs = s.getAbsoluteValue();
			int calc = s.getCalculatedValue();
			String name = s.getAliases().get(0);
			
			if(map.containsKey(name)) {
				map.put(name, map.get(name) + 1);
			} else {
				map.put(name, 1);
			}
			
			if(abs != calc) {
				System.out.printf("%s %d != %d\n", s.getAliases().get(0), abs, calc);
				for(Addition a : s.getAppliedAdditions()) {
					System.out.println(a);
				}
				System.out.println();
			}
			assertEquals(s.getAliases().get(0), abs, calc);
		}
	}

	@Override
	protected void setupCharacterValues() {
	}
}
