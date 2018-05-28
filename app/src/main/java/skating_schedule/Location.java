
package skating_schedule;

import java.util.List;

public class Location{
   	private Coordinates coordinates;
   	private String desc;
   	private FacilityFeatures facilityFeatures;
   	private String id;
   	private String name;
   	private String type;

 	public Coordinates getCoordinates(){
		return this.coordinates;
	}
	public void setCoordinates(Coordinates coordinates){
		this.coordinates = coordinates;
	}
 	public String getDesc(){
		return this.desc;
	}
	public void setDesc(String desc){
		this.desc = desc;
	}
 	public FacilityFeatures getFacilityFeatures(){
		return this.facilityFeatures;
	}
	public void setFacilityFeatures(FacilityFeatures facilityFeatures){
		this.facilityFeatures = facilityFeatures;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
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
