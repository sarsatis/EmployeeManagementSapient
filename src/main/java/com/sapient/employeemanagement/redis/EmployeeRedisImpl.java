package com.sapient.employeemanagement.redis;

import java.util.List;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sapient.employeemanagement.model.Employee;

@Service
public class EmployeeRedisImpl implements EmployeeRedis {

	private RedisTemplate<String, Object> redisTemplate;

	@SuppressWarnings("rawtypes")
	private HashOperations hashOps;
	
	
	public EmployeeRedisImpl(RedisTemplate<String, Object> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
		this.hashOps = redisTemplate.opsForHash();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean saveEmployeesBasedOnLocation(String key, List<Employee> employee) {
		hashOps.put(key, key, employee);
		redisTemplate.persist(key);
		return true;
	}

	@Override
	public List<Object> getEmployeeBasedOnLocation(String key) {
		// TODO Auto-generated method stub
		return redisTemplate.opsForHash().values(key);
	}

	@Override
	public boolean checkLocationExists(String key) {
		// TODO Auto-generated method stub
		return redisTemplate.hasKey(key);
	}

}
