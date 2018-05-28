
package skating_schedule;

public class Activity{
   	private Month month;
   	private String name;
   	private String type;

 	public Month getMonth(){
		return this.month;
	}
	public void setMonth(Month month){
		this.month = month;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
}
