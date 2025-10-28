package team.mjk.agent.global.config;

import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

  @NotNull
  @Override
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10); //최소 스레드 수
    executor.setMaxPoolSize(30); // 최대 스레드 수
    executor.setQueueCapacity(200); // 요청 대기 큐 -> 외부 API 지연 시 요청 버퍼링 용도
    executor.setThreadNamePrefix("async-reservation-"); //다버깅 및 모니터링 시 구분용
    executor.initialize();
    return executor;
  }

}