package com.example.Sim.Utilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Aspect
@Component
public class LogExecutionTime {
    private static final String LOG_MESSAGE_FORMAT = "%s.%s execution time: %dms";
    private static final Log LOG =   LogFactory.getLog(LogExecutionTime.class);

   /* @Around("execution(* com.example.Sim.Services.*.*(..))")
    public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object retVal = joinPoint.proceed();
        stopWatch.stop();
        logExecutionTime(joinPoint, stopWatch);
        return retVal;
    }
    @Around("execution(* com.example.Sim.Utilities.*.*(..))")
    public Object logTimeMethod2(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object retVal = joinPoint.proceed();
        stopWatch.stop();
        logExecutionTime(joinPoint, stopWatch);
        return retVal;
    }*/
    private void logExecutionTime(ProceedingJoinPoint joinPoint, StopWatch stopWatch) {
        String logMessage = String.format(LOG_MESSAGE_FORMAT, joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), stopWatch.getTotalTimeMillis());
        LOG.info(logMessage.toString());
    }
}
