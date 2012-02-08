package com.ncgeek.android.manticore;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.ncgeek.manticore.character.PlayerCharacter;

public class ManticoreCharacter extends PlayerCharacter implements Parcelable, Serializable {

	private static final long serialVersionUID = 1L;

	private transient Bitmap _portrait;
	
	public Bitmap getPortrait() { return _portrait; }
	public void setPortrait(Bitmap portrait) { _portrait = portrait; }
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(this);
		dest.writeParcelable(_portrait, 0);
	}
	
	public static final Parcelable.Creator<ManticoreCharacter> CREATOR = new Parcelable.Creator<ManticoreCharacter>() {
		public ManticoreCharacter createFromParcel(Parcel in) {
		    ManticoreCharacter ret = (ManticoreCharacter)in.readSerializable();
		    ret._portrait = in.readParcelable(null);
		    return ret;
		}
		
		public ManticoreCharacter[] newArray(int size) {
		    return new ManticoreCharacter[size];
		}
	};

}
