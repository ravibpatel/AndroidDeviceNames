package com.jaredrummler.androiddevicenames

import com.jsoizo.kotlincsv.csvReader
import com.jsoizo.kotlincsv.reader.readAll

object Devices {

  private const val URL = "https://storage.googleapis.com/play_public/supported_devices.csv"

  fun get(url: String = URL): List<Device> {
    // Without timeouts a stalled download hangs the CI job until GitHub kills it.
    val conn = java.net.URL(url).openConnection().apply {
      connectTimeout = 30_000
      readTimeout = 60_000
    }
    val bytes = conn.getInputStream().use { it.readBytes() }
    // A connection dropped mid-transfer can end the stream early without an error, which
    // would silently drop the devices at the end of the list.
    val expected = conn.contentLengthLong
    check(expected < 0 || bytes.size.toLong() == expected) {
      "Incomplete download: received ${bytes.size} of $expected bytes"
    }
    return csvReader().readAll(bytes.inputStream(), charset = "UTF-16")
      .drop(1) // skip header
      .filter { records -> records.size == 4 }
      .map { (manufacturer, name, code, model) -> Device(manufacturer, name, code, model) }
  }
}
