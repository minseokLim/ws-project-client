package com.wsproject.clientsvr.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author mslim
 * 함수 실행 시작/끝에 Log를 자동 생성시켜주는 Aspect
 */
@Aspect
@Component
public class LogAspect {
	
	@Around("execution(* com.wsproject.clientsvr.controller.*.*(..))")
	public Object startEndLog(ProceedingJoinPoint point) throws Throwable {
		Signature signature = point.getSignature();
		Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());
		
		logger.info(signature.getName() + " started");
		
		Object result = point.proceed();
		
		logger.info(signature.getName() + " ended");
		
		return result;
	}
}
