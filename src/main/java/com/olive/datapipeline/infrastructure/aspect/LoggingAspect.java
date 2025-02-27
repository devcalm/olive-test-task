package com.olive.datapipeline.infrastructure.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(public * com.olive.datapipeline.service.kafka.*Listener.*(..))")
    private void kafkaListeners() {
    }

    @Pointcut("execution(public * com.olive.datapipeline.web.api.*Controller.*(..))")
    private void controllers() {
    }

    @Around(value = "kafkaListeners() || controllers()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String args = Arrays.toString(joinPoint.getArgs());
        String fullyQualifiedClassName = joinPoint.getSignature().getDeclaringTypeName();
        String className = fullyQualifiedClassName.substring(fullyQualifiedClassName.lastIndexOf('.') + 1);
        String methodName = joinPoint.getSignature().getName();
        Logger logger = LoggerFactory.getLogger(fullyQualifiedClassName); // loggers are cached so no big overhead.

        logger.info("IN  {}.{}{}", className, methodName, args);

        Object result = joinPoint.proceed(joinPoint.getArgs());
        if (result == null) {
            result = "";
        }
        logger.info("OUT {}.{} > {}", className, methodName, result);
        return result;
    }
}
