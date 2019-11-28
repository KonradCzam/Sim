// 
// Decompiled by Procyon v0.5.36
// 

package com.example.Sim.Utilities;

import org.apache.commons.logging.LogFactory;
import org.springframework.util.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Component
public class LogExecutionTime
{
    private static final String LOG_MESSAGE_FORMAT = "%s.%s execution time: %dms";
    private static final Log LOG;
    
    private void logExecutionTime(final ProceedingJoinPoint joinPoint, final StopWatch stopWatch) {
        final String logMessage = String.format("%s.%s execution time: %dms", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), stopWatch.getTotalTimeMillis());
        LogExecutionTime.LOG.info((Object)logMessage.toString());
    }
    
    static {
        LOG = LogFactory.getLog((Class)LogExecutionTime.class);
    }
}
