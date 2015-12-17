package com.voyageone.web2.sdk.api;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * TOP请求接口。
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public abstract class VoApiRequest<T extends VoApiResponse> {

	protected Long timestamp;

	/**
	 * @return 指定或默认的时间戳
	 */
	public Long getTimestamp() {
		if (timestamp == null) {
			return DateTimeUtil.getNowTimeStampLong();
		}
		return timestamp;
	}

	/**
	 * 设置时间戳，如果不设置,发送请求时将使用当时的时间。
	 *
	 * @param timestamp 时间戳
	 */
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Header info
	 */
	protected HttpHeaders headers;
	public HttpHeaders getHeaders() {
		if (headers == null) {
			return new HttpHeaders();
		}
		return headers;
	}

	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}

	/**
	 * Http Method
	 * Default POST
	 */
	protected HttpMethod httpMethod = HttpMethod.POST;
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
	 * 获取TOP的API名称。
	 *
	 * @return API名称
	 */
	public abstract String getApiURLPath();

	/**
	 * get Response Class type
	 * @return class Type
	 */
	public abstract Class<T> getResponseClass();

	/**
	 * 客户端参数检查，减少服务端无效调用
	 */
	public void check() throws ApiRuleException {

	}

}
