package net.engineeringdigest.journalApp.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingInterceptor {

    @Around("@within(net.engineeringdigest.journalApp.annotations.LogExecution) || @annotation(net.engineeringdigest.journalApp.annotations.LogExecution)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        
        System.out.println("Execution time: " + (end - start) + "ms - " + joinPoint.getSignature());
        return result;
    }
}
