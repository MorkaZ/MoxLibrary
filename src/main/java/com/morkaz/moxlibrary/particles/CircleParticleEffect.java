package com.morkaz.moxlibrary.particles;

import com.morkaz.moxlibrary.data.ParticleData;

public class CircleParticleEffect extends ParticleData {

	private Integer radius = 1;

	public CircleParticleEffect(ParticleData particleData, Integer radius){
		super(particleData);
		this.radius = radius;
	}

	public CircleParticleEffect(ParticleData particleData){
		super(particleData);
	}



}
