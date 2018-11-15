package com.morkaz.moxlibrary.scheduler.interfaces;

import org.bukkit.scheduler.BukkitTask;


public interface RunnableScheduler {

	BukkitTask startScheduler();

	BukkitTask getSchedulerTask();

	void cancelSchedulerTask();

	void reloadScheduler();

	void scheduleRunnable(Runnable runnable);

	void unscheduleRunnable(Runnable runnable);

}
