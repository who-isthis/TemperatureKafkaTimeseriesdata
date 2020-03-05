problems faced:
how to configure kafka
apply in scala 
how to pass parameter in apply function and it's use cases.
---
 If i have just created the kafka-producer it will not work.
 
 To connect a kafka code base required a broker which is nothing but servers can say kafka server.
 To manage between brokers we need zookeeper. Without the zookeeper kafka server can not start. it will say "node not exits".
 
 Let's say in my code i just kept the kafka send record to Producer. To see the messages i need one command 
 kafka-console-consumer.sh 
    --bootstrap-server localhost:9092 -- topic my-topic
As because once the kafka will produce the message the consumer is available.


---
Kafka is doing lots of loading into offset need to check what is that. 
What is kafka **consumer group** and it's job and **consumer offset**.
<br>
-----
Problem 1

Now when i am stoping the kafka-console-consumer and restarting again it's not reading the consecutive messages.
<br>That might be issue of some offset .
from book <br>
**Each message in a given partition has a unique offset. By storing the offset of the last consumed message for each partition, either in Zookeeper or in Kafka itself, a consumer can stop and restart without losing its place.** 
<br> This can be solved via offset management . TODO Read it and implement this.

-----



