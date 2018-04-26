/*
 * Units of Measurement Reference Implementation
 * Copyright (c) 2005-2018, Jean-Marie Dautelle, Werner Keil, Otavio Santana.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of JSR-385, Indriya nor the names of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tech.units.indriya.quantity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.measure.MetricPrefix;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.ElectricResistance;
import javax.measure.quantity.Length;
import javax.measure.quantity.Time;

import org.junit.jupiter.api.Test;

import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.quantity.ShortQuantity;
import tech.units.indriya.unit.Units;

public class ShortQuantityTest {

  private final ShortQuantity<ElectricResistance> ONE_OHM = createQuantity((short) 1, Units.OHM);
  private final ShortQuantity<ElectricResistance> TWO_OHM = createQuantity((short)2, Units.OHM);
  private final ShortQuantity<ElectricResistance> MAX_VALUE_OHM = createQuantity(Short.MAX_VALUE, Units.OHM);
  private final ShortQuantity<ElectricResistance> ONE_MILLIOHM = createQuantity((short)1, MetricPrefix.MILLI(Units.OHM));
  private final ShortQuantity<ElectricResistance> ONE_KILOOHM = createQuantity((short)1, MetricPrefix.KILO(Units.OHM));
  private final ShortQuantity<ElectricResistance> ONE_YOTTAOHM = createQuantity((short)1, MetricPrefix.YOTTA(Units.OHM));

  private <Q extends Quantity<Q>> ShortQuantity<Q> createQuantity(short s, Unit<Q> unit) {
    return new ShortQuantity<Q>(Short.valueOf(s).shortValue(), unit);
  }

  /**
   * Verifies that the addition of two quantities with the same multiples results in a new quantity with the same multiple and the value holding the
   * sum.
   */
  @Test
  public void additionWithSameMultipleKeepsMultiple() {
    Quantity<ElectricResistance> actual = ONE_OHM.add(TWO_OHM);
    ShortQuantity<ElectricResistance> expected = createQuantity((short) 3, Units.OHM);
    assertEquals(expected, actual);
  }

  /**
   * Verifies that the addition of two quantities with the same multiples resulting in an overflow throws an exception.
   */
  @Test
  public void additionWithSameMultipleResultingInOverflowThrowsException() {
    assertThrows(ArithmeticException.class, () -> {
      ONE_OHM.add(MAX_VALUE_OHM);
    });
  }

  /**
   * Verifies that adding a quantity with a larger multiple keeps the result to the smaller multiple.
   */
  @Test
  public void additionWithLargerMultipleKeepsSmallerMultiple() {
    Quantity<ElectricResistance> actual = ONE_MILLIOHM.add(ONE_OHM);
    ShortQuantity<ElectricResistance> expected = createQuantity((short) 1001, MetricPrefix.MILLI(Units.OHM));
    assertEquals(expected, actual);
  }

  /**
   * Verifies that adding a quantity with a smaller multiple casts the result to the smaller multiple.
   */
  @Test
  public void additionWithSmallerMultipleCastsToSmallerMultipleIfNeeded() {
    Quantity<ElectricResistance> actual = ONE_OHM.add(ONE_MILLIOHM);
    ShortQuantity<ElectricResistance> expected = createQuantity((short) 1001, MetricPrefix.MILLI(Units.OHM));
    assertEquals(expected, actual);
  }

  /**
   * Verifies that adding a quantity with a larger overflowing multiple casts the result to the larger multiple.
   */
  @Test
  public void additionWithLargerOverflowingMultipleCastsToLargerMultiple() {
    Quantity<ElectricResistance> actual = ONE_OHM.add(ONE_YOTTAOHM);
    assertEquals(ONE_YOTTAOHM, actual);
  }

  /**
   * Verifies that adding a quantity with a larger multiple resulting in an overflowing sum casts the result to the larger multiple.
   */
  @Test
  public void additionWithLargerMultipleAndOverflowingResultCastsToLargerMultiple() {
    ShortQuantity<ElectricResistance> almost_max_value_ohm = createQuantity((short) (Short.MAX_VALUE - 999), Units.OHM);
    Quantity<ElectricResistance> actual = almost_max_value_ohm.add(ONE_KILOOHM);
    ShortQuantity<ElectricResistance> expected = createQuantity((short) (Short.MAX_VALUE / 1000), MetricPrefix.KILO(Units.OHM));
    assertEquals(expected, actual);
  }

  /**
   * Verifies that adding a quantity with a larger multiple resulting in an overflowing sum casts the result to the larger multiple.
   */
  @Test
  public void additionWithLargerMultipleButNotOverflowingResultKeepsSmallerMultiple() {
    ShortQuantity<ElectricResistance> almost_max_value_ohm = createQuantity((short) (Short.MAX_VALUE - 1000), Units.OHM);
    Quantity<ElectricResistance> actual = almost_max_value_ohm.add(ONE_KILOOHM);
    assertEquals(MAX_VALUE_OHM, actual);
  }

  /**
   * Verifies that adding a quantity with a smaller underflowing multiple keeps the result at the larger multiple.
   */
  @Test
  public void additionWithSmallerUnderflowingMultipleKeepsAtLargerMultiple() {
    Quantity<ElectricResistance> actual = ONE_YOTTAOHM.add(ONE_OHM);
    assertEquals(ONE_YOTTAOHM, actual);
  }

  /**
   * Verifies that subtraction subtracts the argument from the target object.
   */
  @Test
  public void subtractionSubtractsArgumentFromTargetObject() {
    Quantity<ElectricResistance> actual = TWO_OHM.subtract(ONE_OHM);
    assertEquals(ONE_OHM, actual);
  }
  
  
  @Test
  public void divideTest() {
    ShortQuantity<ElectricResistance> quantity1 = new ShortQuantity<>(Short.valueOf("3").shortValue(), Units.OHM);
    ShortQuantity<ElectricResistance> quantity2 = new ShortQuantity<>(Short.valueOf("2").shortValue(), Units.OHM);
    Quantity<?> result = quantity1.divide(quantity2);
    assertEquals(Integer.valueOf("1"), result.getValue());
  }

  @Test
  public void multiplyQuantityTest() {
    ShortQuantity<ElectricResistance> quantity1 = new ShortQuantity<>(Short.valueOf("3").shortValue(), Units.OHM);
    ShortQuantity<ElectricResistance> quantity2 = new ShortQuantity<>(Short.valueOf("2").shortValue(), Units.OHM);
    Quantity<?> result = quantity1.multiply(quantity2);
    assertEquals(6, result.getValue());
  }

  @Test
  public void longValueTest() {
    ShortQuantity<Time> day = new ShortQuantity<Time>(Short.valueOf("3").shortValue(), Units.DAY);
    long hours = day.longValue(Units.HOUR);
    assertEquals(72L, hours);
  }

  @Test
  public void doubleValueTest() {
    ShortQuantity<Time> day = new ShortQuantity<Time>(Short.valueOf("3").shortValue(), Units.DAY);
    double hours = day.doubleValue(Units.HOUR);
    assertEquals(72D, hours);
  }

  @Test
  public void toTest() {
    Quantity<Time> day = Quantities.getQuantity(1D, Units.DAY);
    Quantity<Time> hour = day.to(Units.HOUR);
    assertEquals(hour.getValue().intValue(), 24);
    assertEquals(hour.getUnit(), Units.HOUR);

    Quantity<Time> dayResult = hour.to(Units.DAY);
    assertEquals(dayResult.getValue().intValue(), day.getValue().intValue());
    assertEquals(dayResult.getValue().intValue(), day.getValue().intValue());
  }

  @Test
  public void testEquality() throws Exception {
    Quantity<Length> value = Quantities.getQuantity(Short.valueOf("1"), Units.METRE);
    Quantity<Length> anotherValue = Quantities.getQuantity(Short.valueOf("1"), Units.METRE);
    assertEquals(value, anotherValue);
  }
}
