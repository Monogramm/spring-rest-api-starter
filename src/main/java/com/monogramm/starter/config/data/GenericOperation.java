/*
 * Creation by madmath03 the 2017-11-26.
 */

package com.monogramm.starter.config.data;

import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.type.entity.Type;

/**
 * Generic CRUD operations for types.
 * 
 * <p>
 * This enumeration is highly related to the concept of {@link Permission} and provide some regular
 * CRUD operations.
 * </p>
 * 
 * <p>
 * String constants are also available.
 * </p>
 * 
 * @see Permission
 * @see InitialDataLoader
 * 
 * @author madmath03
 */
public enum GenericOperation {
  /**
   * Read a single data.
   */
  READ,
  /**
   * List data of a given type.
   */
  LIST,
  /**
   * Create a new data of a given type.
   */
  CREATE,
  /**
   * Update a data of a given type.
   */
  UPDATE,
  /**
   * Delete a data of a given type.
   */
  DELETE;

  /**
   * The Permission separator between the type and the operation.
   */
  public static final String PERM_SEP = "_";
  /**
   * The read Permission name.
   * 
   * @see GenericOperation#READ
   */
  public static final String PERM_READ = "READ";
  /**
   * The list Permission name.
   * 
   * @see GenericOperation#LIST
   */
  public static final String PERM_LIST = "LIST";
  /**
   * The create Permission name.
   * 
   * @see GenericOperation#CREATE
   */
  public static final String PERM_CREATE = "CREATE";
  /**
   * The update Permission name.
   * 
   * @see GenericOperation#UPDATE
   */
  public static final String PERM_UPDATE = "UPDATE";
  /**
   * The delete Permission name.
   * 
   * @see GenericOperation#DELETE
   */
  public static final String PERM_DELETE = "DELETE";


  /**
   * Create a regular {@link Permission} name for the given type and operation.
   * 
   * @param type a type for which to create a permission name.
   * @param operation an operation.
   * 
   * @return a regular {@link Permission} name for the given type and operation.
   */
  public static String permissionName(final Type type, final GenericOperation operation) {
    return permissionName(type.getName(), operation);
  }

  /**
   * Create a regular {@link Permission} name for the given type and operation.
   * 
   * @param typeName a type name for which to create a permission name.
   * @param operation an operation.
   * 
   * @return a regular {@link Permission} name for the given type and operation.
   */
  public static String permissionName(final String typeName, final GenericOperation operation) {
    final StringBuilder permissionName = new StringBuilder();

    permissionName.append(typeName).append(GenericOperation.PERM_SEP).append(operation.toString());

    return permissionName.toString().toUpperCase();
  }

  /**
   * Create a regular {@link Permission} name for the given type and this operation.
   * 
   * @param type a type for which to create a type operation name.
   * 
   * @return a regular {@link Permission} name for the given type and this operation.
   */
  public String permissionName(final Type type) {
    return GenericOperation.permissionName(type, this);
  }

  /**
   * Create all regular {@link Permission} names for the given type.
   * 
   * @param type a type for which to create all permission names.
   * 
   * @return all regular {@link Permission} names for the given type.
   */
  public static String[] allPermissionNames(final Type type) {
    return allPermissionNames(type.getName());
  }

  /**
   * Create all regular {@link Permission} names for the given type.
   * 
   * @param typeName a type name for which to create all permission names.
   * 
   * @return all regular {@link Permission} names for the given type.
   */
  public static String[] allPermissionNames(final String typeName) {
    final GenericOperation[] values = GenericOperation.values();
    final String[] permissionNames = new String[values.length];

    for (int i = 0; i < values.length; i++) {
      final GenericOperation genericOperation = values[i];
      permissionNames[i] = permissionName(typeName, genericOperation);
    }

    return permissionNames;
  }

}
