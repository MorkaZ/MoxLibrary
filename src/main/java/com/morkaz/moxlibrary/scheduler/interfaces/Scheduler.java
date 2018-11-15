package com.morkaz.moxlibrary.scheduler.interfaces;

import org.bukkit.scheduler.BukkitTask;


public interface Scheduler {

	void cancelSchedulerTask();

	BukkitTask getSchedulerTask();

	void reloadScheduler();

	BukkitTask startScheduler();

}
