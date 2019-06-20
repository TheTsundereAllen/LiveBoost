package me.allen.liveboost;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import me.allen.liveboost.gui.LiveBoostGUI;
import me.allen.liveboost.proxy.Proxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter
public class LiveBoost {
    private LiveBoostGUI liveBoostGUI;

    private ScheduledExecutorService sharedExecutor;
    private ExecutorService heartbeatPool = Executors.newWorkStealingPool(1000);
    private Proxy proxy;

    public LiveBoost() {
        this.sharedExecutor = Executors.newSingleThreadScheduledExecutor();
        this.liveBoostGUI = new LiveBoostGUI(this);
        this.proxy = new Proxy(this);
    }
}
