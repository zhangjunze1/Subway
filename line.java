package sub;

import java.util.ArrayList;
import java.util.List;

public class line {

    private String LineName;  //线名
    private List<String> stations= new ArrayList<String>();   //该线路中所有站

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public void stationAdd(String name){
        stations.add(name);
    }
}
