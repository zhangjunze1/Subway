package sub;

import javafx.scene.shape.Line;

import java.io.*;
import java.util.*;

public class Mymain {

    public static HashMap<String,station> map = new HashMap<>();         //方便查找站点信息
    public static List<line> LineSet= new ArrayList<>();                       //方便查找路线

    public static void getSubway(String filename) {              //读入线路数据
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);

            String read = "";
            read = br.readLine();                                                   //线路数量
            int LineNum = 0;
            LineNum = Integer.parseInt(read);

            for (int i = 0; i <= LineNum; i++) {
                line linex = new line();                                            //当前存的line

                read = br.readLine();
                if(read==null){
                    return;
                }
                String[] stations = read.split(" ");                          //读入的line对应的所有station************************
                linex.setLineName(stations[0]);                                     //输入线路名
                if (stations.length<=1){
                    System.out.print("本线路无可乘站");
                    return;
                }
                for (int j = 1; j < stations.length - 1; j++) {

                    station station1 = new station();                               //当前line中的station
                    station station2 = new station();

                    if (map.containsKey(stations[j])) {                              //如果map中已经有该站点则把这个站点拿出来处理
                        station1 = map.get(stations[j]);
                        map.remove(stations[j]);
                    } else {
                        station1.setStationName(stations[j]);
                        station1.setVisited(false);
                    }

                    if (map.containsKey(stations[j + 1])) {
                        station2 = map.get(stations[j + 1]);
                        map.remove(stations[j + 1]);
                    } else {
                        station2.setStationName(stations[j + 1]);
                        station2.setVisited(false);
                    }

                    if (!station1.getLine().contains(linex.getLineName())) {                  //如果当前站未加入line中，则在line中当前站名
                        station1.AddStationLine(linex.getLineName());
                    }
                    if (!station2.getLine().contains(linex.getLineName())) {                //如果当前站未加入line中，则在line中当前站名
                        station2.AddStationLine(linex.getLineName());
                    }

                    if (!station1.getLinkStations().contains(station2)) {
                        station1.AddLinkStation(station2);
                    }
                    if (!station2.getLinkStations().contains(station1)) {
                        station2.AddLinkStation(station1);
                    }

                    station1.setPreStation(station1.getStationName());
                    station2.setPreStation(station2.getStationName());

                    map.put(stations[j], station1);
                    map.put(stations[j + 1], station2);

                    if (!linex.getStations().contains(station1.getStationName())) {
                        linex.stationAdd(station1.getStationName());
                    }
                    if (!linex.getStations().contains(station2.getStationName())) {
                        linex.stationAdd(station2.getStationName());
                    }

                }
                LineSet.add(linex);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void getStationOfLine(String name ){                             //根据线路名字查找该线路所有站点
        int flag = 0;
        int index = -1;
        for (line line1 :LineSet){
            index++;
            if(line1.getLineName().equals(name)) {
                flag = 1;
                break;
            }
        }
        if (flag==0)
            System.out.println("该地铁线路不存在");
        else {
            System.out.print(name + ": ");
            for (String str : LineSet.get(index).getStations()) {
                System.out.print(str + " ");
            }
        }
    }

    public static void BFS(String start,String end){
        for (String temp :map.keySet()){
            map.get(temp).setVisited(false);
            map.get(temp).setDistance(0);
        }
        station nowStation = new station();

        Queue<String> queue = new LinkedList<>();                   //存放遍历过的站点

        if (!map.containsKey(start)){                               //判断起点站是否存在
            System.out.print("起点站不存在");
            return;
        }
        if (!map.containsKey(end)){
            System.out.print("终点站不存在");                         //判断终点站是否存在
            return;
        }

        if (start.equals(end)){
            System.out.print("起点站与终点站相同 本站为"+end);
            return;
        }
        nowStation = map.get(start);

        queue.add(start);
        while(!queue.isEmpty()){
            String nowStationName = queue.poll();
            map.get(nowStationName).setVisited(true);

            if (nowStation.getStationName().equals(end)){
                break;
            }
            for (station station1 :map.get(nowStationName).getLinkStations()){
                if(!map.get(station1.getStationName()).isVisited()){                    //未访问过的临近站点
                    map.get(station1.getStationName()).setPreStation(nowStationName);   //为preStation赋值
                    map.get(station1.getStationName()).setDistance(map.get(nowStationName).getDistance()+1);//临近站的距离为本站距离加1
                    queue.offer(station1.getStationName());
                }
            }
        }
    }

    public static void PrintPath(String start,String end){

        List<String> path = new ArrayList<>();
        Stack<String> printline = new Stack<>();
        int numStation = 1;//第几站
        int cnt = 0;//换乘次数

        String str = end;

        while(!str.equals(start)){
            path.add(str);
            printline.push(str);
            str = map.get(str).getPreStation();
        }
        path.add(str);              //把起点放进去
        printline.push(str);

        for (int i=1;i<path.size()-1;i++){
            if (map.get(path.get(i)).getLine().size()==1){
                continue;
            }
            String temp1="";
            String temp2="";
            for (String str1 : map.get(path.get(i)).getLine()){
                int flag=0;
                for (String str2 :map.get(path.get(i-1)).getLine()){
                    if (str1.equals(str2)){
                        temp1 = temp1+str1;
                        flag=1;
                        break;
                    }
                }
                if (flag==1)
                    break;
            }
            for (String str1 : map.get(path.get(i)).getLine()){
                int flag=0;
                for (String str2 :map.get(path.get(i+1)).getLine()){
                    if (str1.equals(str2)){
                        temp2 = temp2+str1;
                        flag=1;
                        break;
                    }
                }
                if (flag==1)
                    break;
            }
            if (!temp1.equals(temp2))
                map.get(path.get(i)).setIfchange(true);
        }//判断path中的换乘站

        System.out.println("共"+path.size()+"站");
        while(!printline.empty()){
            String printStation = printline.pop();
            if(numStation==1){
                for (String strnow : map.get(printStation).getLine()){
                    for (String nextStation : map.get(path.get(path.size()-numStation-1)).getLine()){
                        if (strnow.equals(nextStation)) {
                            System.out.println("当前线为："+strnow);

                        }
                    }
                }
            }
            if (map.get(printStation).isIfchange()){
                String nowline ="";
                for (String strnow : map.get(printStation).getLine()){
                    //path.get(path.size()-numStation)换乘站下一站 两站共有的线路就是换乘到的线路
                    for (String nextStation : map.get(path.get(path.size()-numStation-1)).getLine()){
                        if (strnow.equals(nextStation))
                            nowline= nowline + strnow;
                    }
                }
                cnt++;
                System.out.println("");
                System.out.println("转线->"+nowline);
            }
            System.out.print(printStation+" ");
            numStation++;
        }


    }


    public static void main(String[] ards){

        getSubway("C:\\Users\\41205\\IdeaProjects\\Java\\Subway\\src\\sub\\sub.txt");
        for (line linex: LineSet){
            System.out.print(linex.getLineName()+" ");
        }
        System.out.println("");
        System.out.println("1.查询线路(线路包含的所有站)");
        System.out.println("2.查询路线");
        System.out.print("请输入序号(1/2):");

        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        if (input==1){
            System.out.print("请输入线路名称：");
            Scanner sc1 = new Scanner(System.in);
            String linename = sc1.nextLine();
            getStationOfLine(linename);
        }
        else if (input==2){
            System.out.print("请输入起点站：");
            Scanner sc2 = new Scanner(System.in);
            String start =sc2.nextLine();
            System.out.print("请输入终点站：");
            String end = sc2.nextLine();
            BFS(start,end);
            PrintPath(start,end);
        }
        else
            System.out.println("请重新选择");
        return;
    }

}
