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
```

##3.bluetooth connection  
type  
```
python bluetooth_rfcomm.py  
```
Now your raspberry pi open bluetooth server.  
  
Use `android/Test_BLE_RFCOMM` to test raspberry pi's server on your android.  
#Fix your Error  
If `bluetooth.btcommon.bluetooth error (2 'no such file or directory')` error occured,  
type `systemctl status bluetooth`  
and find line which contains `Loaded: loaded:( path_of_your_bluetooth.service )`  
Open your bluetooth.service and find line which contains `Exec` and add `--compat`  
