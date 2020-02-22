package com.ALuniv25.MotionLighting.ReadingData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;
import com.ALuniv25.MotionLighting.ReadingData.DataStore.arduinoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.ALuniv25.MotionLighting.ReadingData.DataStore.arduinoRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class SerialTest implements SerialPortEventListener {

	public static arduinoResponse response;
	SerialPort serialPort;
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
                        "/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM7", // Windows
			"COM5", // Windows
			"COM3", // Windows
	};
	private BufferedReader input;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;

	public void initialize() {

		CommPortIdentifier portId = null;
		final Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		while (portEnum.hasMoreElements()) {
			final CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (final String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (final Exception e) {
			System.err.println("PortId Error: " + e.getMessage());
		}
	}

	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	public synchronized void serialEvent(final SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				final String inputLine = input.readLine();
				final String[] data = inputLine.split("-", 3);
				response = new arduinoResponse(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
						Integer.parseInt(data[2]));
			} catch (final Exception e) {
				System.err.println(e.toString());
			}
		}
	}

	public static void setTimeout(final Runnable runnable, final int delay) {
		new Thread(() -> {
			try {
				Thread.sleep(delay);
				runnable.run();
			} catch (final Exception e) {
				System.err.println(e);
			}
		}).start();
	}

	@MessageMapping("/hello")
	@SendTo("/topic/dataStream")
	public arduinoResponse DataStream(final arduinoRequest message) throws Exception {
		final SerialTest st = new SerialTest();
		st.initialize();

		setTimeout(() -> {
			System.out.println(response.toString());
		}, 1000);

		return response;
	}

	@PostMapping("ManualLight/{b}")
	public void ManualLight(@PathVariable int b){
		SerialPort serialPort;
		SerialTest st= new SerialTest();
		OutputStream output;
		try {
			CommPortIdentifier portId = null;
			final Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
	
			while (portEnum.hasMoreElements()) {
				final CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
				for (final String portName : PORT_NAMES) {
					if (currPortId.getName().equals(portName)) {
						portId = currPortId;
						break;
					}
				}
			}
			if (portId == null) {
				System.out.println("Could not find COM port.");
				return;
			}

			serialPort = (SerialPort) portId.open(st.getClass().getName(), TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			output = serialPort.getOutputStream();
			byte c = 0 ;
			if (b == 1) {
				c=1;
			}else{
				if (b==2) {
					c=2;
				}
			}
			output.write(c);			
			close();
		} catch (final Exception e) {;
			System.err.println("ManualLight Error: " + e.getMessage());
		}
	}

	@GetMapping("LedOff")
	public void LightOff(){
		SerialPort serialPort;
		SerialTest st= new SerialTest();
		OutputStream output;
		try {
			CommPortIdentifier portId = null;
			final Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
	
			while (portEnum.hasMoreElements()) {
				final CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
				for (final String portName : PORT_NAMES) {
					if (currPortId.getName().equals(portName)) {
						portId = currPortId;
						break;
					}
				}
			}
			if (portId == null) {
				System.out.println("Could not find COM port.");
				return;
			}
			serialPort = (SerialPort) portId.open(st.getClass().getName(), TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			output = serialPort.getOutputStream();
			byte c = 2;
			output.write(c);
			close();
		} catch (final Exception e) {
			System.err.println("LightOff Error: " + e.getMessage());
		}
	}

}

