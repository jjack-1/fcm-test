package com.example.fcmspring._core.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;

@Configuration
@EnableAsync // Spring에서 비동기 기능을 활성화하는 어노테이션입니다.
public class AsyncConfig implements AsyncConfigurer {

    // 비동기 메소드에서 발생한, 잡히지 않은 예외를 처리할 핸들러를 반환합니다.
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }

    /**
     * @Async 예외를 처리하기 위한 커스텀 핸들러 클래스입니다.
     */
    public static class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            // 예외가 발생했을 때 이 메소드가 호출됩니다.
            System.err.println("==== Async Exception Details ====");
            System.err.println("Exception message - " + ex.getMessage());
            System.err.println("Method name - " + method.getName());
            for (Object param : params) {
                System.err.println("Parameter value - " + param);
            }
            System.err.println("===============================");
            // 여기에 실제 운영 환경에서는 Log4j, Logback 등을 사용하여 에러 로그를 파일이나 외부 시스템에 기록하는 로직을 추가해야 합니다.
        }
    }
}
