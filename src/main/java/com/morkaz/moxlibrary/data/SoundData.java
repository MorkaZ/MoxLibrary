package com.morkaz.moxlibrary.data;

import org.bukkit.Sound;

public class SoundData {

	private Sound sound;
	private Float pitch = 1.0f, voulme = 1.0f;

	public SoundData(Sound sound){
		this.sound = sound;
	}

	public SoundData(Sound sound, Float pitch, Float voulme) {
		this.sound = sound;
		this.pitch = pitch;
		this.voulme = voulme;
	}

	public Sound getSound() {
		return sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public Float getPitch() {
		return pitch;
	}

	public void setPitch(Float pitch) {
		this.pitch = pitch;
	}

	public Float getVoulme() {
		return voulme;
	}

	public void setVoulme(Float voulme) {
		this.voulme = voulme;
	}
	
}
