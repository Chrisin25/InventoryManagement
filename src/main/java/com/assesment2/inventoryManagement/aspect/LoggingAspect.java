package com.assesment2.inventoryManagement.aspect;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = Logger.getLogger(LoggingAspect.class);

    @Around("execution(* com.assesment2.inventoryManagement.repository.*+.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        logger.info("Method '" + joinPoint.getSignature().getName() + "' in class '" + joinPoint.getSignature().getDeclaringType().getSimpleName() + "' executed in " + stopWatch.getTotalTimeMillis() + " ms");
        return result;
    }
}