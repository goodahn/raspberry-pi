import bluetooth
import time

server_sock = bluetooth.BluetoothSocket( bluetooth.RFCOMM )
server_sock.bind( ( "", bluetooth.PORT_ANY ) )
server_sock.listen(1)

message_size=0

port = server_sock.getsockname()[1]
uuid = "00001101-0000-1000-8000-00805F9B34FB"

bluetooth.advertise_service( server_sock, "SampleServer",
                             service_id = uuid,
                             service_classes = [ uuid, bluetooth.SERIAL_PORT_CLASS ],
                             profiles = [ bluetooth.SERIAL_PORT_PROFILE, ], )

print "waiting for connection %d" %port

client_sock, address = server_sock.accept()

print "Accepted connection from ", address
try:
    while True:
        data = client_sock.recv(4096).replace('\0','')
        print len(data)
        parsed_data = data.split(":")
        if parsed_data[0] == "CheckStart":
            i=0
            #print "Start Checking!"
            start = time.time()
            message_size = int(parsed_data[1])
            print "message size is %d" % message_size
            send_data = message_size*'a'
            client_sock.send( send_data )
        elif parsed_data[0] == "end":
            #print "Bye Bye~"
            break
        elif parsed_data[0] =="CheckEnd":
            end = time.time()
            #print "End Checking!"
            print "total time %f, total count %d" % (end - start,i)
            message_size=0
            client_sock.send(data)
        else:
            i=1
            #print data[:6]
            #if len(data) != message_size:
            #    print "data leaking ",len(data),data
            #client_sock.send(data)

except IOError:
    print "IOError has occured!\n"

client_sock.close()
server_sock.close()
