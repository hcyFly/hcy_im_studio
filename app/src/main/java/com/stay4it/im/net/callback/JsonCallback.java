package com.stay4it.im.net.callback;

import com.stay4it.im.untils.JsonParser;
import com.stay4it.im.untils.Trace;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/** 
 * @author Stay  
 * @version create time：Sep 15, 2014 12:40:04 PM 
 * @param <T>
 */
public abstract class JsonCallback<T> extends AbstractCallback<T> {
	@Override
	public T bindData(String json) {
		Trace.d("request result:" + json);
		Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return JsonParser.deserializeFromJson(json, type);

	}

}
