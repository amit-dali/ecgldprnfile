package nl.ebay.creditlimittracker.aop;

import lombok.extern.slf4j.Slf4j;
import nl.ebay.creditlimittracker.util.GlobalConstants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class AspectLogging {

    private static final String EXCEPTION_LOG_PATTERN = "Exception while processing a %s request on the URI %s from class %s with message %s";
    private static final String ENTER_CONTROLLER_LOG_PATTERN = "Received a {} request on the endpoint {}";
    private static final String EXIT_CONTROLLER_LOG_PATTERN = "Success response sent to the {} {} request";
    private static final String ENTER_SERVICE_LOG_PATTERN = "ENTER :: {}() :: {} Service execution started";
    private static final String EXIT_SERVICE_LOG_PATTERN = "EXIT :: {}() :: {} Service execution completed";
    private static final String EXCEPTION_PREFIX = "EXCEPTION :: {}() :: {} is {}";


    /**
     * This method is invokes by aspect based on execution expression. Method is
     * used to (INFO) log a Enter and exit/Exception log of a nl.jumbo.storemanager.presentation Add
     * flowId, sessionId and requestId in slf4j MDC to include in logs
     *
     * @param proceedingJoinPoint {@link ProceedingJoinPoint}
     * @return output of the method
     * @throws Throwable when the method raises an Exception
     */
    @Around("execution( * nl.ebay.creditlimittracker.presentation.*Controller.* (..))")
    public Object logControllers(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            MDC.put(GlobalConstants.REQUEST_ID_LOGGER_MDC_KEY, UUID.randomUUID().toString());
            Optional<HttpServletRequest> contextOptional = getRequestFromContext();
            log.info(ENTER_CONTROLLER_LOG_PATTERN, proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                    proceedingJoinPoint.getSignature().getName(),
                    contextOptional.isPresent() ? contextOptional.get().getMethod() : GlobalConstants.NOT_APPLICABLE,
                    contextOptional.isPresent() ? contextOptional.get().getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE) : GlobalConstants.NOT_APPLICABLE);

            Object result = proceedingJoinPoint.proceed();
            log.info(EXIT_CONTROLLER_LOG_PATTERN, proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                    proceedingJoinPoint.getSignature().getName(),
                    contextOptional.isPresent() ? contextOptional.get().getMethod() : GlobalConstants.NOT_APPLICABLE,
                    contextOptional.isPresent() ? contextOptional.get().getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE) : GlobalConstants.NOT_APPLICABLE);
            return result;
        } catch (Throwable th) {
            Optional<HttpServletRequest> contextOptional = getRequestFromContext();
            String message = String.format(EXCEPTION_LOG_PATTERN,
                    contextOptional.isPresent() ? contextOptional.get().getMethod() : GlobalConstants.NOT_APPLICABLE,
                    contextOptional.isPresent() ? contextOptional.get().getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE) : GlobalConstants.NOT_APPLICABLE,
                    getExceptionDetails(th), th.getMessage());
            log.error(EXCEPTION_PREFIX, proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                    proceedingJoinPoint.getSignature().getName(), message);
            throw th;
        } finally {
            MDC.clear();
        }
    }

    /**
     * This method is invokes by aspect based on execution expression. Method is
     * used to (Debug) log a Enter and exit log of a services Add requestId and
     * SessionId in slf4j MDC to include in logs
     *
     * @param proceedingJoinPoint {@link ProceedingJoinPoint}
     * @return output of the method
     * @throws Throwable when the method raises an Exception
     */
    @Around("execution( * nl.ebay.creditlimittracker.service.*.* (..))")
    public Object logService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Optional<HttpServletRequest> requestFromContext = getRequestFromContext();
        log.debug(ENTER_SERVICE_LOG_PATTERN, proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                proceedingJoinPoint.getSignature().getName(),
                requestFromContext.isPresent() ? requestFromContext.get().getMethod() : GlobalConstants.NOT_APPLICABLE,
                requestFromContext.isPresent() ? requestFromContext.get().getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE) : GlobalConstants.NOT_APPLICABLE);

        Object result = proceedingJoinPoint.proceed();
        log.debug(EXIT_SERVICE_LOG_PATTERN, proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                proceedingJoinPoint.getSignature().getName(),
                requestFromContext.isPresent() ? requestFromContext.get().getMethod() : GlobalConstants.NOT_APPLICABLE,
                requestFromContext.isPresent() ? requestFromContext.get().getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE) : GlobalConstants.NOT_APPLICABLE);
        return result;
    }


    private Optional<HttpServletRequest> getRequestFromContext() {
        Optional<ServletRequestAttributes> attributesOptional = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return attributesOptional.map(ServletRequestAttributes::getRequest);
    }

    private String getExceptionDetails(Throwable th) {
        return Arrays.stream(th.getStackTrace())
                .filter(rootCause -> rootCause.getClassName().contains("nl.capgemini.smartbuilding")).findFirst()
                .map(ignitionClass -> ignitionClass.getClassName() + "." + ignitionClass.getMethodName()
                        + "() at line number " + ignitionClass.getLineNumber())
                .orElse("No Smart office class was involved");
    }
}
