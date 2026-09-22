package com.jaredrummler.androiddevicenames

import com.jsoizo.kotlincsv.csvReader
import com.jsoizo.kotlincsv.reader.readAll

object Devices {

  private const val URL = "https://storage.googleapis.com/play_public/supported_devices.csv"

  fun get(url: String = URL): List<Device> {
    val conn = java.net.URL(url).openConnection()
    return conn.getInputStream().use { stream ->
      csvReader().readAll(stream, charset = "UTF-16")
        .drop(1) // skip header
        .filter { records -> records.size == 4 }
        .map { (manufacturer, name, code, model) -> Device(manufacturer, name, code, model) }
    }
  }
}
