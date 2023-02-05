package com.cos.person.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.person.domain.CommonDto;
import com.cos.person.domain.JoinReqDto;
import com.cos.person.domain.UpdateReqDto;
import com.cos.person.domain.User;
import com.cos.person.domain.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	
	private final UserRepository userRepository;
	
	// DI == 의존성 주입
	// 파라미터인 userRepository로 스프링 IOC에 떠있는걸 찾고 있으면 주입시켜준다.
	/*public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}*/
	
	// http://localhost:9090/user
	@GetMapping("/user")
	public CommonDto<List<User>> findAll() {
		System.out.println("findAll()");
		return new CommonDto<>(HttpStatus.OK.value(), userRepository.findAll());	// MessageConverter => (JavaObject -> Json String)
	}
	
	// http://localhost:9090/user/1
	@GetMapping("/user/{id}")
	public CommonDto<User> findById(@PathVariable int id) {
		System.out.println("findById() id :  "+id);
		return new CommonDto<>(HttpStatus.OK.value(), userRepository.findById(id));
	}
	
	// http://localhost:9090/user
	@PostMapping("/user")
	@CrossOrigin	// CORS 정책 풀어주는 어노테이션 도메인이 다르면 http로 접근 불가
	// x-www-form-urlencoded => request.getParameter()
	// text/plane => @RequestBody 어노테이션을 쓰면 받을 수 있다.
	// application/json => @ResponseBody 어노테이션 + 오브젝트로 받기
	public CommonDto<?> save(@Valid @RequestBody JoinReqDto dto, BindingResult bindingResult) {
		
		System.out.println("bindingResult = "+bindingResult.getErrorCount());
		
		System.out.println("save()");
		System.out.println("user : "+dto);
		userRepository.save(dto);
		//System.out.println("username : "+username);
		//System.out.println("password : "+password);
		//System.out.println("phone : "+phone);
		return new CommonDto<>(HttpStatus.CREATED.value(), "ok");
	}
	
	@DeleteMapping("/user/{id}")
	public CommonDto delete(@PathVariable int id) {
		System.out.println("delete()");
		userRepository.delete(id);
		return new CommonDto<>(HttpStatus.OK.value());
	}
	
	@PutMapping("/user/{id}")
	public CommonDto update(@PathVariable int id, @Valid @RequestBody UpdateReqDto dto, BindingResult bindingResult) {
		
		System.out.println("bindingResult = "+bindingResult.getErrorCount());
		
		System.out.println("update()");
		userRepository.update(id, dto);
		return new CommonDto<>(HttpStatus.OK.value());
	}
}








