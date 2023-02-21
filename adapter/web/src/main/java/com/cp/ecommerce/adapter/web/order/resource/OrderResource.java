package com.cp.ecommerce.adapter.web.order.resource;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Resources representing order response.
 */
@Setter
@Getter
@Builder
public class OrderResource {

    private String remarks;

    private Date created;

}
