/*
 * Copyright (C) 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.teleport.v2.values;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/** Test cases for the {@link FailsafeElement} class. */
public class FailsafeElementTest {

  /** Tests a {@link FailsafeElement} can be created and cloned via it's builder. */
  @Test
  public void testEquals() {
    FailsafeElement<String, Integer> element =
        FailsafeElement.of("answer", 42).setErrorMessage("failed!").setStacktrace("com.google...");

    // Primitive types
    assertThat(
        element,
        is(
            not(
                equalTo(
                    FailsafeElement.of("answer", 42)
                        .setErrorMessage("failed!")
                        .setStacktrace("com.google...")))));
    assertThat(
        element,
        is(
            equalTo(
                FailsafeElement.of("answer", 1)
                    .setErrorMessage("failed!")
                    .setStacktrace("com.google..."))));

    // Copy
    assertThat(element, is(equalTo(FailsafeElement.of(element))));
  }
}
