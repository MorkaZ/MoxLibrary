package com.morkaz.moxlibrary.scheduler;

import com.morkaz.moxlibrary.database.sql.SQLDatabase;
import com.morkaz.moxlibrary.scheduler.interfaces.QueryScheduler;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SimpleSQLScheduler implements QueryScheduler {

    private Plugin main;
    private List<String> queryList = new ArrayList<>();
    private Boolean processing = false, sync;
    private BukkitTask schedulerTask;
    private SQLDatabase sqlDatabase;

    public SimpleSQLScheduler(Plugin main, SQLDatabase sqlDatabase, Boolean sync){
        this.main = main;
        this.sqlDatabase = sqlDatabase;
        this.sync = sync;
    }

    @Override
    public BukkitTask startScheduler() {
        this.schedulerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!processing) {
                    processing = true;
                    List<String> copiedQueryList = (List<String>)((ArrayList<String>)queryList).clone();
                    queryList.clear();
                    for (String query : copiedQueryList) {
                        if (sync){
                            sqlDatabase.updateSync(query);
                        } else {
                            sqlDatabase.updateAsync(query);
                        }
                    }
                    processing = false;
                }
            }
        }.runTaskTimerAsynchronously(main, 0L, 1L);
        return schedulerTask;
    }

    @Nullable
    @Override
    public BukkitTask getSchedulerTask() {
        return schedulerTask;
    }

    @Override
    public void cancelSchedulerTask() {
        this.schedulerTask.cancel();
    }

    @Override
    public void reloadScheduler() {
        schedulerTask.cancel();
        queryList.clear();
        processing = false;
        startScheduler();
    }

    @Override
    public void scheduleQuery(String query) {
        queryList.add(query);
    }

    @Override
    public void unscheduleQuery(String query) {
        queryList.remove(query);
    }
}
