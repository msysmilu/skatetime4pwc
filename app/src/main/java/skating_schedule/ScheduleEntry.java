
package skating_schedule;

import java.util.List;

public class ScheduleEntry{
   	private String id;
   	private String realName;
   	private String timeEnd;
   	private String timeStart;

 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public String getRealName(){
		return this.realName;
	}
	public void setRealName(String realName){
		this.realName = realName;
	}
 	public String getTimeEnd(){
		return this.timeEnd;
	}
	public void setTimeEnd(String timeEnd){
		this.timeEnd = timeEnd;
	}
 	public String getTimeStart(){
		return this.timeStart;
	}
	public void setTimeStart(String timeStart){
		this.timeStart = timeStart;
	}
}
