package com.morkaz.moxlibrary.database.sql;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public abstract class SQLScheduler{

	private Plugin plugin;
	private ArrayList<Runnable> runnableList = new ArrayList<Runnable>();
	private Boolean processing = false;
	private BukkitTask schedulerTask;


	public SQLScheduler(Plugin plugin) {
		this.plugin = plugin;
		this.startScheduler();
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
					runnableList.add(runnable);
				}
			}, 1L);
		}
	}

	public BukkitTask startScheduler() {
		this.schedulerTask = new BukkitRunnable() {
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
		}.runTaskTimerAsynchronously(plugin, 0L, 5L);
		return schedulerTask;
	}

}
