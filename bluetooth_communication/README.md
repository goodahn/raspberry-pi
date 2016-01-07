#bluetooth communication
##1.install software
type
```
sudo apt-get install bluetooth blueman bluez -y
```

##2.pairing device
type
```
bluetoothctl
pairable on
discoverable on
scan on
pair 00:00:00:00:00:00 <- address of your device
'''

##3.bluetooth connection  
type  
'''
python bluetooth_rfcomm.py  
```
Now your raspberry pi open bluetooth server.  
  
use bluetooth-client to check bluetooth connection.  
