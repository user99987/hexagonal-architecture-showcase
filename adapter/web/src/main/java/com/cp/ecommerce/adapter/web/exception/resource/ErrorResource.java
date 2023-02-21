package com.cp.ecommerce.adapter.web.exception.resource;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResource {

    String id;

    String message;

}
