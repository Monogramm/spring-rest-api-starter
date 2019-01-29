/*
 * Creation by madmath03 the 2019-01-29.
 */

package com.monogramm.starter.dto;

import java.util.Comparator;
import java.util.Date;

/**
 * Default {@link AbstractGenericDto} comparator.
 * 
 * <p>
 * Compares {@link AbstractGenericDto} by their creation date (descending order), then modified date
 * (descending order).
 * </p>
 * <p>
 * This comparator does not allow {@code null} {@link AbstractGenericDto} but {@code null} fields
 * are managed. Fields with {@code null} are
 * </p>
 * 
 * @author madmath03
 */
public class AbstractGenericDtoComparator implements Comparator<AbstractGenericDto> {

  private static final Comparator<String> stringComparator =
      Comparator.nullsFirst(Comparator.naturalOrder());

  private static final Comparator<Date> dateComparator =
      Comparator.nullsFirst(Comparator.reverseOrder());

  @Override
  public int compare(AbstractGenericDto o1, AbstractGenericDto o2) {
    final int compare;

    final int compareCreatedAt = compareCreatedAt(o1, o2);
    if (compareCreatedAt != 0) {
      compare = compareCreatedAt;
    } else {
      compare = compareModifiedAt(o1, o2);
    }

    return compare;
  }

  /**
   * Compare two {@link AbstractGenericDto#getCreatedAt()} dates.
   * 
   * @param o1 the first {@link AbstractGenericDto} to be compared.
   * @param o2 the second {@link AbstractGenericDto} to be compared.
   * 
   * @return a negative integer, zero, or a positive integer as the first argument is less than,
   *         equal to, or greater than the second.
   */
  public static int compareCreatedAt(AbstractGenericDto o1, AbstractGenericDto o2) {
    return compareOnDate(o1.getCreatedAt(), o2.getCreatedAt());
  }

  /**
   * Compare two {@link AbstractGenericDto#getModifiedAt()} dates.
   * 
   * @param o1 the first {@link AbstractGenericDto} to be compared.
   * @param o2 the second {@link AbstractGenericDto} to be compared.
   * 
   * @return a negative integer, zero, or a positive integer as the first argument is less than,
   *         equal to, or greater than the second.
   */
  public static int compareModifiedAt(AbstractGenericDto o1, AbstractGenericDto o2) {
    return compareOnDate(o1.getModifiedAt(), o2.getModifiedAt());
  }

  /**
   * Compare two dates.
   * 
   * @param d1 the first date to be compared.
   * @param d2 the second date to be compared.
   * 
   * @return a negative integer, zero, or a positive integer as the first argument is less than,
   *         equal to, or greater than the second.
   */
  public static int compareOnDate(Date d1, Date d2) {
    return dateComparator.compare(d1, d2);
  }

  /**
   * Compare two strings.
   * 
   * @param s1 the first string to be compared.
   * @param s2 the second string to be compared.
   * 
   * @return a negative integer, zero, or a positive integer as the first argument is less than,
   *         equal to, or greater than the second.
   */
  public static int compareOnString(String s1, String s2) {
    return stringComparator.compare(s1, s2);
  }

}
