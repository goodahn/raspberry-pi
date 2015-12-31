#Write image to sd card
##1. Download OS  
  If you are not familiar with raspberry-pi download NOOBS.  
  You can download NOOBS at  
  * https://www.raspberrypi.org/  
  
##2. Format your sd card  
  I recommand using SDFormatter
  * https://www.sdcard.org/  
  
##3. Write an image to sd card  
* If you download NOOBS  
    You just unzip and paste unzipped file to sd card.  
  
* Else  
    * If your OS is WINDOW  
      Download Win32DiskImager and write image to sdcard.  
  
    * If your OS is Linux  
      Use `dd` command  
      -It's very dangerous because dd can overwrite any partition of your machine  
  
      Type `"df -h"`  
      and insert sd card.  
      Type `"df -h"` agian.  
      New device that has appeared is your sd card.  
      If your sd card shows up more than once in the output of `"df -h"`,  
      it means your raspberry pi has multiple partition.  
      You need to unmount all of them( for example, `"unmount /dev/sdcard"` ).  
      Now you can write image to sd card.  
      Type `"dd bs=4M if='path of image' of='path of your sd card'"`  
  
      `dd` commands doesn't give any information of process. you can use `dcfldd`  


