/*
 * TvServer
 * Simple TCP to Serial server
 * Karmel Al Labadi
 * Smart Design LLC.
 * Oct 2015
*/
import java.io.*; 
import java.net.*; 

import jssc.SerialPort;
import jssc.SerialPortException;

class TvServer 
{    


	// two dimensions, first is incoming tcp command (from VLC), 
	// second is the serial command (to TV)
	// to be extended, it is best to save sommands in an external file 
	// to be edited on the go without compiling
	// "tvOn => 0x02 + "PON" + 0x03
	// "tvOff => 0x02 + "POF" + 0x03
	static String commands[][] = { {"tvOn", "i1\r"}, {"tvOff", "w\r"}};
public byte[] getCommandPrefix()
{	
	byte[] x = {0x02};
	return x;
}

public byte[] getCommandPostfix()
{	
	byte[] x = {0x03};
	return x;
}


public static void main(String argv[]) throws Exception       
{          
	ServerSocket tcpSocket = null;
	SerialPort serialPort = null;

	String clientCommand;  
	int portNumber = 9991;        
	String serialPortName = "/dev/tty.usbmodemfd121";
	try 
	{        
		 tcpSocket = new ServerSocket(portNumber);          
		 serialPort = new SerialPort(serialPortName);
	}
    catch (Exception e)
    {
            System.out.println("cant listen on port " + portNumber);
            System.out.println(e);
    }
		
	System.out.println("tcp server running"); 
	 
	while(true)
	{
		try
		{
			// set up tcp server
			Socket connectionSocket = tcpSocket.accept(); 
			System.out.println("Client connected");       
			BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 
			//DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientCommand = in.readLine();
			
			in.close();
			connectionSocket.close();
			
			// Open serial port
           serialPort.openPort();
            // Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
            serialPort.setParams(SerialPort.BAUDRATE_9600, 
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE); 
			
			System.out.println("Received: " + clientCommand);    
			if (clientCommand != null)
			{
				for (int i = 0; i < commands.length; i++)
				{
					// check if command exists
					if (clientCommand.equals(commands[i][0]))
					{
						//System.out.println(commands[i][1]);  
						byte[] buffer = serialPort.readBytes(100); 
						//Write data to port
						// Thread.sleep(1000);
						serialPort.writeBytes(commands[i][1].getBytes()); 

						//buffer = serialPort.readBytes(20); 
						//System.out.println("serial: " + buffer);

						break;
					}
				}
			}
			System.out.println("Client disconnected");
			// Close serial port
            serialPort.closePort();
        }
        catch (SerialPortException e)
        {
        	System.out.println("cannot connect to serial port " + serialPortName);
        	e.printStackTrace();
        }
        catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
		}
	       
	} 
}

} // TvServer 