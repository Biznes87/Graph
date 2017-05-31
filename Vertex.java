import java.util.*;

/**
 * Created by dex on 5/21/17.
 */
public class Vertex implements Comparable<Vertex>{

    public String label;
    public boolean isInTree;
    public String machineryType; //Тип машин A,B или С
    public int index; //индекс в матрице смежности
    public static List<Integer> indexBox = new ArrayList<>();
    public static List<Vertex> vertexBox = new ArrayList<>();
    public List<String> machineryOnField =new ArrayList<>();

    public Vertex(String label,int index, String machineryType) {
        this.label = label;
        this.machineryType=machineryType;
        isInTree=false;
        this.index=index;
        indexBox.add(index);
        vertexBox.add(this);
    }

    public void setInTreeTrue() {
        isInTree = true;
    }

    public void setInTreeFalse() {
        isInTree = false;
    }

    public boolean isInTree() {
        return isInTree;
    }

    public void addMachineryOnField(String typeOfmachinery){

        machineryOnField.add(typeOfmachinery);
    }

    public static void shiftIndex(int shift){
       ArrayList<Integer> t = new ArrayList<>();
        for (int i = 0; i <indexBox.size() ; i++) {
            if(i-shift>=0){
                t.add(indexBox.get(i-shift));
            }else{
                t.add(indexBox.get(indexBox.size()+i-shift));
            }
        }
        indexBox.clear();
        indexBox.addAll(t);
        t.clear();
        for (int i = 0; i <vertexBox.size() ; i++) {
            vertexBox.get(i).index = indexBox.get(i);
        }
        System.out.println(indexBox);
    }

    public static void sortVertexBox(){
        for (Vertex vert:vertexBox) {
            System.out.print(vert.printVertex());

        }
        System.out.println();
        Collections.sort(vertexBox);
        for (Vertex vert:vertexBox) {
            System.out.print(vert.printVertex());

        }
        System.out.println();
    }

    public String printVertex(){
        String str ="";
        return str = label + " - "+index;
    }

    @Override
    public int compareTo(Vertex anotherVertex) {
        return compare(this.index, anotherVertex.index);
    }

    public static int compare(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
}
