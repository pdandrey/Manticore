package com.ncgeek.android.manticore.mock;

import java.util.HashMap;

public final class MockCounter {

	private HashMap<String, HashMap<Object[],Integer>> mapCalls = new HashMap<String, HashMap<Object[],Integer>>();
	
	private HashMap<String, HashMap<Object[], Object>> mapReturns = new HashMap<String, HashMap<Object[],Object>>();
	
	public MockCounter() {
		
	}
	
	public MockCounter returnWhen(Object returnValue, String method, Object...params) {
		
		HashMap<Object[], Object> mapParams = mapReturns.get(method);
		if(mapParams == null) {
			mapParams = new HashMap<Object[], Object>();
			mapReturns.put(method, mapParams);
		}
		
		mapParams.put(params, returnValue);
		
		return this;
	}
	
	public MockCounter throwWhen(Throwable tr, String method, Object...params){
		return returnWhen(tr, method, params);
	}
	
	private Object getReturn(String method, Object...params) {
		HashMap<Object[], Object> mapParams = mapReturns.get(method);
		if(mapParams == null) {
			return null;
		}
		Object ret =  mapParams.get(params);
		
		if(ret instanceof Throwable)
			throw new RuntimeException((Throwable)ret);
		
		return ret;
	}
	
	public Object call(String method, Object...params) {
		
		HashMap<Object[], Integer> mapParams = mapCalls.get(method);
		if(mapParams == null) {
			mapParams = new HashMap<Object[], Integer>();
			mapCalls.put(method, mapParams);
		}
		
		if(mapParams.containsKey(params)) {
			int count = mapParams.get(params) + 1;
			mapParams.put(params, count);
		} else {
			mapParams.put(params, 1);
		}
		
		return getReturn(method, params);
	}
	
	public void verify(int count, Object caller, String method, Object...params) {
		int calledCount = 0;
		Class<?> cls = caller.getClass();
		
		HashMap<Object[], Integer> mapParams = mapCalls.get(method);
		if(mapParams != null) {			
			if(mapParams.containsKey(params)) {
				calledCount = mapParams.get(params);
			} 
		}
		
		if(calledCount < count) {
			throw new RuntimeException(String.format("%s.%s(%s) failed verify. Expected <%d> but called <%d>.", cls.getSimpleName(), method, this.buildParamClasses(params), count, calledCount));
		}
	}
	
	private String buildParamClasses(Object[] params) {
		
		StringBuilder buf = new StringBuilder(50);
		
		for(Object o : params) {
			buf.append("," + o.getClass().getSimpleName());
		}
		
		return buf.substring(1);
	}
}

