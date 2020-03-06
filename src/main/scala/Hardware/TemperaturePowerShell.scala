package Hardware

import java.time.LocalDateTime

import com.profesorfalken.jpowershell.PowerShell
import kafka.TempReaderProducer
import kafka.SensorData

object TemperaturePowerShell extends App {
  val pwrShl = PowerShell.openSession

  val kfkProducer = new TempReaderProducer
  while (true) {


    val temperatureResponse = pwrShl.executeCommand("Get-WmiObject -Class Win32_PerfFormattedData_Counters_ThermalZoneInformation |%{ \"{0,-10}{1,-1}\" -f $_.Name,$_.Temperature}").getCommandOutput
    temperatureResponse.split("\n").foreach {
      ln =>
        val arr = ln.split(" ")
        val sensorName = s"${arr(0)}  ${LocalDateTime.now()}"
        val temperature = arr(1)
        //println(sensorName)
        //println(temperature)
        val senseData = SensorData(sensorName, temperature)

        kfkProducer.addRecod(senseData)


    }
  }
  kfkProducer.closeProducer


  pwrShl.close()

}
