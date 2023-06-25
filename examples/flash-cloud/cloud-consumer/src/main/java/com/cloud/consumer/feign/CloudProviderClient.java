package com.cloud.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zsp
 * @date 2023/6/25 16:07
 */
@FeignClient(name = "cloud-provider")
public interface CloudProviderClient {

    @GetMapping(value = "/echo/{string}")
    String echo(@PathVariable("string") String string);
}
