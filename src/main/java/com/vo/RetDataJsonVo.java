package com.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author strator
 */
@Data
@AllArgsConstructor
@Builder
public class RetDataJsonVo {

  private Object results;
  private Integer total;
  private Integer current;

}
