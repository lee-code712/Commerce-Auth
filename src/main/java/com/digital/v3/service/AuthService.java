package com.digital.v3.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class AuthService {

	private static Map<String, Map<Long, Long>> tokenMap;
	
	/* token 생성 */
	public synchronized String setToken (long personId) throws Exception {
		
		if (tokenMap == null) {
			tokenMap = new HashMap<String, Map<Long, Long>>();
		}
		
		// token map value set
		Map<Long, Long> authMap = new HashMap<Long, Long>();
		long currentTime = System.currentTimeMillis();
		authMap.put(personId, currentTime);
		
		// token set & token map put
		String token = "Auth" + System.currentTimeMillis();
		tokenMap.put(token, authMap);
		
		return token;
	}
	
	/* token 삭제 */
	public synchronized void deleteToken (String token) {
		tokenMap.remove(token);
	}
	
	/* person ID 반환 */
	public long getPersonId (String token) {
		
		Map<Long, Long> authMap = tokenMap.get(token);
		if (authMap != null) {
			Set<Long> set = authMap.keySet();
			Iterator<Long> iterator = set.iterator();
			
			if (iterator.hasNext()) {
				return iterator.next();
			}
		}
		return 0;
	}
	
	/* token 유효 시간 갱신 */
	public synchronized void updateValidTime (String token) {
		
		Map<Long, Long> authMap = tokenMap.get(token);
		if (authMap != null) {
			Set<Long> set = authMap.keySet();
			Iterator<Long> iterator = set.iterator();
			
			if (iterator.hasNext()) {
				long personId = iterator.next();
				long currentTime = System.currentTimeMillis();
				authMap.put(personId, currentTime);
				
				tokenMap.put(token, authMap);
			}
		}
	}
	
	/* token 유효 여부 확인 */
	public boolean isValidToken (String token) {
		
		if (tokenMap != null && tokenMap.get(token) != null) {
			return true;
		}
		return false;
	}
	
	/* token 만료 여부 확인 */
	public boolean isExpiredToken (String token) {
		
		Map<Long, Long> authMap = tokenMap.get(token);
		if (authMap == null) {
			return true;
		}
		
		Set<Long> set = authMap.keySet();
		Iterator<Long> iterator = set.iterator();

		if (iterator.hasNext()) {
			long personId = iterator.next();
			long startTime = authMap.get(personId);
			long currentTime = System.currentTimeMillis();
			long elapse = currentTime - startTime;

			if (elapse > 30 * 60 * 1000) {
				return true;
			}
		}
		return false;
	}
		
}
