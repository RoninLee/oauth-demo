package com.ronin.common;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常响应对象
 *
 * @author 应颂浩
 */
@Data
@RequiredArgsConstructor
public class ErrorResult implements Serializable {

    private static final long serialVersionUID = 2958790672375927046L;

    @NotNull
    final Integer code;

    @NotBlank
    final String message;

    final Map<String, String> extras = new HashMap<>();
}
