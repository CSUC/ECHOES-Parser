/**
 * 
 */
package org.csuc.rest.api.utils;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author amartinez
 *
 */
public class ResponseError implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String error;
	private String description;
	private int responseCode;
	
	public ResponseError(@JsonProperty("error") String error, @JsonProperty("error_description") String description,
			@JsonProperty("responseCode") int code) {

		this.error = error;
		this.description = description;
		this.responseCode = code;
	}

	@JsonProperty("error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	@JsonProperty("error_description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("responseCode")
	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
}
