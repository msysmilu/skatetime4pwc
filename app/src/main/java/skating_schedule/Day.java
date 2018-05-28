
package skating_schedule;

import java.util.List;

public class Day{
   	private String id;
   	private List scheduleEntry;

 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public List getScheduleEntry(){
		return this.scheduleEntry;
	}
	public void setScheduleEntry(List scheduleEntry){
		this.scheduleEntry = scheduleEntry;
	}
}
