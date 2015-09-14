package org.cpw.cache.local;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cpw.cache.util.DstCacheConfig;

/**
 * cache maintainer ClassName: Maintainer
 * 
 * @description
 * @author yin_changbao
 * @Date Sep 10, 2015
 *
 */
public class Maintainer {
	private static Log logger = LogFactory.getLog(Maintainer.class.getName());
	private LocalCacheClient localCacheClient;
	private ScheduledFuture<?> cleanerHandle = null;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	long initialDelayInSeconds = DstCacheConfig.getLongProperty("cleaner.delay.seconds",60);
	long periodInSeconds = DstCacheConfig.getLongProperty("cleaner.interval.seconds",300);

	Maintainer(LocalCacheClient localCacheClient) {
		this.localCacheClient = localCacheClient;
		this.init();
	}

	private void init() {
		if (cleanerHandle != null) {
			logger.info("Cancelling the current CachePoller task");
			cleanerHandle.cancel(false);
		}
		final Runnable maintainer = new Runnable() {
			public void run() {
				try {
					localCacheClient.maintainAllCache();
				} catch (Throwable e) {
					logger.error("Error encountered when cleaning cache: " + e.toString(), e);
				}
			}
		};
		logger.info("Scheduling a new Maintainer task with period:" + periodInSeconds);
		cleanerHandle = scheduler.scheduleAtFixedRate(maintainer, initialDelayInSeconds, periodInSeconds,
				TimeUnit.SECONDS);
	}

	long getPeriodInSeconds() {
		return periodInSeconds;
	}

	void setPeriodInSeconds(int periodInSeconds) {
		this.periodInSeconds = periodInSeconds;
		this.init();
	}
}
