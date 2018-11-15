package com.morkaz.moxlibrary.scheduler;

import com.morkaz.moxlibrary.scheduler.interfaces.RunnableScheduler;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class SimpleRunnableScheduler implements RunnableScheduler {

	private Plugin plugin;
	private ArrayList<Runnable> runnableList = new ArrayList<Runnable>();
	private Boolean processing = false;
	private BukkitTask schedulerTask;
	private boolean sync;
	private long delay = 0L, period = 1L;

	public SimpleRunnableScheduler(Plugin plugin, Boolean sync) {
		this.plugin = plugin;
		this.sync = sync;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public void cancelSchedulerTask() {
		this.schedulerTask.cancel();
	}
	
	public BukkitTask getSchedulerTask() {
		return this.schedulerTask;
	}
	
	public void reloadScheduler() {
		schedulerTask.cancel();
		runnableList.clear();
		processing = false;
		startScheduler();
	}

	public void scheduleRunnable(Runnable runnable) {
		if (processing == false) {
			runnableList.add(runnable);
		} else {
			plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					scheduleRunnable(runnable);
				}
			}, 1L);
		}
	}

	@Override
	public void unscheduleRunnable(Runnable runnable) {
		runnableList.remove(runnable);
	}

	public BukkitTask startScheduler() {
		BukkitRunnable bukkitRunnable = new BukkitRunnable() {
			@Override
			public void run() {
				if (!processing) {
					processing = true;
					ArrayList<Runnable> copiedRunnableList = (ArrayList<Runnable>)runnableList.clone();
					runnableList.clear();
					for (Runnable runnable : copiedRunnableList) {
						runnable.run();
					}
					processing = false;
				}
			}
		};
		if (!this.sync){
			this.schedulerTask = bukkitRunnable.runTaskTimerAsynchronously(plugin, delay, period);
		} else {
			this.schedulerTask = bukkitRunnable.runTaskTimer(plugin, delay, period);
		}
		return schedulerTask;
	}
}
