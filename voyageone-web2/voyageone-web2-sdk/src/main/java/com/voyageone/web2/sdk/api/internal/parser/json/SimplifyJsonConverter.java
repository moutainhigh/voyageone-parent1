package com.voyageone.web2.sdk.api.internal.parser.json;

import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.internal.mapping.Converter;
import com.voyageone.web2.sdk.api.internal.mapping.Converters;
import com.voyageone.web2.sdk.api.internal.mapping.Reader;
import com.voyageone.web2.sdk.api.internal.util.json.ExceptionErrorListener;
import com.voyageone.web2.sdk.api.internal.util.json.JSONReader;
import com.voyageone.web2.sdk.api.internal.util.json.JSONValidatingReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 精简版JSON格式转换器
 * 
 * @author carver.gu
 * @since 1.0, 2014/03/05
 */
public class SimplifyJsonConverter implements Converter {

	public <T extends VoApiResponse> T toResponse(String rsp, Class<T> clazz) throws ApiException {
		JSONReader reader = new JSONValidatingReader(new ExceptionErrorListener());
		Object rootObj = reader.read(rsp);
		if (rootObj instanceof Map<?, ?>) {
			Map<?, ?> rootJson = (Map<?, ?>) rootObj;
			Object errorJson = rootJson.get(VoApiConstants.ERROR_RESPONSE);
			if (errorJson instanceof Map<?, ?>) {
				return fromJson((Map<?, ?>) errorJson, clazz);
			} else {
				return fromJson(rootJson, clazz);
			}
		}
		return null;
	}

	/**
	 * 把JSON格式的数据转换为对象
	 * 
	 * @param <T> 泛型领域对象
	 * @param json JSON格式的数�?
	 * @param clazz 泛型领域类型
	 * @return 领域对象
	 */
	public <T> T fromJson(final Map<?, ?> json, Class<T> clazz) throws ApiException {
		return Converters.convert(clazz, new Reader() {
			public boolean hasReturnField(Object name) {
				return json.containsKey(name);
			}

			public Object getPrimitiveObject(Object name) {
				return json.get(name);
			}

			public Object getObject(Object name, Class<?> type) throws ApiException {
				Object tmp = json.get(name);
				if (tmp instanceof Map<?, ?>) {
					Map<?, ?> map = (Map<?, ?>) tmp;
					return fromJson(map, type);
				} else {
					return null;
				}
			}

			public List<?> getListObjects(Object listName, Object itemName, Class<?> subType) throws ApiException {
				List<Object> listObjs = null;

				Object jsonList = json.get(listName);
				if (jsonList instanceof List<?>) {
					listObjs = new ArrayList<Object>();
					List<?> listObj = (List<?>) jsonList;
					for (Object tmp : listObj) {
						if (tmp instanceof Map<?, ?>) {// object
							Map<?, ?> subMap = (Map<?, ?>) tmp;
							Object subObj = fromJson(subMap, subType);
							if (subObj != null) {
								listObjs.add(subObj);
							}
						} else if (tmp instanceof List<?>) {// array
							// TODO not support yet
						} else {// boolean, long, double, string, null
							listObjs.add(tmp);
						}
					}
				}

				return listObjs;
			}
		});
	}

}
