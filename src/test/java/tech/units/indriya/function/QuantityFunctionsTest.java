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
package tech.units.indriya.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.measure.Quantity;
import javax.measure.quantity.Temperature;
import javax.measure.spi.QuantityFactory;
import javax.measure.spi.ServiceProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tech.units.indriya.function.QuantityFunctions;
import tech.units.indriya.unit.Units;

public class QuantityFunctionsTest {

  private QuantityFactory<Temperature> tempFactory;
  private Quantity<Temperature> temp1;
  private Quantity<Temperature> temp2;
  
  @BeforeEach
  public void init() {
    final ServiceProvider factoryService = ServiceProvider.current();
    tempFactory = factoryService.getQuantityFactory(Temperature.class);
    temp1 = tempFactory.create(1, Units.CELSIUS);
    temp2 = tempFactory.create(1, Units.KELVIN);
  }

  @Test
  public void sumTemperatureUnitTestC() {
    final List<Quantity<Temperature>> temps = new ArrayList<>(getList());
    final Quantity<Temperature> sumTemp = temps.stream().reduce(QuantityFunctions.sum(Units.CELSIUS)).get();
    assertEquals(Units.CELSIUS, sumTemp.getUnit());
    assertEquals(BigDecimal.valueOf(-271.15), sumTemp.getValue());
  }

  
  @Test
  public void sumTemperatureUnitTestK() {
    final List<Quantity<Temperature>> temps = new ArrayList<>(getList());
    final Quantity<Temperature> sumTemp = temps.stream().reduce(QuantityFunctions.sum(Units.KELVIN)).get();
    assertEquals(Units.KELVIN, sumTemp.getUnit());
    assertEquals(Double.valueOf(275.15), sumTemp.getValue());
  }
  
  @Test
  public void sumTemperatureUnitTestK2C() {
    final List<Quantity<Temperature>> temps = new ArrayList<>(getList());
    final Quantity<Temperature> sumTemp = temps.stream().reduce(QuantityFunctions.sum(Units.KELVIN)).get();
    assertEquals(Units.KELVIN, sumTemp.getUnit());
    assertEquals(2.0, sumTemp.to(Units.CELSIUS).getValue());
  }
  
  private List<Quantity<Temperature>> getList() {
    return Arrays.asList(temp1, temp2);
  }
}
