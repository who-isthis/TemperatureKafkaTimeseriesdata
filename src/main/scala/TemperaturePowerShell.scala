import com.profesorfalken.jpowershell.PowerShell

object TemperaturePowerShell extends App {
  val pwrShl = PowerShell.openSession
  while(true){
    val temperatureResponse = pwrShl.executeCommand("Get-WmiObject -Class Win32_PerfFormattedData_Counters_ThermalZoneInformation |Select-Object Name,Temperature").getCommandOutput
    println(s"Response ${temperatureResponse}")
  }

  pwrShl.close()

}
