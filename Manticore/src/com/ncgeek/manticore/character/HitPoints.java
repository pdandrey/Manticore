package com.ncgeek.manticore.character;

import java.io.Serializable;

public class HitPoints implements Serializable, IRest {

	private static final long serialVersionUID = 1L;

	private int _max;
	private int _current;
	private int _temp;
	
	private int _maxSurges;
	private int _remainingSurges;
	
	private int _deathSaves;
	
	public HitPoints() {
		_max = 0;
		_current = 0;
		_temp = 0;
		_maxSurges = 0;
		_remainingSurges = 0;
		_deathSaves = 0;
	}
	
	public int getMax() { return _max; }
	public void setMax(int max) {
		if(max <= 0)
			throw new IllegalArgumentException("Max HP cannot be <= 0");
		_max = _current = max;
	}
	
	public int getCurrent() { return _current; }
	public void setCurrent(int current) { 
		if(current > _max)
			throw new IllegalArgumentException("Cannot set HP over max");
		_current = current;
	}
	
	public int getTemp() { return _temp; }
	public void setTemp(int temp) {
		if(temp < 0)
			throw new IllegalArgumentException("Temp HP cannot be below 0");
		
		_temp = Math.max(temp, _temp);
	}
	
	public int getTotalSurges() { return _maxSurges; }
	public void setTotalSurges(int total) { 
		if(total <= 0)
			throw new IllegalArgumentException("Total Surges cannot be <= 0");
		_maxSurges = _remainingSurges = total;
	}
	
	public int getDeathSaves() { return _deathSaves; }
	
	public void failDeathSave() { 
		_deathSaves = Math.min(3, _deathSaves + 1);
	}
	
	public boolean isDead() { return _deathSaves >= 3; }
	
	public boolean isBleedingOut() { return _current < 0; }
	
	public int getRemainingSurges() { return _remainingSurges; }
	
	public int getBloodiedValue() { return _max / 2; }
	
	public boolean isBloodied() { return _current <= getBloodiedValue(); }
	
	public int getSurgeValue() { return _max / 4; }
	
	public void fullRest() {
		_deathSaves = 0;
		_temp = 0;
		_current = _max;
		_remainingSurges = _maxSurges;
	}
	
	public void shortRest() {
		_deathSaves = 0;
		_temp = 0;
	}
	
	public void takeDamage(int damage) {
		if(damage < 0)
			throw new IllegalArgumentException("damage must be > 0");
		
		if(_temp >= damage) {
			_temp -= damage;
		} else {
			_current += (_temp - damage);
			_temp = 0;
		}
	}
	
	public void heal(int healing) {
		if(healing < 0)
			throw new IllegalArgumentException("Healing must be > 0");
		
		if(_current < 0) {
			_current = healing;
			_deathSaves = 0;
		} else {
			_current = Math.min(_max, _current + healing);
		}
	}
	
	public void useSurge(int extraHealing) {
		if(_remainingSurges > 0) {
			heal(getSurgeValue() + extraHealing);
			expendSurge();
		}
	}
	
	public void expendSurge() {
		if(_remainingSurges > 0)
			--_remainingSurges;
	}

	@Override
	public void milestone() {
		// Nothing to do for milestone
	}
}
