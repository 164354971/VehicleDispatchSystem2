package cn.lingbaocrisps.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class TaskExecutorConfig {

    @Bean("threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数
        threadPoolTaskExecutor.setCorePoolSize(10);
        //最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(20);
        //队列最大长度
        threadPoolTaskExecutor.setQueueCapacity(50);
        //线程空闲时间
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        //线程前缀名
        threadPoolTaskExecutor.setThreadNamePrefix("threadPoolTaskExecutor-");
        //设置拒绝策略，这里使用CallerRunsPolicy，当线程池饱和时，主线程会自己执行被拒绝的任务
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Bean("threadPoolTaskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        //核心线程数
        threadPoolTaskScheduler.setPoolSize(10);
        //线程前缀名
        threadPoolTaskScheduler.setThreadNamePrefix("threadPoolTaskScheduler-");
        //设置拒绝策略，这里使用CallerRunsPolicy，当线程池饱和时，主线程会自己执行被拒绝的任务
        threadPoolTaskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }
}
