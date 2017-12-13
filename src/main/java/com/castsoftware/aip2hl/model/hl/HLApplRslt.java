package com.castsoftware.aip2hl.model.hl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HLApplRslt {
	HLApplication [] result;
	HLApplication [] error;
}
