import bluetooth
import socket
import time


#########################################################
#                 bluetooth socket                      #
#########################################################

server_sock = bluetooth.BluetoothSocket( bluetooth.RFCOMM )

server_sock.bind(("",bluetooth.PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]
uuid = #YOUR UUID#

bluetooth.advertise_service( server_sock, "SampleServer",
                             service_id = uuid,
                             service_classes = [ uuid, bluetooth.SERIAL_PORT_CLASS ],
                             profiles = [ bluetooth.SERIAL_PORT_PROFILE ],
                             #protocols = [ OBEX_UUID ]
                             )

print "waiting for connnection %d" %port

client_sock, address = server_sock.accept()
print "Accepted connection from ",address

try:
    while True:
        data = client_sock.recv(1024)
        print "received [%s]" %data

except IOError:
    print "IOError occured!"

s.close()
client_sock.close()
server_sock.close()
print "all done"
