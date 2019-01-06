package com.monogramm.starter.persistence.type.service;

import com.monogramm.starter.dto.type.TypeDto;
import com.monogramm.starter.persistence.GenericService;
import com.monogramm.starter.persistence.type.entity.Type;
import com.monogramm.starter.persistence.type.exception.TypeNotFoundException;

import java.util.List;

/**
 * {@link Type} service interface.
 * 
 * @author madmath03
 */
public interface ITypeService extends GenericService<Type, TypeDto> {

  @Override
  TypeBridge getBridge();

  /**
   * Find all types by their name while ignoring case.
   * 
   * @param name the name content to search.
   * 
   * @return the list of all the types matching the search.
   */
  List<Type> findAllByName(final String name);

  /**
   * Find an type through its name while ignoring case.
   * 
   * @param name the name to search.
   * 
   * @return the type matching the name.
   * 
   * @throws TypeNotFoundException if no type matches the name in the repository.
   */
  Type findByName(final String name);

}
