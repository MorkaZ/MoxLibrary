package com.morkaz.moxlibrary.data;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ParticleData {

	// ------------------
	// FIELDS
	// ------------------

	private Particle particle = Particle.SPELL_MOB;
	private Double offsetX = 0d, offsetY = 0d, offsetZ = 0d;
	private Double extra = 0d;
	private Integer count = 1;
	private Object particleData = null;
	private Location location = null;
	// Particle | offset xyz | extra (speed) | particleData (np. kolor)


	// ------------------
	// CONSTRUCTORS
	// ------------------

	public ParticleData(ParticleData particleData){
		if (particleData == null){
			return;
		}
		this.particle = particleData.getParticle();
		this.location = particleData.getLocation();
		this.count = particleData.getCount();
		this.offsetX = particleData.getOffsetX();
		this.offsetY = particleData.getOffsetY();
		this.offsetZ = particleData.getOffsetZ();
		this.extra = particleData.getExtra();
		this.particleData = particleData.getParticleData();

	}

	public ParticleData(Particle particle, Location location, Integer count, Double offsetX, Double offsetY, Double offsetZ, Double extra, Object data) {
		this.particle = particle;
		this.location = location;
		if (count != null){
			this.count = count;
		}
		if (offsetX != null){
			this.offsetX = offsetX;
		}
		if (offsetY != null){
			this.offsetY = offsetY;
		}
		if (offsetX != null){
			this.offsetZ = offsetZ;
		}
		if (extra != null) {
			this.extra = extra;
		}
		this.particleData = data;
	}

	public ParticleData(Particle particle, Location location, Integer count, Double offsetX, Double offsetY, Double offsetZ, Double extra){
		this.particle = particle;
		this.location = location;
		if (count != null){
			this.count = count;
		}
		if (offsetX != null){
			this.offsetX = offsetX;
		}
		if (offsetY != null){
			this.offsetY = offsetY;
		}
		if (offsetX != null){
			this.offsetZ = offsetZ;
		}
		if (extra != null) {
			this.extra = extra;
		}
	}

	public ParticleData(Particle particle, Location location, Integer count, Double offsetX, Double offsetY, Double offsetZ){
		this.particle = particle;
		this.location = location;
		if (count != null){
			this.count = count;
		}
		if (offsetX != null){
			this.offsetX = offsetX;
		}
		if (offsetY != null){
			this.offsetY = offsetY;
		}
		if (offsetX != null){
			this.offsetZ = offsetZ;
		}
	}

	public ParticleData(Particle particle, Location location, Integer count){
		this.particle = particle;
		this.location = location;
		if (count != null){
			this.count = count;
		}
	}

	public ParticleData(Particle particle, Location location){
		this.particle = particle;
		this.location = location;
	}

	public ParticleData(Particle particle){
		this.particle = particle;
	}

	public ParticleData(){

	}


	// ------------------
	// GETTERS & SETTERS
	// ------------------


	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getOffsetX() {
		return offsetX;
	}

	public Particle getParticle() {
		return particle;
	}

	public void setParticle(Particle particle) {
		this.particle = particle;
	}

	public void setOffsetX(Double offsetX) {
		this.offsetX = offsetX;
	}

	public Double getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(Double offsetY) {
		this.offsetY = offsetY;
	}

	public Double getOffsetZ() {
		return offsetZ;
	}

	public void setOffsetZ(Double offsetZ) {
		this.offsetZ = offsetZ;
	}

	public Double getExtra() {
		return extra;
	}

	public void setExtra(Double extra) {
		this.extra = extra;
	}

	public Object getParticleData() {
		return particleData;
	}

	public void setParticleData(Object particleData) {
		this.particleData = particleData;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}


	// ------------------
	// METHODS
	// ------------------

	public boolean spawn(Player player, Location location){
		if (location != null){
			player.spawnParticle(this.particle, location, this.count, this.offsetX, this.offsetY, this.offsetZ, this.extra, this.particleData);
			return true;
		}
		return false;
	}

	public boolean spawn(Player player){
		if (location != null){
			player.spawnParticle(this.particle, this.location, this.count, this.offsetX, this.offsetY, this.offsetZ, this.extra, this.particleData);
			return true;
		}
		return false;
	}


	public boolean spawn(Collection<? extends Player> players){
		if (this.location != null){
			for (Player player : players){
				player.spawnParticle(this.particle, this.location, this.count, this.offsetX, this.offsetY, this.offsetZ, this.extra, this.particleData);
			}
			return true;
		}
		return false;
	}

	public boolean spawn(Collection<? extends Player> players, Location location){
		if (this.location != null){
			for (Player player : players){
				player.spawnParticle(this.particle, location, this.count, this.offsetX, this.offsetY, this.offsetZ, this.extra, this.particleData);
			}
			return true;
		}
		return false;
	}

}
