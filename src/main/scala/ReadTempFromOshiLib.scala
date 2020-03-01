import oshi.hardware.CentralProcessor
import oshi.hardware.CentralProcessor.TickType
import oshi.hardware.ComputerSystem
import oshi.hardware.Display
import oshi.hardware.GlobalMemory
import oshi.hardware.HWDiskStore
import oshi.hardware.HWPartition
import oshi.hardware.HardwareAbstractionLayer
import oshi.hardware.NetworkIF
import oshi.hardware.PhysicalMemory
import oshi.hardware.PowerSource
import oshi.hardware.Sensors
import oshi.hardware.SoundCard
import oshi.hardware.UsbDevice
import oshi.hardware.VirtualMemory
import oshi.software.os.FileSystem
import oshi.software.os.NetworkParams
import oshi.software.os.OSFileStore
import oshi.software.os.OSProcess
import oshi.software.os.OSService
import oshi.software.os.OperatingSystem
import oshi.software.os.OperatingSystem.ProcessSort
import oshi.util.FormatUtil
import oshi.util.Util
import oshi.hardware.ComputerSystem
import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer
import java.time.Instant
import java.util
import oshi.SystemInfo;
object ReadTempFromOshiLib extends App {

  //https://github.com/oshi/oshi/blob/master/oshi-core/src/test/java/oshi/SystemInfoTest.java

  val si: SystemInfo = new SystemInfo
  var oshi = new ArrayBuffer[String]()

  val hal: HardwareAbstractionLayer = si.getHardware
  val os: OperatingSystem = si.getOperatingSystem

  printOperatingSystem(os)
  printComputerSystem(hal.getComputerSystem)
  printProcessor(hal.getProcessor)

  printMemory(hal.getMemory)


  printCpu(hal.getProcessor)

  //printProcesses(os, hal.getMemory)

  private def printOperatingSystem(os:OperatingSystem ) {
    oshi.add(String.valueOf(os));
    oshi.add("Booted: " + Instant.ofEpochSecond(os.getSystemBootTime()));
    oshi.add("Uptime: " + FormatUtil.formatElapsedSecs(os.getSystemUptime()));
    //oshi.add("Running with" + (os.isElevated() ? "" : "out") + " elevated permissions.");
  }




  private def printComputerSystem(computerSystem: ComputerSystem): Unit = {
    oshi.add("===================Checking computer system ====================")
    oshi.add("system: " + computerSystem.toString)
    oshi.add(" firmware: " + computerSystem.getFirmware.toString)
    oshi.add(" baseboard: " + computerSystem.getBaseboard.toString)
  }



  private def printProcessor(processor: CentralProcessor): Unit = {
    oshi.add("===================Checking Processor=========================================")
    oshi.add(processor.toString)
  }


  private def printMemory(memory: GlobalMemory): Unit = {
    oshi.add("=================Checking Memory ======================")
    oshi.add("Memory: \n " + memory.toString)
    val vm = memory.getVirtualMemory
    oshi.add("Swap: \n " + vm.toString)
    val pmArray = memory.getPhysicalMemory
    if (pmArray.length > 0) {
      oshi.add("Physical Memory: ")
      for (pm <- pmArray) {
        oshi.add(" " + pm.toString)
      }
    }
  }


  import java.util

  private def printCpu(processor: CentralProcessor): Unit = {
    oshi.add("======================== Checking CPU =====================")
    oshi.add("Context Switches/Interrupts: " + processor.getContextSwitches + " / " + processor.getInterrupts)
    val prevTicks = processor.getSystemCpuLoadTicks
    val prevProcTicks = processor.getProcessorCpuLoadTicks
    oshi.add("CPU, IOWait, and IRQ ticks @ 0 sec:" + util.Arrays.toString(prevTicks))
    // Wait a second...
    Util.sleep(1000)
    val ticks = processor.getSystemCpuLoadTicks
    oshi.add("CPU, IOWait, and IRQ ticks @ 1 sec:" + util.Arrays.toString(ticks))
    val user = ticks(TickType.USER.getIndex) - prevTicks(TickType.USER.getIndex)
    val nice = ticks(TickType.NICE.getIndex) - prevTicks(TickType.NICE.getIndex)
    val sys = ticks(TickType.SYSTEM.getIndex) - prevTicks(TickType.SYSTEM.getIndex)
    val idle = ticks(TickType.IDLE.getIndex) - prevTicks(TickType.IDLE.getIndex)
    val iowait = ticks(TickType.IOWAIT.getIndex) - prevTicks(TickType.IOWAIT.getIndex)
    val irq = ticks(TickType.IRQ.getIndex) - prevTicks(TickType.IRQ.getIndex)
    val softirq = ticks(TickType.SOFTIRQ.getIndex) - prevTicks(TickType.SOFTIRQ.getIndex)
    val steal = ticks(TickType.STEAL.getIndex) - prevTicks(TickType.STEAL.getIndex)
    val totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal
    //oshi.add(String.format("User: %.1f%% Nice: %.1f%% System: %.1f%% Idle: %.1f%% IOwait: %.1f%% IRQ: %.1f%% SoftIRQ: %.1f%% Steal: %.1f%%",
     // 100d * user / totalCpu, 100d * nice / totalCpu, 100d * sys / totalCpu, 100d * idle / totalCpu, 100d * iowait / totalCpu, 100d * irq / totalCpu, 100d * softirq / totalCpu, 100d * steal / totalCpu))
    oshi.add(s"user: ${100d * user / totalCpu}")
    /*oshi.add(String.format("CPU load: %.1f%%", processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100))
    val loadAverage = processor.getSystemLoadAverage(3)
    oshi.add("CPU load averages:" + (if (loadAverage(0) < 0) " N/A"
    else String.format(" %.2f", loadAverage(0))) + (if (loadAverage(1) < 0) " N/A"
    else String.format(" %.2f", loadAverage(1))) + (if (loadAverage(2) < 0) " N/A"
    else String.format(" %.2f", loadAverage(2))))
    // per core CPU
    val procCpu = new StringBuilder("CPU load per processor:")
    val load = processor.getProcessorCpuLoadBetweenTicks(prevProcTicks)
    for (avg <- load) {
      procCpu.append(String.format(" %.1f%%", avg * 100))
    }
    oshi.add(procCpu.toString)
    var freq = processor.getProcessorIdentifier.getVendorFreq
    if (freq > 0) oshi.add("Vendor Frequency: " + FormatUtil.formatHertz(freq))
    freq = processor.getMaxFreq
    if (freq > 0) oshi.add("Max Frequency: " + FormatUtil.formatHertz(freq))
    val freqs = processor.getCurrentFreq
    if (freqs(0) > 0) {
      val sb = new StringBuilder("Current Frequencies: ")
      var i = 0
      while ( {
        i < freqs.length
      }) {
        if (i > 0) sb.append(", ")
        sb.append(FormatUtil.formatHertz(freqs(i)))

        {
          i += 1; i - 1
        }
      }
      oshi.add(sb.toString)*/
    }



  /*private def printProcesses(os: OperatingSystem, memory: GlobalMemory): Unit = {
    oshi.add("============================ Checking Processes================================")
    oshi.add("My PID: " + os.getProcessId + " with affinity " + os.getProcessAffinityMask(os.getProcessId))
    oshi.add("Processes: " + os.getProcessCount + ", Threads: " + os.getThreadCount)
    // Sort by highest CPU
    val procs = util.Arrays.asList(os.getProcesses(5, ProcessSort.CPU))
    oshi.add("   PID  %CPU %MEM       VSZ       RSS Name")
    var i = 0
    while ( {
      i < procs.size && i < 5
    }) {
      val p = procs.get(i)
      oshi.add(String.format(" %5d %5.1f %4.1f %9s %9s %s", p.getProcessID, 100d * (p.getKernelTime + p.getUserTime) / p.getUpTime, 100d * p.getResidentSetSize / memory.getTotal, FormatUtil.formatBytes(p.getVirtualSize), FormatUtil.formatBytes(p.getResidentSetSize), p.getName))

      {
        i += 1; i - 1
      }
    }
  }*/

  oshi.foreach(println)
}
