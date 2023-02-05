package com.cos.person.config;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cos.person.domain.CommonDto;

import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;

@Component // @Controller가 뜨고나서 뜬다
@Aspect
public class BindingAdvice {
	
	
	private static final Logger log = LoggerFactory.getLogger(BindingAdvice.class);
	
	// 어떤 함수가 언제 몇번 실행됬는지 횟수같은거 로그 남기기
	@Before("execution(* com.cos.person.web..*Controller.*(..))")
	public void testCheck() {
		HttpServletRequest request = 
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		System.out.println("주소 : "+request.getRequestURI());
		// request 값 처리는?
		// log처리는? 파일로 어떻게?
		System.out.println("전처리 로그를 남겼습니다.");
	}
	
	@After("execution(* com.cos.person.web..*Controller.*(..))")
	public void testCheck2() {
		System.out.println("후처리 로그를 남겼습니다.");
	}
	
	// 함수 : 앞 뒤를 제어
	// 함수 : 앞만 제어 (username이 안들어왔으면 내가 강제로 넣어주고 실행하게 할수도 잇다)
	// 함수 : 뒤만 제어(응답만 관리)
	//@Before
	//@After
	@Around("execution(* com.cos.person.web..*Controller.*(..))")
	public Object validCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
		String method = proceedingJoinPoint.getSignature().getName();
		
		System.out.println("type : "+type);
		System.out.println("method : "+method);
		
		Object[] args = proceedingJoinPoint.getArgs();
		
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						// 로그 레벨
						log.warn(type+", "+method+"() => 필드 : "+error.getField()+", 메세지 : "+error.getDefaultMessage());
						log.debug(type+", "+method+"() => 디버그 필드 : "+error.getField()+", 메세지 : "+error.getDefaultMessage());
						Sentry.captureMessage(type+", "+method+"() => 필드 : "+error.getField()+", 메세지 : "+error.getDefaultMessage());
					}
					
					return new CommonDto<>(HttpStatus.BAD_REQUEST.value(), errorMap);
				}
			}
		}
		return proceedingJoinPoint.proceed(); // 함수의 스택을 실행해라.
	}
	
}
