/*
 * Creation by madmath03 the 2017-11-13.
 */

package com.monogramm.starter.utils;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * A JSON compatible object.
 * 
 * @author madmath03
 */
public interface Jsonable {

  /**
   * Returns a JSON string representation of this object.
   * 
   * <p>
   * This method is generally intended to serialize objects during <em>JSON</em> serialization.
   * </p>
   *
   * @return a JSON string representation of this.
   * 
   * @throws JsonProcessingException if the object could not be serialized to JSON.
   */
  public default String toJson() throws JsonProcessingException {
    return JsonUtils.toJson(this);
  }
}
