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

import java.io.File
import java.sql.DriverManager

class Generator {
  companion object {
    private const val MIN_EXPECTED_DEVICES = 20_000
    private const val DATABASE_PATH = "database/android-devices.db"

    // Google's list only shrinks slowly, so a larger drop than this means a broken
    // download or a CSV layout change, and it must never be released automatically.
    private const val MAX_DROP_FRACTION = 0.10

    @JvmStatic
    fun main(args: Array<String>) {
      // Get the devices supported by Google Play and create the database
      val devices = Devices.get()
      // Guard against silently generating an empty/truncated database if Google changes
      // the CSV layout or the download is incomplete.
      check(devices.size >= MIN_EXPECTED_DEVICES) {
        "Only ${devices.size} devices were parsed (expected at least $MIN_EXPECTED_DEVICES); refusing to generate database"
      }
      val previous = previousDeviceCount()
      if (previous != null) {
        check(devices.size >= previous * (1 - MAX_DROP_FRACTION)) {
          "Only ${devices.size} devices were parsed, down from $previous; refusing to generate database. " +
              "If the drop is genuine, regenerate locally and commit the database by hand."
        }
      }
      println("Parsed ${devices.size} devices" + (previous?.let { " (previously $it)" } ?: ""))
      DatabaseGenerator(devices, DATABASE_PATH).generate()
    }

    private fun previousDeviceCount(): Int? {
      if (!File(DATABASE_PATH).isFile) return null
      DriverManager.getConnection("jdbc:sqlite:$DATABASE_PATH").use { conn ->
        conn.createStatement().use { statement ->
          statement.executeQuery("SELECT COUNT(*) FROM devices").use { rows ->
            return if (rows.next()) rows.getInt(1) else null
          }
        }
      }
    }
  }
}