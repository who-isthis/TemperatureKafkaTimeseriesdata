package Hardware

import com.profesorfalken.jpowershell.PowerShell
import kafka.TempReaderProducer

object TemperaturePowerShell extends App {
  val pwrShl = PowerShell.openSession


    val temperatureResponse = pwrShl.executeCommand("Get-WmiObject -Class Win32_PerfFormattedData_Counters_ThermalZoneInformation |Select-Object Name,Temperature").getCommandOutput
    println(s"Response ${temperatureResponse}")
  val f = temperatureResponse.startsWith("\\_")
  println(s"first : name == ${f}")

    //val kfkProducer = new TempReaderProducer(temperatureResponse)



      pwrShl.close()

}
