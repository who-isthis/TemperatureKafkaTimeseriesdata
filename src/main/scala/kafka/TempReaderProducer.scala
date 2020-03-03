package kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class TempReaderProducer {

  lazy val kafkaProducer = new KafkaProducer[String,String](createConfiguration)

  def apply(): KafkaProducer[String, String] = {
    new TempReaderProducer()
    kafkaProducer
  }

  private def createConfiguration ={
    val props = new Properties()
    props.put("bootstrap.servers","localhost:9042")
    props.put("acks","all")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props
  }

  def addRecod(senseData : SensorData)={
    val record: ProducerRecord[String, String] = new ProducerRecord[String,String]("my-topic",senseData.sensorName,senseData.sensorTemperature)
    kafkaProducer.send(record)
  }

  def closeProducer={
    kafkaProducer.close()
  }
}
