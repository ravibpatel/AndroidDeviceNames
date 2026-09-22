/*
 * Copyright (C) 2017 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jaredrummler.androiddevicenames

class Generator {
  companion object {
    private const val MIN_EXPECTED_DEVICES = 20_000

    @JvmStatic
    fun main(args: Array<String>) {
      // Get the devices supported by Google Play and create the database
      val devices = Devices.get()
      // Guard against silently generating an empty/truncated database if Google changes
      // the CSV layout or the download is incomplete.
      check(devices.size >= MIN_EXPECTED_DEVICES) {
        "Only ${devices.size} devices were parsed (expected at least $MIN_EXPECTED_DEVICES); refusing to generate database"
      }
      println("Parsed ${devices.size} devices")
      DatabaseGenerator(devices).generate()
    }
  }
}