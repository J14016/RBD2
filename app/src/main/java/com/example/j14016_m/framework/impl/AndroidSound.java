package com.example.j14016_m.framework.impl;

import android.media.SoundPool;

import com.example.j14016_m.framework.Sound;

public class AndroidSound implements Sound {
	private int soundId;
	private SoundPool soundPool;
	
	public AndroidSound(SoundPool soundPool, int soundId) {
		this.soundId = soundId;
		this.soundPool = soundPool;
	}
	
	@Override
	public void play(float volume) {
		soundPool.play(soundId, volume, volume, 0, 0, 1);
	}
	
	@Override
	public void dispose() {
		soundPool.unload(soundId);
	}
}
