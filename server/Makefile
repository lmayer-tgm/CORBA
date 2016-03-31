CXX            	= /usr/bin/g++
CPPFLAGS      	= -g -c
LDFLAGS       	= -g
OMNI_HOME     	= /root/Downloads/omniORB-4.2.1
OMNIIDL       	= $(OMNI_HOME)/build/bin/omniidl
LIBS          	= -lomniORB4 -lomnithread -lomniDynamic4
OBJECTS       	= echo_callbackSK.o server.o
IDL_DIR		= ../idl
IDL_FILE	= $(IDL_DIR)/echo_callback.idl

all server: $(OBJECTS)
	$(CXX) $(LDFLAGS) -o server server.o echo_callbackSK.o $(LIBS)

server.o: server.cc
	$(CXX) $(CPPFLAGS) server.cc -I.

echo_callbackSK.o: echo_callbackSK.cc echo_callback.hh
	$(CXX) $(CPPFLAGS) echo_callbackSK.cc -I.

echo_callbackSK.cc: $(IDL_FILE)
	$(OMNIIDL) -bcxx $(IDL_FILE)

run: server
	# Start Naming service with command 'omniNames -start -always' as root
	./server -ORBInitRef NameService=corbaname::localhost

clean clean-up:
	rm -rf *.o
	rm -rf *.hh
	rm -rf *SK.cc
	rm -rf server
