package com.digital.v3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.digital.v3.schema.Auth;
import com.digital.v3.service.AuthService;
import com.digital.v3.schema.ErrorMsg;
import com.digital.v3.schema.SuccessMsg;
import com.digital.v3.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "인증", description = "Auth Related API")
@RequestMapping(value = "/rest/auth")
public class AuthController {
	
	@Resource
	private AuthService authSvc;
	
	@RequestMapping(value = "/tokenValid", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "토큰 검증", notes = "토큰의 유효성을 검증하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Auth.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> tokenVaild (HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();

		Auth resAuth = new Auth();
		try {
			String token = request.getHeader("Authorization");
			if (authSvc.isValidToken(token)) {
				resAuth.setValidToken(true);
			}
			else {
				resAuth.setValidToken(false);
			}
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		return new ResponseEntity<Auth>(resAuth, header, HttpStatus.valueOf(200));
	}
	
	@RequestMapping(value = "/expireCheck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "토큰 유효시간 검증", notes = "토큰의 유효시간을 검증하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Auth.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> expireCheck (HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();

		Auth resAuth = new Auth();
		try {
			String token = request.getHeader("Authorization");
			if (authSvc.isExpiredToken(token)) {
				resAuth.setExpiredToken(true);
			}
			else {
				resAuth.setExpiredToken(false);
			}
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		return new ResponseEntity<Auth>(resAuth, header, HttpStatus.valueOf(200));
	}
	
	@RequestMapping(value = "/generateToken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "토큰 생성", notes = "로그인 정보로 토큰을 생성하는 API. *입력필드: personId")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Auth.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> generateToken (@ApiParam(value = "로그인 정보", required = false) @RequestBody Auth auth) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();

		Auth resAuth = new Auth();
		try {
			String token = authSvc.setToken(auth.getPersonId());
			resAuth.setToken(token);
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		return new ResponseEntity<Auth>(resAuth, header, HttpStatus.valueOf(200));
	}
	
	@RequestMapping(value = "/personInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "사용자 ID 확인", notes = "토큰 정보로 사용자 ID를 확인하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Auth.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> getPersonId (HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();

		Auth resAuth = new Auth();
		try {
			String token = request.getHeader("Authorization");
			long personId = authSvc.getPersonId(token);
			resAuth.setPersonId(personId);
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		return new ResponseEntity<Auth>(resAuth, header, HttpStatus.valueOf(200));
	}
	
	@RequestMapping(value = "/updateValidTime", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "토큰 유효시간 갱신", notes = "토큰의 유효시간을 갱신하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = SuccessMsg.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> updateValidTime (HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		SuccessMsg success = new SuccessMsg();

		try {
			String token = request.getHeader("Authorization");
			authSvc.updateValidTime(token);
			
			success.setSuccessCode(200);
			success.setSuccessMsg("유효시간을 갱신했습니다.");
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		return new ResponseEntity<SuccessMsg>(success, header, HttpStatus.valueOf(200));
	}
	
	@RequestMapping(value = "/deleteToken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "토큰 삭제", notes = "토큰을 삭제하는 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = SuccessMsg.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> deleteToken (HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		SuccessMsg success = new SuccessMsg();

		try {
			String token = request.getHeader("Authorization");
			authSvc.deleteToken(token);
			
			success.setSuccessCode(200);
			success.setSuccessMsg("토큰을 삭제했습니다.");
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		return new ResponseEntity<SuccessMsg>(success, header, HttpStatus.valueOf(200));
	}

}
