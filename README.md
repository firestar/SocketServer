SocketServer
============

Example code at https://github.com/firestar/SocketServerTest


Quick Example
--

Quick encoding and reading example for string.

Server Side - send command (0x01,0x01,0x01) 1,1,1 [cubic byte commands]
```java
= Transmission.encodeMessage(new byte[]{0x01,0x01,0x01},"Hello world".getBytes());
Socket.getOutputStream().write(message);
Socket.getOutputStream().flush();
```

Client Side - receive command (0x01,0x01,0x01)
```java
int readBytes =0;
byte[] buffer = new byte[8*1024];
while((readBytes=socket.read(buffer))!=1){
  TransmissionMessage message = Transmission.decodeMessage(Arrays.copyOf(buffer,readBytes));
  if(message.getCommand()[0]==0x01 
    && message.getCommand()[1]==0x01 
    && message.getCommand()[2]==0x01)
  {
    String stringMessage = new String(message.getValue());
  }
}
```


http://Synload.com
