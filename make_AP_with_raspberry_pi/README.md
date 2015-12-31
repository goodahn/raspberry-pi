#make AP with raspberry pi
##what you need
First, you need WiFi adapter and Ethernet cable  
type `iw list` if you don't have iw then type `sudo apt-get install iw`  
if result of `iw list` doesn't have  
        Supported interface modes:  
        * AP  
        * AP/VLAN  
then your WiFi adapter can't do AP so get new WiFi adapter  
##install software
you need hostapd and udhcpd  
type `sudo apt-get install hostapd udhcpd -y`  
##configure
you need to configure `/etc/udhcpd.conf`  
##1.  
you should edit /etc/udhcpd.con contain  
```        start 192.168.42.2  
        end 192.168.42.20  
        interface wlan0  
        remaining yes  
        opt dns 8.8.8.8 4.2.2.2  
        opt subnet 255.255.255.0  
        opt router 192.168.42.1  
        opt lease 864000   ```
##2.  
Edit /etc/default/udhcpd  
change the line `DHCPD_ENABLED="no"` to `DCHPD_ENABLED="yes"`  
##3.  
Now, make your raspberry pi has static ip address.  
Type `sudo ipconfig wlan0 192.168.42.1`  
If you want to make it automatically, you should edit `/etc/network/interfaces`  
Change your  
```        allow-hotplug wlan0  
        wpa-roam /etc/wpa_supplicant/wpa_supplicant.conf  
        iface default inet manual  ```
To  
```        iface wlan0 inet static  
        address 192.168.42.1  
        netmask 255.255.255.0  
        #allow-hotplug wlan0  
        #wpa-roam /etc/wpa_supplicant/wpa_supplicant.conf  
        #iface default inet manual  ```
##4.  
You should edit configuration of hostapd which will make network.  
Maybe you will make /etc/hostapd/hostapd.conf.  
```        interface=wlan0  
        driver=nl80211  
        ssid=Test_AP  
        hw_mode=g  
        channel=6  
        macaddr_acl=0  
        auth_algs=1  
        ignore_broadcast_ssid=0  
        wpa=2  
        wpa_passphrase=My_Passphrase  
        wpa_key_mgmt=WPA-PSK  
        wpa_pairwise=TKIP  
        rsn_pairwise=CCMP  ```
`driver` is your WiFi adapter's driver. If you use realtek your driver should be rtl871xdrv  
It depends on your WiFi adapter.  
`wpa_passphrase` is password of your AP.  
And then you should edit /etc/default/hostapd  
Change the line  
        #DAEMON_CONF=""
To  
        DAEMON_CONF=/etc/hostapd/hostapd.conf  
##5.
Need to configure network address translation.  
Add `net.ipv4.ip_forward=1` end of `/etc/sysctl.conf`.  
Type  
```        sudo iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE  
        sudo iptables -A FORWARD -i eth0 -o wlan0 -m state --state RELATED,ESTABLISHED -j ACCEPT  
        sudo iptables -A FORWARD -i wlan0 -o eth0 -j ACCEPT  ```
If you want to make it automatically type  
`sudo sh -c "iptables-save > /etc/iptables.ipv4.nat"`  
and add `up iptables-restore < /etc/iptables.ipv4.nat` to `/etc/network/interfaces`  
##6.
It's time to make AP!  
type  
```        sudo service hostapd start  
        sudo service udhcpd start  ```
you can see `Test_AP` is appeared in your WiFi pannel.  

