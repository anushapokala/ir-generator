package com.neuron.cv.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT )
@JsonPropertyOrder({"public","private"})
public class Alley {
	@JsonProperty("public")
	public boolean _public;
	@JsonProperty("private")
	public boolean _private;
}
