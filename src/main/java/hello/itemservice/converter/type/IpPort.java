package hello.itemservice.converter.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;

// ip <-> port 

@Getter
@EqualsAndHashCode // ? TODO: EqualAndHashCode 어노테이션은 무엇인지?
public class IpPort {
	
	private String ip;
	private int port;
	
	public IpPort(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
}	
