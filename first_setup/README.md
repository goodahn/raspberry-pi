#write image to sd card
##1. download OS  
  If you are not familiar with raspberry-pi download NOOBS.  
  you can download NOOBS at  
  * https://www.raspberrypi.org/  
  
##2. format your sd card  
  i recommand using SDFormatter
  * https://www.sdcard.org/  
  
##3. write an image to sd card  
  * if you download NOOBS  
    you just unzip and paste unzipped file to sd card.  
  
  * else  
    * if your OS is WINDOW  
      download Win32DiskImager and write image to sdcard.  
  
    * if your OS is Linux  
      use `dd` command  
      -it's very dangerous because dd can overwrite any partition of your machine  
  
      type `"df -h"`  
      and insert sd card  
      type `"df -h"` agian  
      new device that has appeared is your sd card.  
      if your sd card shows up more than once in the output of `"df -h"`,  
      it means your raspberry pi has multiple partition.  
      you need to unmount all of them( for example, `"unmount /dev/sdcard"` ).  
      now you can write image to sd card.  
      type `"dd bs=4M if='path of image' of='path of your sd card'"`  
  
      `dd` commands doesn't give any information of process. you can use `dcfldd`  


