package nextstep.subway.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.log4j.Log4j2;

@Component
@Aspect // Aspect 를 꼭 명시!
@Log4j2
class LogAspect {

	@Around("@annotation(LogExecutionTime)") // 해당 어노테이션에 대해서
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Object proceed = joinPoint.proceed();

		stopWatch.stop();
		log.info(stopWatch.prettyPrint());
		return proceed;
	}
}
