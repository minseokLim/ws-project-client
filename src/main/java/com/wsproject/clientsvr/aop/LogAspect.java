package com.wsproject.clientsvr.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 함수 실행 시작/끝에 Log를 자동 생성시켜주는 Aspect
 * @author mslim
 */
@Aspect
@Component
public class LogAspect {
	
	/**
	 * 함수 실행 시작/끝에 Log를 자동 생성
	 * @param point
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* com.wsproject.clientsvr.controller.*.*(..))")
	public Object setStartEndLogs(ProceedingJoinPoint point) throws Throwable {
		Signature signature = point.getSignature();
		Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());
		
		logger.debug(signature.getName() + " started");
		
		Object result = point.proceed();
		
		logger.debug(signature.getName() + " ended");
		
		return result;
	}
}
