package com.ncgeek.manticore.character;

import java.io.Serializable;
import java.util.Observable;

public class HitPoints extends Observable implements Serializable, IRest {

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
	
	public void setCurrent(int hp, int tempHP, int surges, int deathSaves) {
		
		if(hp != _current) {
			_current = hp;
			setChanged();
		}
		
		if(tempHP != _temp) {
			_temp = tempHP;
			setChanged();
		}
		
		if(surges != _remainingSurges) {
			_remainingSurges = surges;
			setChanged();
		}
		
		if(deathSaves != _deathSaves) {
			_deathSaves = deathSaves;
			setChanged();
		}
		
		if(hasChanged()) {
			notifyObservers();
		}
	}
	
	public int getMax() { return _max; }
	public void setMax(int max) {
		if(max <= 0)
			throw new IllegalArgumentException("Max HP cannot be <= 0");
		_max = _current = max;
		setChanged();
		notifyObservers();
	}
	
	public int getCurrent() { return _current; }
	public void setCurrent(int current) { 
		if(current > _max)
			throw new IllegalArgumentException("Cannot set HP over max");
		_current = current;
		setChanged();
		notifyObservers();
	}
	
	public int getTemp() { return _temp; }
	public void setTemp(int temp) {
		if(temp < 0)
			throw new IllegalArgumentException("Temp HP cannot be below 0");
		
		if(temp > _temp) {
			_temp = temp;
			setChanged();
			notifyObservers();
		}
	}
	
	public int getTotalSurges() { return _maxSurges; }
	public void setTotalSurges(int total) { 
		if(total <= 0)
			throw new IllegalArgumentException("Total Surges cannot be <= 0");
		_maxSurges = _remainingSurges = total;
		setChanged();
		notifyObservers();
	}
	
	public int getDeathSaves() { return _deathSaves; }
	public void setDeathSaves(int deaths) { 
		_deathSaves = deaths;
		setChanged();
		notifyObservers();
	}
	public void failDeathSave() { 
		if(_deathSaves < 3) {
			++_deathSaves;
			setChanged();
			notifyObservers();
		}
	}
	
	public boolean isDead() { return _deathSaves >= 3; }
	
	public boolean isBleedingOut() { return _current < 0; }
	
	public int getRemainingSurges() { return _remainingSurges; }
	public void setRemainingSurges(int surges) { _remainingSurges = surges; }
	
	public int getBloodiedValue() { return _max / 2; }
	
	public boolean isBloodied() { return _current <= getBloodiedValue(); }
	
	public int getSurgeValue() { return _max / 4; }
	
	public void fullRest() {
		_deathSaves = 0;
		_temp = 0;
		_current = _max;
		_remainingSurges = _maxSurges;
		setChanged();
		notifyObservers();
	}
	
	public void shortRest() {
		_deathSaves = 0;
		_temp = 0;
		setChanged();
		notifyObservers();
	}
	
	public void takeDamage(int damage) {
		if(damage <= 0)
			throw new IllegalArgumentException("damage must be > 0");
		
		if(_temp >= damage) {
			_temp -= damage;
		} else {
			_current += (_temp - damage);
			_temp = 0;
		}
		setChanged();
		notifyObservers();
	}
	
	public void heal(int healing) {
		if(healing <= 0)
			throw new IllegalArgumentException("Healing must be > 0");
		
		if(_current < 0) {
			_current = healing;
			_deathSaves = 0;
		} else {
			_current = Math.min(_max, _current + healing);
		}
		
		setChanged();
		notifyObservers();
	}
	
	public void useSurge(int extraHealing) {
		if(_remainingSurges > 0) {
			heal(getSurgeValue() + extraHealing);
			expendSurge();
			setChanged();
			notifyObservers();
		}
	}
	
	public void expendSurge() {
		if(_remainingSurges > 0) {
			--_remainingSurges;
			setChanged();
			notifyObservers();
		}
	}

	@Override
	public void milestone() {
		// Nothing to do for milestone
	}
	
	@Override
	public String toString() {
		return String.format("HP: %d+%d/%d, Surges: %d/%d, Death Saves: %d", _current, _temp, _max, _remainingSurges, _maxSurges, _deathSaves);
	}
}
