
package com.sapient.weather.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cod",
    "message",
    "cnt",
    "list",
    "city"
})
@Generated("jsonschema2pojo")
public class WeatherResponse {

    @JsonProperty("cod")
    private String cod;
    @JsonProperty("message")
    private Integer message;
    @JsonProperty("cnt")
    private Integer cnt;
    @JsonProperty("list")
    private java.util.List<com.sapient.weather.response.List> list = new ArrayList<com.sapient.weather.response.List>();
    @JsonProperty("city")
    private City city;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("cod")
    public String getCod() {
        return cod;
    }

    @JsonProperty("cod")
    public void setCod(String cod) {
        this.cod = cod;
    }

    public WeatherResponse withCod(String cod) {
        this.cod = cod;
        return this;
    }

    @JsonProperty("message")
    public Integer getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(Integer message) {
        this.message = message;
    }

    public WeatherResponse withMessage(Integer message) {
        this.message = message;
        return this;
    }

    @JsonProperty("cnt")
    public Integer getCnt() {
        return cnt;
    }

    @JsonProperty("cnt")
    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public WeatherResponse withCnt(Integer cnt) {
        this.cnt = cnt;
        return this;
    }

    @JsonProperty("list")
    public java.util.List<com.sapient.weather.response.List> getList() {
        return list;
    }

    @JsonProperty("list")
    public void setList(java.util.List<com.sapient.weather.response.List> list) {
        this.list = list;
    }

    public WeatherResponse withList(java.util.List<com.sapient.weather.response.List> list) {
        this.list = list;
        return this;
    }

    @JsonProperty("city")
    public City getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(City city) {
        this.city = city;
    }

    public WeatherResponse withCity(City city) {
        this.city = city;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public WeatherResponse withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

	@Override
	public String toString() {
		return "WeatherResponse [cod=" + cod + ", message=" + message + ", cnt=" + cnt + ", list=" + list + ", city="
				+ city + ", additionalProperties=" + additionalProperties + "]";
	}

}
