package com.ncgeek.android.manticore.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public class CharacterModel implements Parcelable {
	
	public static final Parcelable.Creator<CharacterModel> CREATOR = new Parcelable.Creator<CharacterModel>() {
		@Override
		public CharacterModel createFromParcel(Parcel source) {
			CharacterModel cm = new CharacterModel();
			cm.id = source.readInt();
			cm.name = source.readString();
			cm.race = source.readString();
			cm.heroicClass = source.readString();
			cm.paragonClass = source.readString();
			cm.epicClass = source.readString();
			cm.level = source.readInt();
			cm.portrait = source.readParcelable(null);
			cm.importedOn = new Time();
			cm.importedOn.parse(source.readString());
			String update = source.readString();
			if(update != null) {
				cm.updatedOn = new Time();
				cm.updatedOn.parse(update);
			}
			return cm;
		}

		@Override
		public CharacterModel[] newArray(int size) {
			return new CharacterModel[size];
		}
	};
	
	private int id;
	private String name;
	private String race;
	private String heroicClass;
	private String paragonClass;
	private String epicClass;
	private int level;
	private Bitmap portrait;
	private Time importedOn;
	private Time updatedOn;
	
	public CharacterModel() {
	}

	public final int getId() {
		return id;
	}

	public final void setId(int id) {
		this.id = id;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getRace() {
		return race;
	}

	public final void setRace(String race) {
		this.race = race;
	}

	public final String getHeroicClass() {
		return heroicClass;
	}

	public final void setHeroicClass(String heroicClass) {
		this.heroicClass = heroicClass;
	}

	public final String getParagonClass() {
		return paragonClass;
	}

	public final void setParagonClass(String paragonClass) {
		this.paragonClass = paragonClass;
	}

	public final String getEpicClass() {
		return epicClass;
	}

	public final void setEpicClass(String epicClass) {
		this.epicClass = epicClass;
	}

	public final int getLevel() {
		return level;
	}

	public final void setLevel(int level) {
		this.level = level;
	}

	public final Bitmap getPortrait() {
		return portrait;
	}

	public final void setPortrait(Bitmap portrait) {
		this.portrait = portrait;
	}

	public final Time getImportedOn() {
		return importedOn;
	}

	public final void setImportedOn(Time importedOn) {
		this.importedOn = importedOn;
	}

	public final Time getUpdatedOn() {
		return updatedOn;
	}

	public final void setUpdatedOn(Time updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(race);
		dest.writeString(heroicClass);
		dest.writeString(paragonClass);
		dest.writeString(epicClass);
		dest.writeInt(level);
		dest.writeParcelable(portrait, flags);
		dest.writeString(importedOn.format2445());
		dest.writeString(updatedOn == null ? null : updatedOn.format2445());
	}
}
