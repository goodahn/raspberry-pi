# make BLE audio receiver  
##Install Software  
You need to download Software!  
Type `sudo apt-get install bluez pulseaudio-module-bluetooth python-gobject python-gobject-2`  
##Change bluetooth configure  
Type `usermod -a -G lp pi`  
If you don't have `/etc/bluetooth/audio.conf` then make it.  
```
[General]  
Enable=Source,Media,Socket,Sink  
```
Maybe your audio.conf is like above.    
Now edit `/etc/pulse/daemon.conf`. Add the `resample-method=trivial`  
It's time to reboot!  
##Paring and connect your device  
Type  
```
sudo hcitool hci0 piscan  
bluetoothctl  
discoverable on  
pairable on  
scan on  
pair ADDRESS_OF_YOUR_AUDIO_DEVICE  
connect ADDRESS_OF_YOUR_AUDIO_DEVICE  
```  
If raspberry pi is not shown as audio device, reboot raspberry pi and find it again.  
##Change audio sink from hdmi to jack  
result of `pactl list sources short` will be  
```
0    alsa_output.0.analog-stereo.monitor    module-alsa-card.c    s16le 2ch 44100Hz    SUSPENDED  
1    bluez_source.ADDRESS_OF_YOUR_DEVICE    module-bluetooth-device.c    s16le 2ch 44100Hz    SUSPENDED  
```
Then type  
```
pactl load-module module-loopback source=blue_source.ADDRESS_OF_YOUR_DEVICE sink=alsa_ouput.0.analog-stereo.monitor  
amixer cset numid=3 1  
```
Now your music is redirected to jack output of raspberry pi.  
Congratulation!  
