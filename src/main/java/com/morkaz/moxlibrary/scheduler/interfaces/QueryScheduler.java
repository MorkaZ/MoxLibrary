package com.morkaz.moxlibrary.scheduler.interfaces;

import com.morkaz.moxlibrary.database.sql.Query;
import org.bukkit.scheduler.BukkitTask;

public interface QueryScheduler {

    BukkitTask startScheduler();

    BukkitTask getSchedulerTask();

    void cancelSchedulerTask();

    void reloadScheduler();

    void scheduleQuery(Query query);

    void unscheduleQuery(Query query);

}
