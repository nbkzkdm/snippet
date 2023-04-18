/**
 * 
 */
package jp.sample.holiday.common;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

/**
 * @author nbkzk
 *
 */
@Aspect
@Component
@Log4j2
public class LoggingInterceptor {

    @Before("within(jp.sample.postal.code.api.*)")
    public void controllerLog(JoinPoint jp) {
        methodInfoLog(jp, "api");
    }

    @Before("within(jp.sample.postal.code.api.process.*)")
    public void processLog(JoinPoint jp) {
        methodInfoLog(jp, "process");
    }

    @Before("within(jp.sample.postal.code.domain.service.*)")
    public void serviceLog(JoinPoint jp) {
        methodInfoLog(jp, "service");
    }
 
    @AfterThrowing(value = "within(jp.sample.postal.code.domain.service.*)", throwing = "e")
    public void throwLog(Throwable e) {
        log.error(e.getMessage(), e);
    }
    private void methodInfoLog(JoinPoint jp, String prefix){
        Logger logger = LoggerFactory.getLogger(jp.getTarget().getClass());
        logger.info(String.format("%s: %s(%s)",
                prefix, jp.getSignature().getName(), Arrays.toString(jp.getArgs())));
    }

    @Before("execution(public * jp.sample.postal.code.domain.repository..*(..))")
    public void repositoryLog(JoinPoint jp) {
        methodDebugLog(jp, "repository");
    }
    private void methodDebugLog(JoinPoint jp, String prefix){
        log.debug(String.format("%s: %s#%s(%s)",
                prefix, jp.getSignature().getDeclaringType().getSimpleName(),
                jp.getSignature().getName(), Arrays.toString(jp.getArgs())));
    }

}
