/*
import com.profesorfalken.jsensors.JSensors
import scala.collection.JavaConversions._
object ReadTemperature extends App{
  val components = JSensors.get.components()
  val listComp = components.cpus
  listComp.foreach{
    cpuComp =>
      if(cpuComp!= null){
        println(s" CPU component ${cpuComp.name}")
        if(cpuComp.sensors != null){
          val sensors = cpuComp.sensors
          println(s" sensors.temperatures are0")
          sensors.temperatures.foreach{
            tmp =>
              println(s"temp name ${tmp.name}")
              println(s"temp value ${tmp.value}")
          }


        }

      }

  }
}
*/
