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
	private Object data = null;
	private Location location = null;
	// Particle | offset xyz | extra (speed) | data (np. kolor)


	// ------------------
	// CONSTRUCTORS
	// ------------------

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
		this.data = data;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
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

	public boolean spawnParticle(Player player){
		if (location != null){
			player.spawnParticle(this.particle, this.location, this.count, this.offsetX, this.offsetY, this.offsetZ, this.extra, this.data);
			return true;
		}
		return false;
	}


	public boolean spawnParticle(Collection<? extends Player> players){
		if (this.location != null){
			for (Player player : players){
				player.spawnParticle(this.particle, this.location, this.count, this.offsetX, this.offsetY, this.offsetZ, this.extra, this.data);
			}
			return true;
		}
		return false;
	}
}
